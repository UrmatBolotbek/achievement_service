package faang.school.achievement.service;

import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.EventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private EventPublisher eventPublisher;
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
    void testGetByTitleNotInCacheButInDB() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.of(achievement));

        Achievement result = achievementService.getByTitle("HANDSOME");
        Assertions.assertEquals(achievement, result);
    }

    @Test
    void testGetAchievementSuccess() {
        when(achievementRepository.findByTitle("HANDSOME")).thenReturn(Optional.of(achievement));
        assertDoesNotThrow(() -> achievementService.getByTitle("HANDSOME"));
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
    void testGiveAchievementSuccess() {
        achievementService.giveAchievement(19L, achievement);
        verify(userAchievementRepository).save(userAchievementCaptor.capture());
        verify(eventPublisher).publish(publishEventCaptor.capture());

        UserAchievement saved = userAchievementCaptor.getValue();
        Assertions.assertEquals(19L, saved.getUserId());
        Assertions.assertEquals(achievement, saved.getAchievement());

        PublishEvent published = publishEventCaptor.getValue();
        Assertions.assertEquals(19L, published.getUserId());
        Assertions.assertEquals(25L, published.getAchievementId());
        Assertions.assertEquals("HANDSOME", published.getAchievementName());
    }
}
