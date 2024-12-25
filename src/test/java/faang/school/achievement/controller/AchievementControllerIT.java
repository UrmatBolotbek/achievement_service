package faang.school.achievement.controller;

import com.redis.testcontainers.RedisContainer;
import faang.school.achievement.model.Achievement;
import faang.school.achievement.model.UserAchievement;
import faang.school.achievement.repository.AchievementRepository;
import faang.school.achievement.repository.UserAchievementRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class AchievementControllerIT {

    @Autowired
    protected MockMvc mockMvc;

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
        initTable();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static void initTable() {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAchievementsByIdSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/achievements/" + 1).header("x-user-id", 11))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("COLLECTOR"));
    }

    @Test
    void testGetAchievementsByUserIdWithException() throws Exception {
        mockMvc.perform(get("/api/v1/achievements/user").header("x-user-id", 11))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAchievementsInProgressByUserIdException() throws Exception {
        mockMvc.perform(get("/api/v1/achievements/user/in_progress").header("x-user-id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAchievementsSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/achievements/filters?title=COLLECTOR")
                        .header("x-user-id", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("COLLECTOR"));
    }

}
