package faang.school.achievement.event_handler;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.subscription.BloggerAchievementHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BloggerAchievementHandlerTest {
    @Mock
    private AchievementService service;

    @InjectMocks
    private BloggerAchievementHandler handler;

    private Achievement achievement;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .title("BLOGGER")
                .id(3L)
                .points(500L)
                .build();
    }

    @Test
    void testHandleSuccess() {
        FollowerEvent followerEvent = new FollowerEvent();
        followerEvent.setFolloweeId(1L);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(500L);

        when(service.getByTitle("BLOGGER")).thenReturn(achievement);
        when(service.hasAchievement(1L, 3L)).thenReturn(false);
        when(service.getProgress(1L, 3L)).thenReturn(achievementProgress);

        assertDoesNotThrow(() -> handler.handle(followerEvent));

        verify(service).createProgressIfNecessary(1L, 3L);
        verify(service).giveAchievement(1L, achievement);
    }
}