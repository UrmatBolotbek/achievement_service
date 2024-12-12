package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.repository.AchievementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private RedisTemplate<String, Achievement> redisTemplate;

    @Mock
    private ValueOperations<String, Achievement> valueOps;

    @InjectMocks
    private AchievementCache achievementCache;

    private Achievement ach1;
    private Achievement ach2;

    @BeforeEach
    void setUp() {
        ach1 = Achievement.builder()
                .id(1L)
                .title("Title1")
                .build();

        ach2 = Achievement.builder()
                .id(2L)
                .title("Title2")
                .build();

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void initCacheWithAchievements() {
        when(achievementRepository.findAll()).thenReturn(Arrays.asList(ach1, ach2));

        invokeInitCacheMethod();

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Achievement> achCaptor = ArgumentCaptor.forClass(Achievement.class);

        verify(valueOps, times(2)).set(keyCaptor.capture(), achCaptor.capture());

        List<String> allKeys = keyCaptor.getAllValues();
        List<Achievement> allAchs = achCaptor.getAllValues();

        assertTrue(allKeys.contains("achievement:Title1"));
        assertTrue(allKeys.contains("achievement:Title2"));
        assertTrue(allAchs.contains(ach1));
        assertTrue(allAchs.contains(ach2));
    }

    @Test
    void initCacheWithNoAchievements() {
        when(achievementRepository.findAll()).thenReturn(List.of());

        invokeInitCacheMethod();

        verify(valueOps, never()).set(anyString(), any());
    }

    @Test
    void getAchievement() {
        Achievement expected = Achievement.builder().title("Title1").id(1L).build();
        when(valueOps.get("achievement:Title1")).thenReturn(expected);

        Achievement actual = achievementCache.get("Title1");
        assertEquals(expected, actual);
    }

    private void invokeInitCacheMethod() {
        Method initCacheMethod = ReflectionUtils.findMethod(AchievementCache.class, "initCache");
        assertNotNull(initCacheMethod);
        assertDoesNotThrow(() -> ReflectionUtils.invokeMethod(initCacheMethod, achievementCache));
    }
}
