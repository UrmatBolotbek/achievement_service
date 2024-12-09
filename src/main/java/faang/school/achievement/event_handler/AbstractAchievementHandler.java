package faang.school.achievement.event_handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public abstract class AbstractAchievementHandler {

    private final AchievementService achievementService;
    private final AchievementRepository achievementRepository;

    protected void handleAchievement(long userId, String achievementName) {
        Achievement achievement = achievementRepository.findByTitle(achievementName)
                .orElseThrow(() -> new RuntimeException("No achievement found"));
        boolean result = achievementService.hasAchievement(userId, achievement.getId());
        if (!result) {
            achievementService.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress achievementProgress = achievementService.getProgress(userId, achievement.getId());
            achievementProgress.increment();
            if(achievement.getPoints() == achievementProgress.getCurrentPoints()) {
                achievementService.giveAchievement(userId, achievement);
            }
        }
    }
}


