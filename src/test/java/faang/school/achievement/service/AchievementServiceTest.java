package faang.school.achievement.service;

import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class AchievementServiceTest {

    @Captor
    private ArgumentCaptor<UserAchievement> userAchievementCaptor;

    @Captor
    private ArgumentCaptor<PublishEvent> publishEventCaptor;

    @InjectMocks
    private AchievementService achievementService;
    @Mock
    private UserAchievementRepository userAchievementRepository;
    @Mock
    private AchievementProgressRepository achievementProgressRepository;
    @Mock
    private AchievementPublisher achievementPublisher;
    @Mock
    private AchievementRepository achievementRepository;

    private Achievement achievement;
    private AchievementProgress achievementProgress;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .title("HANDSOME")
                .id(25L)
                .build();
        achievementProgress = AchievementProgress.builder()
                .achievement(achievement)
                .currentPoints(10L)
                .userId(19L)
                .build();
    }

    @Test
    void testGetByTitleFoundInCache() {
        Achievement result = achievementService.getAchievement("HANDSOME");
        Assertions.assertEquals(achievement, result);
        verifyNoInteractions(achievementRepository);
    }

    @Test
    void testGetByTitleNotInCacheButInDB() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.getAchievement("HANDSOME");
        Assertions.assertEquals(achievement, result);
    }

    @Test
    void testGetByTitleNotFoundAnywhere() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievement("HANDSOME"));
    }

    @Test
    void testGetAchievementSuccess() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.of(achievement));
        assertDoesNotThrow(() -> achievementService.getAchievement("HANDSOME"));
    }

    @Test
    void testGetAchievementFailure() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> achievementService.getAchievement("HANDSOME"));
    }

    @Test
    void testHasAchievementWithTrue() {
        when(userAchievementRepository.existsByUserIdAndAchievementId(19L, 25L)).thenReturn(true);
        Assertions.assertTrue(achievementService.hasAchievement(19L, 25L));
    }

    @Test
    void testCreateProgressIfNecessary() {
        assertDoesNotThrow(() -> achievementService.createProgressIfNecessary(19L, 25L));
    }

    @Test
    void testGetProgressSuccess() {
        when(achievementProgressRepository.findByUserIdAndAchievementId(19L, 25L))
                .thenReturn(Optional.of(achievementProgress));

        long result = achievementService.getCurrentPointsOfProgress(19L, 25L);

        verify(achievementProgressRepository).save(achievementProgress);
        Assertions.assertEquals(11, result);
    }

    @Test
    void testGiveAchievementSuccess() {
        achievementService.giveAchievement(19L, achievement);
        verify(userAchievementRepository).save(userAchievementCaptor.capture());
        verify(achievementPublisher).publish(publishEventCaptor.capture());

        UserAchievement saved = userAchievementCaptor.getValue();
        Assertions.assertEquals(19L, saved.getUserId());
        Assertions.assertEquals(achievement, saved.getAchievement());

        PublishEvent published = publishEventCaptor.getValue();
        Assertions.assertEquals(19L, published.getUserId());
        Assertions.assertEquals(25L, published.getAchievementId());
        Assertions.assertEquals("HANDSOME", published.getAchievementName());
    }
}
