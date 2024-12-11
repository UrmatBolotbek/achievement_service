package faang.school.achievement.service;

import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.dto.AchievementDto;
import faang.school.achievement.model.mapper.AchievementMapper;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementCacheTest {

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @Mock
    private RedisTemplate<String, AchievementDto> redisTemplate;

    @Mock
    private ValueOperations<String, AchievementDto> valueOps;

    @InjectMocks
    private AchievementCache achievementCache;

    private Achievement ach1;
    private Achievement ach2;
    private AchievementDto dto1;
    private AchievementDto dto2;

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

        dto1 = AchievementDto.builder()
                .id(1L)
                .title("Title1")
                .build();

        dto2 = AchievementDto.builder()
                .id(2L)
                .title("Title2")
                .build();

        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void initCacheWithAchievements() {
        when(achievementRepository.findAll()).thenReturn(Arrays.asList(ach1, ach2));
        when(achievementMapper.toDto(ach1)).thenReturn(dto1);
        when(achievementMapper.toDto(ach2)).thenReturn(dto2);

        invokeInitCacheMethod();

        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AchievementDto> dtoCaptor = ArgumentCaptor.forClass(AchievementDto.class);

        verify(valueOps, times(2)).set(keyCaptor.capture(), dtoCaptor.capture());

        List<String> allKeys = keyCaptor.getAllValues();
        List<AchievementDto> allDtos = dtoCaptor.getAllValues();

        assertTrue(allKeys.contains("achievement:Title1"));
        assertTrue(allKeys.contains("achievement:Title2"));
        assertTrue(allDtos.contains(dto1));
        assertTrue(allDtos.contains(dto2));
    }

    @Test
    void initCacheWithNoAchievements() {
        when(achievementRepository.findAll()).thenReturn(List.of());

        invokeInitCacheMethod();

        verify(valueOps, never()).set(anyString(), any());
    }

    @Test
    void getAchievement() {
        AchievementDto expectedDto = AchievementDto.builder().title("Title1").id(1L).build();
        when(valueOps.get("achievement:Title1")).thenReturn(expectedDto);

        AchievementDto actual = achievementCache.get("Title1");
        assertEquals(expectedDto, actual);
    }

    private void invokeInitCacheMethod() {
        Method initCacheMethod = ReflectionUtils.findMethod(AchievementCache.class, "initCache");
        assertNotNull(initCacheMethod);
        ReflectionUtils.invokeMethod(initCacheMethod, achievementCache);
    }
}
