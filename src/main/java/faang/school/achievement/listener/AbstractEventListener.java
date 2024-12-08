package faang.school.achievement.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> clazz, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            consumer.accept(event);
        } catch (IOException e) {
            log.error("Error deserializing JSON to object", e);
            throw new RuntimeException("Error deserializing JSON to object", e);
        }
    }

}