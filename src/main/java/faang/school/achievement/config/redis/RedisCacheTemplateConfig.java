package faang.school.achievement.config.redis;

import faang.school.achievement.model.dto.AchievementDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCacheTemplateConfig {

    @Bean
    public Jackson2JsonRedisSerializer<AchievementDto> achievementRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(AchievementDto.class);
    }

    @Bean
    public RedisTemplate<String, AchievementDto> achievementRedisTemplate(
            JedisConnectionFactory connectionFactory,
            Jackson2JsonRedisSerializer<AchievementDto> achievementRedisSerializer) {

        RedisTemplate<String, AchievementDto> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(achievementRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
