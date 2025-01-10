package faang.school.achievement.listener.profile_pic;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> {
    @Value("${spring.data.redis.channels.profile-pic-channel}")
    private String topicProfilePic;

    public ProfilePicEventListener(List<EventHandler<ProfilePicEvent>> eventHandlers,
                                   RedisMessageListenerContainer container,
                                   ObjectMapper objectMapper) {
        super(eventHandlers, container, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfilePicEvent.class);
    }

    @Override
    protected String getTopicName() {
        return topicProfilePic;
    }
}
