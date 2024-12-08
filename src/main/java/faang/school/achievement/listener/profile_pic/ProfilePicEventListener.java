package faang.school.achievement.listener.profile_pic;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.listener.AbstractEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProfilePicEventListener extends AbstractEventListener<ProfilePicEvent> implements MessageListener {


    public ProfilePicEventListener(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfilePicEvent.class, event -> {

        });
    }
}
