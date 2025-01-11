package faang.school.achievement.handler.comment;


import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AngryCommenterAchievementHandlerTest {
    @Mock
    private AchievementService service;
    @InjectMocks
    private AngryCommenterAchievementHandler handler;

    private Achievement achievement;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .title("ANGRY_COMMENTER")
                .id(4L)
                .points(100L)
                .build();
    }

    @Test
    void testHandleSuccess() {
        CommentEvent commentEvent = new CommentEvent();
        commentEvent.setCommentAuthorId(1L);
        AchievementProgress achievementProgress = new AchievementProgress();
        achievementProgress.setCurrentPoints(100L);

        when(service.getByTitle("ANGRY_COMMENTER")).thenReturn(achievement);
        when(service.hasAchievement(1L, 4L)).thenReturn(false);
        when(service.getProgress(1L, 4L)).thenReturn(achievementProgress);

        assertDoesNotThrow(() -> handler.handle(commentEvent));

        verify(service).createProgressIfNecessary(1L, 4L);
        verify(service).giveAchievement(1L, achievement);
    }
}