package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.transaction.Transactional;
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
    private final RedisTemplate<String, Achievement> achievementRedisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    @Async("fixedThreadPool")
    @Transactional
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

    public Achievement get(String title) {
        return achievementRedisTemplate.opsForValue().get(buildKey(title));
    }

    public void putInCache(Achievement achievement) {
        achievementRedisTemplate.opsForValue().set(buildKey(achievement.getTitle()), achievement);
    }

    private String buildKey(String title) {
        return String.format(KEY_FORMAT, PREFIX, title);
    }
}
