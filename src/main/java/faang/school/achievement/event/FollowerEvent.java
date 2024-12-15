package faang.school.achievement.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class FollowerEvent {
    private long followerId;
    private long followeeId;
    private LocalDateTime receivedAt;
}