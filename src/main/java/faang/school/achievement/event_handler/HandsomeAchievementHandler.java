package faang.school.achievement.event_handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.publisher.profile_pic.ProfilePicEventPublisher;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HandsomeAchievementHandler extends AbstractAchievementHandler implements EventHandler<ProfilePicEvent> {

    @Value("${achievement.profile-pic}")
    private String profilePic;

    private final ProfilePicEventPublisher publisher;

    public HandsomeAchievementHandler(AchievementService achievementService,
                                      AchievementRepository achievementRepository,
                                      ProfilePicEventPublisher profilePicEventPublisher
                                      ) {
        super(achievementService, achievementRepository);
        this.publisher = profilePicEventPublisher;
    }

    @Override
    public void handle(ProfilePicEvent event) {
        handleAchievement(event.getUserId(), profilePic);
        if(isPublish()) {
            publisher.publish(event);
        }
    }

}
