package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AchievementControllerTest {

    private final long USER_ID = 25L;

    private MockMvc mockMvc;

    @InjectMocks
    private AchievementController achievementController;
    @Mock
    private AchievementService achievementService;
    @Mock
    private UserContext userContext;

    private AchievementRequestFilterDto requestFilterDto;
    private AchievementResponseDto responseDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();

        requestFilterDto = new AchievementRequestFilterDto();
        requestFilterDto.setDescription("description");
        requestFilterDto.setTitle("title");
        responseDto = AchievementResponseDto.builder()
                .title("title")
                .description("description")
                .build();
    }

    @Test
    void testGetAchievements() throws Exception {
        when(achievementService.getAchievementsWithFilters(requestFilterDto)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/achievements/filters?title=title" +
                        "&description=description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$[0].description").value(responseDto.getDescription()));
    }

    @Test
    void testGetAchievementsByUserId() throws Exception {
        when(userContext.getUserId()).thenReturn(USER_ID);
        when(achievementService.getAchievementsByUserId(USER_ID)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/achievements/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$[0].description").value(responseDto.getDescription()));
    }

    @Test
    void testGetAchievementById() throws Exception {
        long ACHIEVEMENT_ID = 19L;
        when(achievementService.getAchievementById(ACHIEVEMENT_ID)).thenReturn(responseDto);

        mockMvc.perform(get("/api/v1/achievements/" + ACHIEVEMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(responseDto.getDescription()));
    }

    @Test
    void testGetAchievementsInProgressByUserId() throws Exception {
        when(userContext.getUserId()).thenReturn(USER_ID);
        when(achievementService.getAchievementsInProgressByUserId(USER_ID)).thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/achievements/user/in_progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(responseDto.getTitle()))
                .andExpect(jsonPath("$[0].description").value(responseDto.getDescription()));
    }
}

