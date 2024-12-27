package faang.school.achievement.service.achievement_filter;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.model.Achievement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementTitleFilterTest {

    private AchievementRequestFilterDto requestDto;
    private AchievementTitleFilter achievementTitleFilter;
    private Stream<Achievement> achievementStream;

    @BeforeEach
    public void initData() {
        requestDto = new AchievementRequestFilterDto();
        requestDto.setTitle("заголовок");
        achievementTitleFilter = new AchievementTitleFilter();
        achievementStream = Stream.of(
                Achievement.builder().title("заголовок 1").build(),
                Achievement.builder().title("заголовок 2").build(),
                Achievement.builder().title("ЗА КОРОЛЯ!!!").build()
        );
    }

    @Test
    public void testApply() {
        List<Achievement> achievements = achievementTitleFilter
                .apply(achievementStream, requestDto)
                .stream()
                .toList();
        assertEquals(2, achievements.size());
        achievements.forEach(achievement ->
                assertTrue(achievement.getTitle().contains(requestDto.getTitle())));
    }

    @Test
    public void testIsApplicable() {
        assertTrue(achievementTitleFilter.isApplicable(requestDto));
        assertFalse(achievementTitleFilter.isApplicable(new AchievementRequestFilterDto()));
    }

}
