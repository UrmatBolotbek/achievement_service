package faang.school.achievement.listener.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.CommentEvent;
import faang.school.achievement.handler.EventHandler;
import faang.school.achievement.listener.AbstractEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class CommentEventListener extends AbstractEventListener<CommentEvent> {

    @Value("${spring.data.redis.channels.comment-channel}")
    private String commentChannel;
    private final EventHandler<CommentEvent> angryCommenterAchievementHandler;

    public CommentEventListener(
            ObjectMapper objectMapper,
            RedisMessageListenerContainer container,
            EventHandler<CommentEvent> angryCommenterAchievementHandler) {
        super(objectMapper, container);
        this.angryCommenterAchievementHandler = angryCommenterAchievementHandler;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, CommentEvent.class, angryCommenterAchievementHandler::handle);
    }

    @Override
    protected String getTopicName() {
        return commentChannel;
    }
}