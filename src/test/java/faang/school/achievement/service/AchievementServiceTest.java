package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.mapper.AchievementMapperImpl;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.EventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement_filter.AchievementFilter;
import faang.school.achievement.validator.AchievementValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    private final long USER_ID = 19;

    @Captor
    private ArgumentCaptor<UserAchievement> userAchievementCaptor;

    @Captor
    private ArgumentCaptor<PublishEvent> publishEventCaptor;

    private AchievementService achievementService;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private AchievementRepository achievementRepository;
    @Spy
    private AchievementMapperImpl mapper;
    @Mock
    private AchievementValidator achievementValidator;

    private Achievement achievement;
    private AchievementProgress achievementProgress;
    private List<AchievementFilter> filters;
    private AchievementResponseDto achievementResponseDto;

    @BeforeEach
    public void setUp() {
        AchievementFilter filter = Mockito.mock(AchievementFilter.class);
        filters = Collections.singletonList(filter);
        achievementService = new AchievementService(userAchievementRepository,
                achievementProgressRepository,
                eventPublisher,
                achievementRepository,
                filters,
                mapper,
                achievementValidator);
        achievement = Achievement.builder()
                .title("HANDSOME")
                .id(25L)
                .build();
        achievementProgress = AchievementProgress.builder()
                .achievement(achievement)
                .currentPoints(10L)
                .userId(19L)
                .build();
        achievementResponseDto = AchievementResponseDto.builder()
                .title("HANDSOME")
                .id(25L)
                .build();
    }

    @Test
    void testGetAchievementsWithFiltersSuccess() {
        List<Achievement> achievements = Collections.singletonList(achievement);
        when(achievementRepository.findAll()).thenReturn(achievements);
        when(filters.get(0).isApplicable(new AchievementRequestFilterDto())).thenReturn(true);
        when(filters.get(0).apply(any(), any())).thenReturn(List.of(achievement));

        List<AchievementResponseDto> realList = achievementService.getAchievementsWithFilters(new AchievementRequestFilterDto());

        verify(achievementRepository).findAll();
        assertEquals(realList,mapper.toResponseDtoList(Collections.singletonList(achievement)));
    }

    @Test
    void testGetAchievementsByUserIdSuccess() {
        UserAchievement userAchievement = new UserAchievement();
        userAchievement.setUserId(USER_ID);
        userAchievement.setAchievement(achievement);
        when(userAchievementRepository.findByUserId(USER_ID)).thenReturn(List.of(userAchievement));

        List<AchievementResponseDto> realList = achievementService.getAchievementsByUserId(USER_ID);
        verify(userAchievementRepository).findByUserId(USER_ID);

        assertEquals(realList,mapper.toResponseDtoList(Collections.singletonList(achievement)));
    }

    @Test
    void testGetAchievementByIdSuccess() {
        when(achievementValidator.validateAchievement(25L)).thenReturn(achievement);
        assertEquals(achievementResponseDto,achievementService.getAchievementById(25L));
    }

    @Test
    void testGetAchievementsInProgressByUserId() {
        AchievementProgress userAchievement = new AchievementProgress();
        userAchievement.setUserId(USER_ID);
        userAchievement.setAchievement(achievement);
        when(achievementProgressRepository.findByUserId(USER_ID)).thenReturn(List.of(userAchievement));

        List<AchievementResponseDto> realList = achievementService.getAchievementsInProgressByUserId(USER_ID);
        verify(achievementProgressRepository).findByUserId(USER_ID);

        assertEquals(realList,mapper.toResponseDtoList(Collections.singletonList(achievement)));
    }

    @Test
    void testGetAchievementSuccess() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.ofNullable(achievement));
        assertDoesNotThrow(()-> achievementService.getAchievement("HANDSOME"));
    }

    @Test
    void testGetAchievementFailure() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> achievementService.getAchievement("HANDSOME"));
    }

    @Test
    void testHasAchievementWithTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(USER_ID, 25L)).thenReturn(true);
        Assertions.assertTrue(achievementService.hasAchievement(USER_ID, 25L));
    }

    @Test
    void testCreateProgressIfNecessary() {
        assertDoesNotThrow(()->  achievementService.createProgressIfNecessary(USER_ID, 25L));
    }

    @Test
    void testGetProgressSuccess() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(USER_ID, 25L))
                .thenReturn(Optional.ofNullable(achievementProgress));
        long result = achievementService.getProgress(USER_ID, 25L);
        verify(achievementProgressRepository).save(achievementProgress);
        Assertions.assertEquals(11, result);
    }

    @Test
    void testGiveAchievementSuccess() {
        achievementService.giveAchievement(USER_ID, achievement);
        verify(userAchievementRepository).save(userAchievementCaptor.capture());
        verify(eventPublisher).publish(publishEventCaptor.capture());
    }

}
