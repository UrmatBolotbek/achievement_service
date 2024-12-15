package faang.school.achievement.handler.recommendation;

import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NiceGuyAchievementHandler extends AbstractAchievementHandler<RecommendationEvent> {
    private static final String ACHIEVEMENT_TITLE = "NICE_GUY";


    public NiceGuyAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(RecommendationEvent event) {
        handleAchievement(event.getAuthorId(), ACHIEVEMENT_TITLE);
    }
}
