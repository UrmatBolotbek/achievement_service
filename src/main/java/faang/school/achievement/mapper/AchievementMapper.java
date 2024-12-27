package faang.school.achievement.mapper;

import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.model.Achievement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementResponseDto achievementToResponseDto(Achievement achievement);

    List<AchievementResponseDto> toResponseDtoList(List<Achievement> achievements);


}
