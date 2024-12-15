package faang.school.achievement.service;

import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.AchievementPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementPublisher achievementPublisher;
    private final AchievementRepository achievementRepository;

    @Cacheable(value = "achievement")
    public Achievement getAchievement(String title) {
        return achievementRepository.findByTitle(title).orElseThrow(() -> new IllegalArgumentException("Achievement not found"));
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        log.info("Created progress for user {} achievement {}", userId, achievementId);
    }

    @Cacheable(value = "progress")
    public AchievementProgress getProgress(long userId, long achievementId) {
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow();
    }

    @Transactional
    public long getCurrentPointsOfProgress(long userId, long achievementId) {
        AchievementProgress achievementProgress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId)
                .orElseThrow(() -> new EntityNotFoundException("\n" +
                        "Progress for achievements " + achievementId + " and for the user " + userId + " was not found"));
        achievementProgress.increment();
        log.info("Achievement progress for authorId: {} has incremented successfully", userId);
        achievementProgressRepository.save(achievementProgress);
        return achievementProgress.getCurrentPoints();
    }

    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievement(achievement)
                .build();
        userAchievementRepository.save(userAchievement);
        log.info("Achievement: {} for authorId: {} saved successfully", achievement, userId);
        PublishEvent publishEvent = PublishEvent.builder()
                .userId(userId)
                .achievementId(achievement.getId())
                .achievementName(achievement.getTitle())
                .achievementDescription(achievement.getDescription())
                .build();
        achievementPublisher.publish(publishEvent);
        log.info("Achievement: {} for authorId: {} publish successfully", achievement, userId);
    }
}
