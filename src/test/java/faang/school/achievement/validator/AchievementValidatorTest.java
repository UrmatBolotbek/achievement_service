package faang.school.achievement.validator;

import faang.school.achievement.client.UserServiceClient;
import faang.school.achievement.dto.UserDto;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private UserServiceClient userServiceClient;
    @Mock
    private AchievementRepository achievementRepository;

    @Test
    void testUserValidationWithException() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, ()-> achievementValidator.userValidation(USER_ID));
    }

    @Test
    void testUserValidationSuccess() {
        when(userServiceClient.getUser(USER_ID)).thenReturn(new UserDto());
        assertDoesNotThrow(()-> achievementValidator.userValidation(USER_ID));
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
