package faang.school.achievement.config.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class AsyncConfiguration {
    @Value("${async.pool-size}")
    private int poolSize;
    @Value("${async.max-pool-size}")
    private int maxPoolSize;
    @Value("${async.keep-alive-time}")
    private long keepAliveTime;

    @Bean(name = "fixedThreadPool")
    public ExecutorService fixedThreadPoolTaskExecutor() {
        return new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }
}
