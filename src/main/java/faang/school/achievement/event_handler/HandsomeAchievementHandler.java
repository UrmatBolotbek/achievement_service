package faang.school.achievement.event_handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractAchievementHandler implements EventHandler<ProfilePicEvent> {

    @Value("${achievement.profile-pic}")
    private String profilePic;

    public HandsomeAchievementHandler(AchievementService achievementService,
                                      AchievementRepository achievementRepository) {
        super(achievementService, achievementRepository);
    }

    @Override
    public void handle(ProfilePicEvent event) {
        handleAchievement(event.getUserId(), profilePic);
    }

}
