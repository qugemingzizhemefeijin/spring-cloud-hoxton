package com.atguigu.springcloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 原生(Spring)异步任务线程池装配类
 * <br><br>
 * Async异步方法默认使用Spring创建ThreadPoolTaskExecutor。
 * <br><br>
 * 默认核心线程数：8，最大线程数：Integet.MAX_VALUE，队列使用LinkedBlockingQueue，容量是：Integer.MAX_VALUE，空闲线程保留时间：60s，线程池拒绝策略：AbortPolicy。
 * <br><br>
 *
 * @see TaskExecutionProperties
 * @see TaskExecutionAutoConfiguration
 *
 */
@Slf4j
@Configuration
public class NativeAsyncTaskExecutePool implements AsyncConfigurer {

    //注入配置类
    @Autowired
    TaskThreadPoolConfig config;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(config.getCorePoolSize());
        //最大线程数
        executor.setMaxPoolSize(config.getMaxPoolSize());
        //队列容量
        executor.setQueueCapacity(config.getQueueCapacity());
        //活跃时间
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        //线程名字前缀
        executor.setThreadNamePrefix("DefaultExecutor-");
        // setRejectedExecutionHandler：当pool已经达到max size的时候，如何处理新任务
        // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * 异步任务中异常处理
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("==========================" + throwable.getMessage() + "=======================", throwable);
            log.error("exception method:" + method.getName());
        };
    }

}
