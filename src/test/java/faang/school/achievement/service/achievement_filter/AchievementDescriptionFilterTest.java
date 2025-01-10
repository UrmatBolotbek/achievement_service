package faang.school.achievement.service.achievement_filter;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementDescriptionFilterTest {

    private AchievementRequestFilterDto requestDto;
    private AchievementDescriptionFilter requestDescriptionFilter;
    private Stream<Achievement> achievementStream;

    @BeforeEach
    public void initData() {
        requestDto = new AchievementRequestFilterDto();
        requestDto.setDescription("описание");
        requestDescriptionFilter = new AchievementDescriptionFilter();
        achievementStream = Stream.of(
                Achievement.builder().description("описание 1").build(),
                Achievement.builder().description("описание 2").build(),
                Achievement.builder().description("ЗА КОРОЛЯ!!!").build()
        );
    }

    @Test
    public void testApply() {
        List<Achievement> achievements = requestDescriptionFilter
                .apply(achievementStream, requestDto)
                .stream()
                .toList();
        assertEquals(2, achievements.size());
        achievements.forEach(achievement ->
                assertTrue(achievement.getDescription().contains(requestDto.getDescription())));
    }

    @Test
    public void testIsApplicable() {
        assertTrue(requestDescriptionFilter.isApplicable(requestDto));
        assertFalse(requestDescriptionFilter.isApplicable(new AchievementRequestFilterDto()));
    }

}
