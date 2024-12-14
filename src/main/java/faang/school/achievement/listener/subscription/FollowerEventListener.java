package faang.school.achievement.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.FollowerEvent;
import faang.school.achievement.handler.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowerEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final List<EventHandler<FollowerEvent>> handlers;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FollowerEvent followerEvent =  objectMapper.readValue(message.getBody(), FollowerEvent.class);
            handlers.forEach(handler -> handler.handle(followerEvent));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
