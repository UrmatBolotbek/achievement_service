package faang.school.achievement.handler.profile_pic;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import faang.school.achievement.model.AchievementTitle;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractAchievementHandler<ProfilePicEvent> {

    public HandsomeAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(ProfilePicEvent event) {
        handleAchievement(event.getUserId(), AchievementTitle.HANDSOME);
    }

}
