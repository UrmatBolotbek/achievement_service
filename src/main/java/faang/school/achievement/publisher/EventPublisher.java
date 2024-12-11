package faang.school.achievement.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.PublishEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    @Value("${spring.data.redis.channels.achievement-channel}")
    private String topicAchievement;
    @Qualifier("redisPublishTemplate")
    private final RedisTemplate<String, Object> redisPublishTemplate;
    private final ObjectMapper objectMapper;

    public void publish(PublishEvent event) {
        try {
            String eventToPublish = objectMapper.writeValueAsString(event);
            redisPublishTemplate.convertAndSend(topicAchievement, eventToPublish);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while working with JSON: ", e);
            throw new RuntimeException(e);
        }
    }

}
