package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.dto.AchievementDto;
import faang.school.achievement.model.mapper.AchievementMapper;
import faang.school.achievement.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AchievementCache {
    private static final String KEY_FORMAT = "%s:%s";
    private static final String PREFIX = "achievement";

    private final AchievementRepository achievementRepository;
    private final RedisTemplate<String, AchievementDto> achievementRedisTemplate;
    private final AchievementMapper achievementMapper;

    @EventListener(ApplicationReadyEvent.class)
    @Async("fixedThreadPool")
    public void initCache() {
        log.info("Starting cache initialization");

        List<Achievement> allAchievements = achievementRepository.findAll();
        if (allAchievements.isEmpty()) {
            log.info("No achievements found in database. Cache is empty.");
            return;
        }

        allAchievements.forEach(this::putInCache);
        log.info("Cache initialization completed. Loaded {} achievements.", allAchievements.size());
    }

    public AchievementDto get(String title) {
        return achievementRedisTemplate.opsForValue().get(buildKey(title));
    }

    public void putInCache(Achievement achievement) {
        AchievementDto dto = achievementMapper.toDto(achievement);
        achievementRedisTemplate.opsForValue().set(buildKey(dto.getTitle()), dto);
    }

    private String buildKey(String title) {
        return String.format(KEY_FORMAT, PREFIX, title);
    }
}
