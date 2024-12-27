package faang.school.achievement.dto;

import faang.school.achievement.model.Rarity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AchievementRequestFilterDto {

    private String title;
    private String description;
    private Rarity rarity;

}
