package faang.school.achievement.service;

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
    @Async("cashThreadPool")
    public void initCache() {
        log.info("Starting cache initialization");
        List<AchievementDto> allAchievementDtos = achievementRepository.findAll().stream()
                .map(achievementMapper::toDto)
                .toList();

        if (allAchievementDtos.isEmpty()) {
            log.info("No achievements found in database. Cache is empty.");
        } else {
            for (AchievementDto achievementDto : allAchievementDtos) {
                achievementRedisTemplate.opsForValue().set(buildKey(achievementDto.getTitle()), achievementDto);
            }
            log.info("Cache initialization completed. Loaded {} achievements.", allAchievementDtos.size());
        }
    }

    public AchievementDto get(String title) {
        return achievementRedisTemplate.opsForValue().get(buildKey(title));
    }

    private String buildKey(String title) {
        return String.format(KEY_FORMAT, PREFIX, title);
    }
}
