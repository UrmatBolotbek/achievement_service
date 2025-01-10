package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.handler.EventHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {
    private final List<EventHandler<T>> handlers;
    private final RedisMessageListenerContainer container;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(getTopicName()));
        log.info("Registered listener {} on topic {}", this.getClass().getSimpleName(), getTopicName());

    }

    protected void handleEvent(Message message, Class<T> clazz) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            handlers.forEach(handler -> handler.handle(event));
        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }

    protected abstract String getTopicName();
}