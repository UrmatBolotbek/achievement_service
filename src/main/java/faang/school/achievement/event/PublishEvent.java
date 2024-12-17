package faang.school.achievement.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublishEvent {
    private long userId;
    private long achievementId;
    private String achievementName;
    private String achievementDescription;

}