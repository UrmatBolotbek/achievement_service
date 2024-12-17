package faang.school.achievement.handler.comment;

import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AngryCommenterAchievementHandler extends AbstractAchievementHandler<CommentEvent> {

    @Value("${achievement.angry-commenter}")
    private String angryCommenter;

    public AngryCommenterAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    @Transactional
    public void handle(CommentEvent event) {
        handleAchievement(event.getCommentAuthorId(), angryCommenter);
    }
}