package faang.school.achievement.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    @Value("${spring.data.redis.channels.follower-event-channel}")
    private String topicFollower;

    public FollowerEventListener(List<EventHandler<FollowerEvent>> eventHandlers,
                                 RedisMessageListenerContainer container,
                                 ObjectMapper objectMapper) {
        super(eventHandlers, container, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class);
    }

    @Override
    protected String getTopicName() {
        return topicFollower;
    }
}
