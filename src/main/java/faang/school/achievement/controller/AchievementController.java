package faang.school.achievement.controller;

import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achievements")
public class AchievementController {

    private final AchievementService achievementService;
    private final UserContext userContext;

    @GetMapping("/filters")
    public List<AchievementResponseDto> getAchievements(@ModelAttribute AchievementRequestFilterDto requestDto) {
        return achievementService.getAchievementsWithFilters(requestDto);
    }

    @GetMapping("/user")
    public List<AchievementResponseDto> getAchievementsByUserId() {
        return achievementService.getAchievementsByUserId(userContext.getUserId());
    }

    @GetMapping("/{achievementId}")
    public AchievementResponseDto getAchievementById(@PathVariable long achievementId) {
        return achievementService.getAchievementById(achievementId);
    }

    @GetMapping("/user/in_progress")
    public List<AchievementResponseDto> getAchievementsInProgressByUserId() {
        return achievementService.getAchievementsInProgressByUserId(userContext.getUserId());
    }

    @GetMapping("/in_progress/{userId}")
    public List<AchievementResponseDto> getAchievementsInProgressWithUserId(@PathVariable long userId) {
        return achievementService.getAchievementsInProgressByUserId(userId);
    }

}
