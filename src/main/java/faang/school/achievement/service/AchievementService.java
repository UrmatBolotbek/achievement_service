package faang.school.achievement.service;

import faang.school.achievement.dto.AchievementRequestFilterDto;
import faang.school.achievement.dto.AchievementResponseDto;
import faang.school.achievement.event.PublishEvent;
import faang.school.achievement.mapper.AchievementMapper;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.publisher.EventPublisher;
import faang.school.achievement.repository.AchievementProgressRepository;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import faang.school.achievement.service.achievement_filter.AchievementFilter;
import faang.school.achievement.validator.AchievementValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AchievementService {
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementProgressRepository achievementProgressRepository;
    private final EventPublisher eventPublisher;
    private final AchievementRepository achievementRepository;
    private final List<AchievementFilter> filters;
    private final AchievementMapper mapper;
    private final AchievementValidator achievementValidator;

    @Cacheable(value = "achievement")
    public Achievement getByTitle(String title) {
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
        return achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(
                () -> new EntityNotFoundException("Progress for achievements " + achievementId + " and for the user " + userId + " was not found")
        );
    }

    @CachePut(value =  "progress")
    public AchievementProgress incrementProgress(AchievementProgress achievementProgress) {
        achievementProgress.increment();
        log.info("Achievement progress for authorId: {} has incremented successfully", achievementProgress.getUserId());
        achievementProgressRepository.save(achievementProgress);

        return achievementProgress;
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
        eventPublisher.publish(publishEvent);
        log.info("Achievement: {} for authorId: {} publish successfully", achievement, userId);
    }

    public List<AchievementResponseDto> getAchievementsWithFilters(AchievementRequestFilterDto requestFilterDto) {
        Stream<Achievement> achievements = achievementRepository.findAll().stream().toList().stream();
        filters.stream()
                .filter(filter -> filter.isApplicable(requestFilterDto))
                .forEach(filter -> filter.apply(achievements, requestFilterDto));
        log.info("Getting a list of achievement after filtering");
        return mapper.toResponseDtoList(achievements.toList());
    }

    public List<AchievementResponseDto> getAchievementsByUserId(long userId) {
        List<UserAchievement> achievementsOfUser = userAchievementRepository.findByUserId(userId);
        achievementValidator.validateListAchievementByUser(achievementsOfUser, userId);
        List<Achievement> achievements = achievementsOfUser.stream().map(UserAchievement::getAchievement).toList();
        log.info("Getting a list of achievement for user id {}", userId);
        return mapper.toResponseDtoList(achievements);
    }

    public AchievementResponseDto getAchievementById(long achievementId) {
        Achievement achievement = achievementValidator.validateAchievement(achievementId);
        return mapper.achievementToResponseDto(achievement);
    }

    public List<AchievementResponseDto> getAchievementsInProgressByUserId(long userId) {
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);
        achievementValidator.validateListAchievementInProgressByUser(achievementProgresses, userId);
        List<Achievement> achievements = achievementProgresses.stream().map(AchievementProgress::getAchievement).toList();
        log.info("Getting a list of achievement in progress for user id {}", userId);
        return mapper.toResponseDtoList(achievements);
    }
}
