package faang.school.achievement.event_handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractAchievementHandler<ProfilePicEvent>{

    @Value("${achievement.profile-pic}")
    private String profilePic;

    public HandsomeAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    public void handle(ProfilePicEvent event) {
        handleAchievement(event.getUserId(), profilePic);
    }

}
