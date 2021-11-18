package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Slf4j
public class MyWorkHandler implements WorkHandler<MyEventModel> {

    // 外部可以传入Consumer实现类，每处理一条消息的时候，consumer的accept方法就会被执行一次
    private Consumer<MyEventModel> consumer;

    private final int number;

    public MyWorkHandler(Consumer<MyEventModel> consumer, int number) {
        this.consumer = consumer;
        this.number = number;
    }

    @Override
    public void onEvent(MyEventModel event) throws Exception {
        log.info("number handler [{}] 处理了一条数据：{}", number, event.getData());

        // 这里延时10ms，模拟消费事件的逻辑的耗时
        TimeUnit.MILLISECONDS.sleep(10);

        // 如果外部传入了consumer，就要执行一次accept方法
        if(consumer != null) {
            consumer.accept(event);
        }
    }

}
