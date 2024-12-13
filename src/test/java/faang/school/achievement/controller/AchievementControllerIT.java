package faang.school.achievement.controller;

import com.redis.testcontainers.RedisContainer;
import faang.school.achievement.config.context.UserContext;
import faang.school.achievement.dto.AchievementRequestFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootTest
@Testcontainers
public class AchievementControllerIT {

    @Autowired
    private AchievementController achievementController;

    @Autowired
    private UserContext userContext;

    @Container
    public static PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:13.6");

    @Container
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis/redis-stack:latest"));

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379));
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAchievements() {
        AchievementRequestFilterDto achievementDto = new AchievementRequestFilterDto();
        achievementDto.setTitle("Title");
        achievementController.getAchievements(achievementDto);
    }

    @Test
    void testGetAchievementsByUserId() {
        userContext.setUserId(1L);
        achievementController.getAchievementsByUserId();
    }

    @Test
    void testGetAchievementById() {
        achievementController.getAchievementById(1L);
    }

    @Test
    void testGetAchievementsInProgressByUserId() {
        userContext.setUserId(1L);
        achievementController.getAchievementsInProgressByUserId();
    }

}
