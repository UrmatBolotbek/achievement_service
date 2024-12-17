package faang.school.achievement.publisher;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.PublishEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.data.redis.channels.achievement-channel}")
    private String topicAchievement;

    public void publish(PublishEvent event) {
        try {
            String eventToPublish = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(topicAchievement, eventToPublish);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while working with JSON: ", e);
            throw new RuntimeException(e);
        }
    }
}
