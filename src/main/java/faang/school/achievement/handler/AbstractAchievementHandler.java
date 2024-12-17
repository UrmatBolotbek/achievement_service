package faang.school.achievement.handler;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractAchievementHandler<T> implements EventHandler<T> {
    protected final AchievementService service;

    protected void handleAchievement(long userId, String achievementName) {
        Achievement achievement = service.getByTitle(achievementName);
        if (!service.hasAchievement(userId, achievement.getId())) {
            service.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress achievementProgress = service.getProgress(userId, achievement.getId());
            if (achievementProgress.getCurrentPoints() >= achievement.getPoints()) {
                service.giveAchievement(userId, achievement);
                log.info("User with ID {} received achievement {}", userId, achievementName);
            } else {
                service.incrementProgress(achievementProgress);
            }
        }
    }
}

