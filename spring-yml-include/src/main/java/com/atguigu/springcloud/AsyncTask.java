package com.atguigu.springcloud;

import com.atguigu.springcloud.config.TaskExecutePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncTask {

    @Async(TaskExecutePool.MY_TASK_ASYNC_POOL)  // myTaskAsynPool即配置线程池的方法名，此处如果不写自定义线程池的方法名，会使用默认的线程池
    public void doTask1(int i) {
        log.info("Task" + i + " started.");
    }

    @Async
    public void doTask2(int i) {
        log.info("Task2-Native" + i + " started.");
    }

}
