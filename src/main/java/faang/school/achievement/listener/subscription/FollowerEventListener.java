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

    private final List<EventHandler<FollowerEvent>> handlers;
    @Value("${spring.data.redis.channels.follower-event-channel}")
    private String topicFollower;

    public FollowerEventListener(ObjectMapper objectMapper,
                                 RedisMessageListenerContainer container,
                                 List<EventHandler<FollowerEvent>> handlers) {
        super(objectMapper, container);
        this.handlers = handlers;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, event -> {
            for (EventHandler<FollowerEvent> handler : handlers) {
                handler.handle(event);
            }
        });
    }

    @Override
    protected String getTopicName() {
        return topicFollower;
    }
}