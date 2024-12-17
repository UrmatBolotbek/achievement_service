package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfilePicEvent {
    private long userId;
    private String picUrl;

}
