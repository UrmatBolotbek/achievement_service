package faang.school.achievement.service.achievement_filter;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementRarityFilterTest {

    private AchievementRequestFilterDto requestDto;
    private AchievementRarityFilter achievementRarityFilter;
    private Stream<Achievement> achievementStream;

    @BeforeEach
    public void initData() {
        requestDto = new AchievementRequestFilterDto();
        requestDto.setRarity(Rarity.LEGENDARY);
        achievementRarityFilter = new AchievementRarityFilter();
        achievementStream = Stream.of(
                Achievement.builder().rarity(Rarity.LEGENDARY).build(),
                Achievement.builder().rarity(Rarity.LEGENDARY).build(),
                Achievement.builder().rarity(Rarity.COMMON).build()
        );
    }

    @Test
    public void testApply() {
        List<Achievement> achievements = achievementRarityFilter
                .apply(achievementStream, requestDto)
                .stream()
                .toList();
        assertEquals(2, achievements.size());
        achievements.forEach(achievement ->
                assertSame(achievement.getRarity(), requestDto.getRarity()));
    }

    @Test
    public void testIsApplicable() {
        assertTrue(achievementRarityFilter.isApplicable(requestDto));
        assertFalse(achievementRarityFilter.isApplicable(new AchievementRequestFilterDto()));
    }
}
