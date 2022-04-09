package com.atguigu.springcloud.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {

    // initialCapacity：初始缓存空大小
    // maximumSize：缓存的最大数量，设置这个值可以避免出现内存溢出
    // expireAfterWrite：指定缓存的过期时间，是最后一次写操作后的一个时间
    // 缓存的过期策略也可以通过expireAfterAccess或refreshAfterWrite指定
    @Bean
    public Cache<String,Object> caffeineCache(){
        return Caffeine.newBuilder()
                .initialCapacity(128)//初始大小
                .maximumSize(1024)//最大数量
                .expireAfterWrite(60, TimeUnit.SECONDS)//过期时间
                .build();
    }

}
