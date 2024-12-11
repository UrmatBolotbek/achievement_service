package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.model.Rarity;
import faang.school.achievement.service.AchievementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementControllerTest {

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
        requestFilterDto = new AchievementRequestFilterDto();
        requestFilterDto.setRarity(Rarity.UNCOMMON);
        requestFilterDto.setDescription("description");
        requestFilterDto.setTitle("title");
        responseDto = AchievementResponseDto.builder()
                .title("title")
                .description("description")
                .rarity(Rarity.UNCOMMON)
                .build();
    }

    @Test
    void testGetAchievements() throws Exception {
        when(achievementService.getAchievementsWithFilters(requestFilterDto)).thenReturn(List.of(responseDto));



    }


}
