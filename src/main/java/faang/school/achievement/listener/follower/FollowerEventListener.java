package faang.school.achievement.listener.follower;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.event_handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {

    @Value("${spring.data.redis.channels.follower-event-channel}")
    private String topicFollower;

    private final EventHandler<FollowerEvent> bloggerAchievementHandler;

    public FollowerEventListener(
            ObjectMapper objectMapper,
            RedisMessageListenerContainer container,
            EventHandler<FollowerEvent> bloggerAchievementHandler
    ) {
        super(objectMapper, container);
        this.bloggerAchievementHandler = bloggerAchievementHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, bloggerAchievementHandler::handle);
    }

    @Override
    protected String getTopicName() {
        return topicFollower;
    }
}