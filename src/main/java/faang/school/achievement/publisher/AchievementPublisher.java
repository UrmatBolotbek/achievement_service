package faang.school.achievement.publisher;


import faang.school.achievement.event.PublishEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementPublisher {
    @Value("${spring.data.redis.channels.achievement-channel}")
    private String topicAchievement;
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(PublishEvent event) {
        redisTemplate.convertAndSend(topicAchievement, event);
    }
}
