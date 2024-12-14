package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final AchievementRepository achievementRepository;

    @Cacheable(value = "achievements")
    public Achievement getAchievement(String title) {
        return achievementRepository.findByTitle(title);
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    @CachePut(value="achievementProgresses")
    @Transactional
    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
    }

    @Cacheable(value = "achievementProgresses")
    public AchievementProgress getProgress(long userId, long achievementId) {
       return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow();
    }

    @CachePut(value = "achievementProgresses")
    public AchievementProgress saveProgress(AchievementProgress achievementProgress) {
        return achievementProgressRepository.save(achievementProgress);
    }

    @CachePut(value="achievements")
    @Transactional
    public void giveAchievement(long userId, Achievement achievement) {
        UserAchievement userAchievement = UserAchievement
                .builder()
                .userId(userId)
                .achievement(achievement)
                .createdAt(LocalDateTime.now())
                .build();
        userAchievementRepository.save(userAchievement);
    }
}
