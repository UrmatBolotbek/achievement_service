package faang.school.achievement.event_handler;

import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractAchievementHandler<RecommendationEvent> {
    @Value("${achievement.nice-guy}")
    private String niceGuy;

    public NiceGuyAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(RecommendationEvent event) {
        handleAchievement(event.getAuthorId(), niceGuy);
    }
}
