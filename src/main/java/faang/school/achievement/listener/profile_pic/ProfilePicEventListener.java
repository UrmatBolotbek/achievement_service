package faang.school.achievement.listener.profile_pic;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.event_handler.HandsomeAchievementHandler;
import faang.school.achievement.listener.AbstractEventListener;
import faang.school.achievement.publisher.profile_pic.ProfilePicEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> implements MessageListener {

    private final HandsomeAchievementHandler handsomeAchievementHandler;
    private final ProfilePicEventPublisher profilePicEventPublisher;

    public ProfilePicEventListener(ObjectMapper objectMapper, HandsomeAchievementHandler handsomeAchievementHandler, ProfilePicEventPublisher profilePicEventPublisher) {
        super(objectMapper);
        this.handsomeAchievementHandler = handsomeAchievementHandler;
        this.profilePicEventPublisher = profilePicEventPublisher;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfilePicEvent.class, event -> {
            handsomeAchievementHandler.handle(event);
            profilePicEventPublisher.publish(event);
        });
    }
}
