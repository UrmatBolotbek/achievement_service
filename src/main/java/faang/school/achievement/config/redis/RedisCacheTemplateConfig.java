package faang.school.achievement.config.redis;

import faang.school.achievement.model.Achievement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisCacheTemplateConfig {

    @Bean
    public Jackson2JsonRedisSerializer<Achievement> achievementRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(Achievement.class);
    }

    @Bean
    public RedisTemplate<String, Achievement> achievementRedisTemplate(
            JedisConnectionFactory connectionFactory,
            Jackson2JsonRedisSerializer<Achievement> achievementRedisSerializer) {

        RedisTemplate<String, Achievement> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(achievementRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
