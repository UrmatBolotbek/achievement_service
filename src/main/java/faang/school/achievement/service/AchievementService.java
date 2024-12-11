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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    public List<AchievementResponseDto> getAchievements(AchievementRequestFilterDto requestFilterDto) {
        Stream<Achievement> achievements = StreamSupport.stream(achievementRepository.findAll().spliterator(),
                false).toList().stream();
        filters.stream()
                .filter(filter -> filter.isApplicable(requestFilterDto))
                .forEach(filter -> filter.apply(achievements, requestFilterDto));
        log.info("Getting a list of achievement after filtering");
        return mapper.toResponseDtoList(achievements.toList());
    }

    public List<AchievementResponseDto> getAchievementsByUserId(long userId) {
        achievementValidator.userValidation(userId);
        List<UserAchievement> achievementsOfUser = userAchievementRepository.findByUserId(userId);
        List<Achievement> achievements = achievementsOfUser.stream().map(UserAchievement::getAchievement)
                .map(achievement -> achievementValidator.validateAchievement(achievement.getId()))
                .toList();
        log.info("Getting a list of achievement for user id {}", userId);
        return mapper.toResponseDtoList(achievements);
    }

    public AchievementResponseDto getAchievementById(long achievementId) {
        Achievement achievement = achievementValidator.validateAchievement(achievementId);
        if (achievement == null) {
            throw new EntityNotFoundException("Achievement with achievementId " + achievementId + " not found");
        }
        return mapper.achievementToResponseDto(achievement);
    }

    public List<AchievementResponseDto> getAchievementsInProgressByUserId(long userId) {
        achievementValidator.userValidation(userId);
        List<AchievementProgress> achievementProgresses = achievementProgressRepository.findByUserId(userId);
        List<Achievement> achievements = achievementProgresses.stream().map(AchievementProgress::getAchievement)
                .map(achievement -> achievementValidator.validateAchievement(achievement.getId()))
                .toList();
        log.info("Getting a list of achievement in progress for user id {}", userId);
        return mapper.toResponseDtoList(achievements);
    }

    public Achievement getAchievement(String achievementName) {
        return achievementRepository.findByTitle(achievementName)
                .orElseThrow(() -> new EntityNotFoundException("No achievement with name " + achievementName + " exists"));
    }

    public boolean hasAchievement(long userId, long achievementId) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievementId);
    }

    public void createProgressIfNecessary(long userId, long achievementId) {
        achievementProgressRepository.createProgressIfNecessary(userId, achievementId);
        log.info("Created progress for user {} achievement {}", userId, achievementId);
    }

    public long getProgress(long userId, long achievementId) {
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
        eventPublisher.publish(publishEvent);
        log.info("Achievement: {} for authorId: {} publish successfully", achievement, userId);
    }

}
