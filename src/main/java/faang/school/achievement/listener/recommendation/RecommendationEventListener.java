package faang.school.achievement.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.RecommendationEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {

    @Value("${spring.data.redis.channels.recommendation-channel}")
    private String topicRecommendation;

    private final EventHandler<RecommendationEvent> niceGuyAchievementHandler;

    public RecommendationEventListener(
            ObjectMapper objectMapper,
            RedisMessageListenerContainer container,
            EventHandler<RecommendationEvent> niceGuyAchievementHandler
    ) {
        super(objectMapper, container);
        this.niceGuyAchievementHandler = niceGuyAchievementHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class, niceGuyAchievementHandler::handle);
    }

    @Override
    protected String getTopicName() {
        return topicRecommendation;
    }
}
