package faang.school.achievement.model.mapper;

import faang.school.achievement.model.dto.AchievementDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {

    @Mapping(target = "acceptedUserIds", source = "userAchievements", qualifiedByName = "mapUserAchievementsToIds")
    AchievementDto toDto(Achievement achievement);

    @Named("mapUserAchievementsToIds")
    default List<Long> mapUserAchievementsToIds(List<UserAchievement> userAchievements) {
        if (userAchievements == null) {
            return new ArrayList<>();
        }

        return userAchievements.stream()
                .map(UserAchievement::getUserId)
                .toList();
    }
}
