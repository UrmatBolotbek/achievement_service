package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.io.IOException;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisMessageListenerContainer container;


    @PostConstruct
    public void init() {
        container.addMessageListener(this, new ChannelTopic(getTopicName()));
        log.info("Registered listener {} on topic {}", this.getClass().getSimpleName(), getTopicName());

    }

    protected void handleEvent(Message message, Class<T> clazz, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }

    protected abstract String getTopicName();
}