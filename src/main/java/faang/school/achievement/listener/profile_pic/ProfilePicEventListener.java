package faang.school.achievement.listener.profile_pic;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> {

    @Value("${spring.data.redis.channels.profile-pic-channel}")
    private String topicProfilePic;

    private final EventHandler<ProfilePicEvent> profilePicEventEventHandler;

    public ProfilePicEventListener(ObjectMapper objectMapper,
                                   RedisMessageListenerContainer container,
                                   EventHandler<ProfilePicEvent> profilePicEventEventHandler) {
        super(objectMapper,container);
        this.profilePicEventEventHandler = profilePicEventEventHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfilePicEvent.class, profilePicEventEventHandler::handle);
    }

    @Override
    protected String getTopicName() {
        return topicProfilePic;
    }
}
