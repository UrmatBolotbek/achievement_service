package faang.school.achievement.model.dto;

import faang.school.achievement.model.Rarity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementDto {
    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private List<Long> acceptedUserIds;
    private long points;
}
