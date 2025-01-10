package faang.school.achievement.service.achievement_filter;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.model.Achievement;

import java.util.List;
import java.util.stream.Stream;

public interface AchievementFilter {

    boolean isApplicable(AchievementRequestFilterDto filters);

    List<Achievement> apply(Stream<Achievement> achievementStream, AchievementRequestFilterDto filters);
}
