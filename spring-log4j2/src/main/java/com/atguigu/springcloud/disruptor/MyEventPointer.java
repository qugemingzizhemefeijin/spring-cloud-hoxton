package com.atguigu.springcloud.disruptor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

@Slf4j
public class MyEventPointer implements Consumer<MyEventModel> {

    private final MultiProducerService multiProducerService;

    private final CountDownLatch countDownLatch;

    private final int countDownLatchGate;

    public MyEventPointer(int countDownLatchGate, MultiProducerService multiProducerService, CountDownLatch countDownLatch) {
        this.countDownLatchGate = countDownLatchGate;
        this.multiProducerService = multiProducerService;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void accept(MyEventModel data) {
        synchronized (this) {
            // 消费一个
            int consumerCount = multiProducerService.consumerCounter();

            /*
             * 这是辅助测试用的，
             * 测试的时候，完成事件发布后，测试主线程就用这个countDownLatch开始等待，
             * 在消费到指定的数量(countDownLatchGate)后，消费线程执行countDownLatch的countDown方法，
             * 这样测试主线程就可以结束等待了
             */
            if (null != countDownLatch && consumerCount >= countDownLatchGate) {
                // 关闭计数器
                multiProducerService.turnOff();
                countDownLatch.countDown();
            }
        }
    }

}
