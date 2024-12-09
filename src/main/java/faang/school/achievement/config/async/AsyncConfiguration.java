package faang.school.achievement.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AsyncConfiguration {

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPoolTaskExecutor() {
        return new ThreadPoolExecutor(10,20,10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
