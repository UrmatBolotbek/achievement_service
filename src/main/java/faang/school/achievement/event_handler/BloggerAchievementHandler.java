package faang.school.achievement.event_handler;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BloggerAchievementHandler extends AbstractAchievementHandler<FollowerEvent> {

    @Value("${achievement.blogger}")
    private String blogger;

    public BloggerAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), blogger);
    }
}