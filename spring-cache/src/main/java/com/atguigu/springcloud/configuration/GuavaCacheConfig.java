package com.atguigu.springcloud.configuration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaCacheConfig {

    @Bean
    public Cache<String, Object> guavaCache() {
        return CacheBuilder.newBuilder()
                .initialCapacity(128)//初始大小
                .maximumSize(1024)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

}
