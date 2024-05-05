package com.tune_fun.v1.common.config;

import com.tune_fun.v1.common.async.MDCTaskDecorator;
import com.tune_fun.v1.common.config.annotation.OnlyDevelopmentConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@OnlyDevelopmentConfiguration
public class MailExecutorConfig implements AsyncConfigurer {

    private final int corePoolSize = 4;

    private final int maxPoolSize = 4;

    private final int queueCapacity = 10;

    private final String threadNamePrefix = "MailExecutor-";

    @Bean("emailAsync")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setTaskDecorator(new MDCTaskDecorator());
        executor.initialize();

        return executor;
    }
}
