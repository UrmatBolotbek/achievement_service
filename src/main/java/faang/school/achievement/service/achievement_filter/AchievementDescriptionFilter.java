package faang.school.achievement.service.achievement_filter;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.model.Achievement;

import java.util.List;
import java.util.stream.Stream;

public class AchievementDescriptionFilter implements  AchievementFilter {

    @Override
    public boolean isApplicable(AchievementRequestFilterDto filters) {
        return filters.getDescription() != null;
    }

    @Override
    public List<Achievement> apply(Stream<Achievement> achievementStream, AchievementRequestFilterDto filters) {
        return achievementStream.filter(achievement -> achievement.getDescription().contains(filters.getDescription()))
                .toList();
    }
}
