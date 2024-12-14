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
public class HandsomeAchievementHandlerTest {

    @InjectMocks
    private HandsomeAchievementHandler handler;
    @Mock
    private AchievementService service;

    private Achievement achievement;

    @BeforeEach
    public void setUp() {
        achievement = Achievement.builder()
                .title("HANDSOME")
                .id(25L)
                .points(10L)
                .build();
    }

    @Test
    void testHandleSuccess() {
        when(service.getByTitle("HANDSOME")).thenReturn(achievement);
        when(service.hasAchievement(19L,25L)).thenReturn(false);
        when(service.getProgress(19L,25L)).thenReturn(11L);

        assertDoesNotThrow(()-> handler.handleAchievement(19L, "HANDSOME"));

        verify(service).createProgressIfNecessary(19L,25L);
        verify(service).giveAchievement(19L, achievement);
    }

}
