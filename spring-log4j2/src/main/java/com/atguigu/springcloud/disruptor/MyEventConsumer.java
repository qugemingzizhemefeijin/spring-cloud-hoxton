package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息事件消费者
 */
@Slf4j
public class MyEventConsumer implements EventHandler<MyEventModel> {

    @Override
    public void onEvent(MyEventModel event, long sequence, boolean endOfBatch) throws Exception {
        log.info("我处理了一条数据：{}", event.getData());
    }

}
