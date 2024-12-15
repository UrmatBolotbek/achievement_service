package faang.school.achievement.event_handler;

import faang.school.achievement.model.Achievement;
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
        when(service.getByTitle("BLOGGER")).thenReturn(achievement);
        when(service.hasAchievement(1L, 3L)).thenReturn(false);
        when(service.getProgress(1L, 3L)).thenReturn(500L);

        assertDoesNotThrow(() -> handler.handleAchievement(1L, "BLOGGER"));

        verify(service).createProgressIfNecessary(1L, 3L);
        verify(service).giveAchievement(1L, achievement);
    }
}