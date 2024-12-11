package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementResponseDto {

    private long id;
    private String title;
    private String description;
    private Rarity rarity;
    private long points;

}
