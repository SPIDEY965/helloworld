package com.project.helloworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;


@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());  // 실행 대기중인 Thread 개수
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors()*2); // 동시에 동작하는 최대 Thread 개수
        executor.setQueueCapacity(500); // CorePool이 초과 될 때 n개만큼 Queue에 저장했다가 꺼내서 실행함
        executor.setThreadNamePrefix("async-"); // Spring에서 생성하는 Thread이름의 접두사
        executor.initialize();
        return executor;
    }
}
