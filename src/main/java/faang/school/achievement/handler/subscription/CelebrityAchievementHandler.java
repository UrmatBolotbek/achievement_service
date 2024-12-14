package faang.school.achievement.handler.subscription;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CelebrityAchievementHandler extends FollowerEventHandler  {
    private static final int NUMBER_OF_FOLLOWERS = 1_000_000;
    private static final String ACHIEVEMENT_TITLE = "CELEBRITY";

    public CelebrityAchievementHandler(AchievementService service) {
        super(service);
    }

    @Async("fixedThreadPool")
    @Override
    public void handle(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), ACHIEVEMENT_TITLE, NUMBER_OF_FOLLOWERS);
    }
}
