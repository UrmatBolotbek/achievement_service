# Achievement Service

**Achievement Service** is a microservice that manages user achievements as part of our integrated application ecosystem. It handles the issuance, publication, retrieval, and caching of achievements, ensuring that other services (such as Notification Service) can be promptly informed about important events.

---

## Overview

- **Event Publication:**  
  When an achievement is granted to a user, the service publishes an event to a Redis topic (e.g., `achievement_channel`). This allows other microservices to react—such as sending notifications—without directly coupling to the achievement logic.

- **Comprehensive Achievement Management:**  
  The service provides RESTful APIs (through its controllers) to retrieve:
  - All available achievements (with filtering by name, description, and rarity).
  - Achievements for a specific user.
  - Details for a single achievement.
  - Unreceived achievements along with the user’s progress.
  
- **In-Memory Caching:**  
  Since the set of achievements is relatively static and small, the service caches all achievements in memory for fast retrieval. The cache is populated at startup using data from the database, reducing unnecessary load on persistent storage.

---

## Key Features

- **Redis Pub/Sub Integration:**  
  - **AchievementPublisher:** Publishes achievement events as JSON messages to the Redis topic `achievement_channel` using a configured `RedisTemplate` and `ObjectMapper`.
  - **Configuration:** Beans for Redis (via `RedisConfig` and `AchievementRedisConfig`) are set up to support pub/sub messaging.

- **RESTful Achievement API:**  
  - Provides endpoints for fetching achievements by various criteria (e.g., filtering by name, description, rarity) and for retrieving a user’s achievements and progress.
  - Uses robust validation and type conversion (e.g., converting strings or numbers to enum types for event and interval parameters).

- **Efficient In-Memory Cache:**  
  - **AchievementCache:** Loads all achievements from the database on startup (using `@PostConstruct` in conjunction with `AchievementRepository`) into a data structure keyed by achievement title for fast lookup.
  - This caching mechanism improves performance by avoiding frequent database queries for static achievement data.

---

## Architecture and Technologies

This service follows the standard Spring Boot project template and integrates seamlessly with the rest of our microservices architecture.

### Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot) – Main framework for building the application.
- [PostgreSQL](https://www.postgresql.org/) – Primary relational database.
- [Redis](https://redis.io/) – Used for caching and pub/sub messaging.
- [Testcontainers](https://testcontainers.com/) – For isolated integration testing with a real database.
- [Liquibase](https://www.liquibase.org/) – Manages database schema migrations.
- [Gradle](https://gradle.org/) – Build system.
- [Lombok](https://projectlombok.org/) – Simplifies POJO code.
- [MapStruct](https://mapstruct.org/) – For mapping between POJO classes.

### Database & Infrastructure

- **Database and Redis Instances:**  
  Both PostgreSQL and Redis are managed as part of our infrastructure (see the [infra](../infra) service).
- **Liquibase Migrations:**  
  Schema changes are automatically applied at startup.
- **Testing:**  
  Integration tests use Testcontainers to spin up isolated instances of PostgreSQL and Redis.

---
