package com.example.demo.schedule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yxl
 * @date 2023/2/27 下午4:10
 */

@Configuration
@EnableAsync
public class DataBean {

    @Bean
    public AtomicBoolean updateSiteFlag() {
        return new AtomicBoolean(false);
    }

    @Bean
    List<String> siteDate() {
        return new ArrayList<>();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        return taskScheduler;
    }

    @Bean
    public AtomicInteger integer() {
        return new AtomicInteger(0);
    }

    @Bean
    public String[] wai_gua() {
        return new String[3];
    }

    @Bean
    public Timer getTimer() {
        return new Timer();
    }
}
