package faang.school.achievement.event_handler;

import faang.school.achievement.model.Achievement;
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
public class NiceGuyAchievementHandlerTest {
    @Mock
    private AchievementService service;

    @InjectMocks
    private NiceGuyAchievementHandler handler;

    private Achievement achievement;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .title("NICE GUY")
                .id(9L)
                .points(5L)
                .build();
    }

    @Test
    void testHandleSuccess() {
        when(service.getByTitle("NICE GUY")).thenReturn(achievement);
        when(service.hasAchievement(1L,9L)).thenReturn(false);
        when(service.getProgress(1L,9L)).thenReturn(5L);

        assertDoesNotThrow(()-> handler.handleAchievement(1L, "NICE GUY"));

        verify(service).createProgressIfNecessary(1L,9L);
        verify(service).giveAchievement(1L, achievement);
    }
}

