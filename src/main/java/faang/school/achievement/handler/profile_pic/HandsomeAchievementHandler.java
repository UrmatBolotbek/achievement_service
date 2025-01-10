package faang.school.achievement.handler.profile_pic;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.AbstractAchievementHandler;
import faang.school.achievement.service.AchievementService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractAchievementHandler<ProfilePicEvent> {

    @Value("${achievement.profile-pic}")
    private String profilePic;

    public HandsomeAchievementHandler(AchievementService achievementService) {
        super(achievementService);
    }

    @Override
    @Async("fixedThreadPool")
    @Transactional
    public void handle(ProfilePicEvent event) {
        handleAchievement(event.getUserId(), profilePic);
    }
}