package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementRequestFilterDto {

    private String title;
    private String description;
    private Rarity rarity;

}
