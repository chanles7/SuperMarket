package com.mail.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MyThreadConfig {


    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties property) {
        return new ThreadPoolExecutor(property.getCoreSize(), property.getMaxSize()
                , property.getKeepAliveTime(), TimeUnit.SECONDS, new LinkedBlockingDeque<>(50000)
                , Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

}
