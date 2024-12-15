package faang.school.achievement.handler.subscription;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CelebrityAchievementHandler extends AbstractAchievementHandler<FollowerEvent> {
    private static final String ACHIEVEMENT_TITLE = "CELEBRITY";

    public CelebrityAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), ACHIEVEMENT_TITLE);
    }
}
