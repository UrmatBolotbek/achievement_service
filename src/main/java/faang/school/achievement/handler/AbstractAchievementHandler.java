package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@Slf4j
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {
    protected final AchievementService achievementService;

    protected void handleAchievement(long userId, String achievementName) {
        Achievement achievement = achievementService.getAchievement(achievementName);
        if (!achievementService.hasAchievement(userId, achievement.getId())) {
            achievementService.createProgressIfNecessary(userId, achievement.getId());
            long achievementProgressPoints = achievementService.getCurrentPointsOfProgress(userId, achievement.getId());
            if (achievementProgressPoints >= achievement.getPoints()) {
                achievementService.giveAchievement(userId, achievement);
                log.info("User with ID {} received achievement {}", userId, achievementName);
            }
        }
    }
}

