package faang.school.achievement.config.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    public RedisTemplate<String, Achievement> achievementRedisTemplate(JedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        JavaType javaType = TypeFactory.defaultInstance().constructType(Achievement.class);

        return getStringAchievementRedisTemplate(connectionFactory, objectMapper, javaType);
    }

    private static RedisTemplate<String, Achievement> getStringAchievementRedisTemplate(
            JedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper,
            JavaType javaType) {
        Jackson2JsonRedisSerializer<Achievement> achievementRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, javaType);

        RedisTemplate<String, Achievement> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(achievementRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(achievementRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }
}