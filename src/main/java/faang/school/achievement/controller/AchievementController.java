package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    private final UserContext userContext;

    public List<AchievementResponseDto> getAchievements(AchievementRequestFilterDto requestDto) {
        return achievementService.getAchievements(requestDto);
    }

    public List<AchievementResponseDto> getAchievementsByUserId() {
        return achievementService.getAchievementsByUserId(userContext.getUserId());
    }

    public AchievementResponseDto getAchievementById(int achievementId) {
        return achievementService.getAchievementById(achievementId);
    }

    public List<AchievementResponseDto> getAchievementsInProgressByUserId() {
        return achievementService.getAchievementsInProgressByUserId(userContext.getUserId());
    }

}
