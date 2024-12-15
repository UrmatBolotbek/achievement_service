package faang.school.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {
    @Value("${spring.data.redis.channels.recommendation-channel}")
    private String topicRecommendation;

    public RecommendationEventListener(List<EventHandler<RecommendationEvent>> eventHandlers,
                                       RedisMessageListenerContainer container,
                                       ObjectMapper objectMapper) {
        super(eventHandlers, container, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class);
    }

    @Override
    protected String getTopicName() {
        return topicRecommendation;
    }
}
