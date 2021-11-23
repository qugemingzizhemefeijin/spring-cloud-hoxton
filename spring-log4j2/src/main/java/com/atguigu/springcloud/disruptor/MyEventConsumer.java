package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 消息事件消费者
 */
@Slf4j
public class MyEventConsumer implements EventHandler<MyEventModel> {

    // 外部可以传入Consumer实现类，每处理一条消息的时候，consumer的accept方法就会被执行一次
    private final Consumer<MyEventModel> consumer;

    private final String name;

    private final AtomicInteger counter = new AtomicInteger();

    public MyEventConsumer(String name, Consumer<MyEventModel> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    @Override
    public void onEvent(MyEventModel event, long sequence, boolean endOfBatch) throws Exception {
        log.info("{} 处理了第{}条数据：{}, sequence [{}], endOfBatch [{}]", name, counter.incrementAndGet(), event.getData(),  sequence, endOfBatch);

        // 这里延时10ms，模拟消费事件的逻辑的耗时
        TimeUnit.MILLISECONDS.sleep(10);

        // 如果外部传入了consumer，就要执行一次accept方法
        if(consumer != null) {
            consumer.accept(event);
        }
    }

}
