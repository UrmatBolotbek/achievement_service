package faang.school.achievement.handler.subscription;

import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CelebrityAchievementHandler extends AbstractAchievementHandler<FollowerEvent> {

    @Value("${achievement.celebrity}")
    private String celebrity;

    public CelebrityAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    @Transactional
    public void handle(FollowerEvent event) {
        handleAchievement(event.getFolloweeId(), celebrity);
    }
}