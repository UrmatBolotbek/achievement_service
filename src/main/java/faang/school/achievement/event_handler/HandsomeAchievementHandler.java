package faang.school.achievement.event_handler;

import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class HandsomeAchievementHandler implements EventHandler<ProfilePicEvent> {

    @Value("${achievement.profile-pic}")
    private String profilePic;

    private final AchievementService achievementService;






}
