package faang.school.achievement.validator;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.AchievementProgress;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AchievementValidatorTest {

    private final long USER_ID = 19L;
    private final long ACHIEVEMENT_ID = 25L;

    @InjectMocks
    private AchievementValidator achievementValidator;

    @Mock
    private AchievementRepository achievementRepository;

    @Test
    void testValidateListAchievementByUserWithException() {
        List<UserAchievement> userAchievements = new ArrayList<>();
        assertThrows(EntityNotFoundException.class, ()-> achievementValidator
                .validateListAchievementByUser(userAchievements, USER_ID));
    }

    @Test
    void testValidateListAchievementByUserSuccess() {
        List<UserAchievement> userAchievements = List.of(new UserAchievement());
        assertDoesNotThrow(()-> achievementValidator.validateListAchievementByUser(userAchievements, USER_ID));
    }

    @Test
    void testValidateListAchievementInProgressByUserWithException() {
        List<AchievementProgress> userAchievements = new ArrayList<>();
        assertThrows(EntityNotFoundException.class, ()-> achievementValidator
                .validateListAchievementInProgressByUser(userAchievements, USER_ID));
    }

    @Test
    void testValidateListAchievementInProgressByUserSuccess() {
        List<AchievementProgress> userAchievements = List.of(new AchievementProgress());
        assertDoesNotThrow(()-> achievementValidator.validateListAchievementInProgressByUser(userAchievements, USER_ID));
    }

    @Test
    void testValidateAchievementWithException() {
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> achievementValidator.validateAchievement(ACHIEVEMENT_ID));
    }

    @Test
    void testValidateAchievementSuccess() {
        when(achievementRepository.findById(ACHIEVEMENT_ID)).thenReturn(Optional.of(new Achievement()));
        assertDoesNotThrow(()-> achievementValidator.validateAchievement(ACHIEVEMENT_ID));
    }

}
