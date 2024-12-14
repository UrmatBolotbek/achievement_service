package faang.school.achievement.handler.subscription;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class FollowerEventHandler implements EventHandler<FollowerEvent> {
    protected final AchievementService service;

    protected void handleAchievement(long userId, String achievementTitle, long achievementPoints) {
        Achievement achievement = service.getAchievement(achievementTitle);
        boolean hasAchievement = service.hasAchievement(userId, achievement.getId());

        if (!hasAchievement) {
            service.createProgressIfNecessary(userId, achievement.getId());
            AchievementProgress progress = service.getProgress(userId, achievement.getId());
            if (progress == null) {
                progress = new AchievementProgress();
            }
            if (progress.getCurrentPoints() >= achievementPoints) {
                service.giveAchievement(userId, achievement);
                log.info("User {} received achievement {}", userId, achievement.getTitle());
            } else {
                progress.increment();
                //  service.saveProgress(progress);
            }
        } else {
            log.warn("User with id {} already has achievement {}", userId, achievement.getTitle());
        }
    }
}
