package faang.school.achievement.validator;

import faang.school.achievement.client.UserServiceClient;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AchievementValidator {

    private final UserServiceClient userServiceClient;
    private final AchievementRepository achievementRepository;

    public void userValidation(long userId) {
        if (userServiceClient.getUser(userId) == null) {
            log.warn("User with id {} not found", userId);
            throw new EntityNotFoundException("User with id " + userId + " not found");
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
