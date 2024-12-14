package faang.school.achievement.validator;
 
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementValidator {

    private final AchievementRepository achievementRepository;

    public void validateListAchievementByUser(List<UserAchievement> achievementList, long userId) {
        if (achievementList == null || achievementList.isEmpty()) {
            log.warn("Achievement list is null or empty for user with id {}", userId);
            throw new EntityNotFoundException("Achievement list is null or empty for user with id " + userId);
        }
    }

    public void validateListAchievementInProgressByUser(List<AchievementProgress> achievementList, long userId) {
        if (achievementList == null || achievementList.isEmpty()) {
            log.warn("Achievement in progress list in is null or empty for user with id {}", userId);
            throw new EntityNotFoundException("Achievement in progress list is null or empty for user with id " + userId);
        }
    }

    public Achievement validateAchievement(long achievementId) {
        Optional<Achievement> achievementOptional = achievementRepository.findById(achievementId);
        if (achievementOptional.isEmpty()) {
            throw new EntityNotFoundException("Achievement with achievementId " + achievementId + " not found");
        }
        return achievementOptional.get();
    }

}
