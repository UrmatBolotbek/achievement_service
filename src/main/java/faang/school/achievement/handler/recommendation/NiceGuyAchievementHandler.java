package faang.school.achievement.handler.recommendation;

import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import faang.school.achievement.model.AchievementTitle;
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
    @Transactional
    public void handle(RecommendationEvent event) {
        handleAchievement(event.getAuthorId(), niceGuy);
    }
}
