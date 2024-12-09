package faang.school.achievement.publisher.profile_pic;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.achievement.event.ProfilePicEvent;
import faang.school.achievement.publisher.AbstractEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProfilePicEventPublisher extends AbstractEventPublisher<ProfilePicEvent> {

    @Value("${spring.data.redis.channel.profile_pic}")
    private String topicProfilePic;


    public ProfilePicEventPublisher(RedisTemplate<String, Object> redisTemplate,
                                    ObjectMapper objectMapper) {
        super(redisTemplate, objectMapper);
    }

    public void publish(ProfilePicEvent event) {
        handleEvent(event, topicProfilePic);
    }

}
