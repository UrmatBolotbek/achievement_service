package faang.school.achievement.handler.comment;

import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.model.AchievementTitle;
import faang.school.achievement.service.AchievementService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AngryCommenterAchievementHandler extends AbstractAchievementHandler<CommentEvent> {

    public AngryCommenterAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(CommentEvent event) {
        handleAchievement(event.getCommentAuthorId(), AchievementTitle.ANGRY_COMMENTER);
    }
}