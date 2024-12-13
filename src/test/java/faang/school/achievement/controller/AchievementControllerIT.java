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
import java.sql.SQLException;
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
        try (Connection conn = DriverManager.getConnection(
                POSTGRESQL_CONTAINER.getJdbcUrl(),
                POSTGRESQL_CONTAINER.getUsername(),
                POSTGRESQL_CONTAINER.getPassword());
             Statement stmt = conn.createStatement()) {
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE," +
                            "username varchar(64) UNIQUE NOT NULL," +
                            "password varchar(128) NOT NULL," +
                            "email varchar(64) UNIQUE NOT NULL," +
                            "phone varchar(32) UNIQUE," +
                            "about_me varchar(4096)," +
                            "active boolean DEFAULT true NOT NULL," +
                            "city varchar(64)," +
                            "country_id bigint NOT NULL," +
                            "experience int," +
                            "created_at timestamptz DEFAULT current_timestamp," +
                            "updated_at timestamptz DEFAULT current_timestamp" +
                            ");"
            );
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
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
