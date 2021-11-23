package com.atguigu.springcloud.log4j2.test;

import com.atguigu.springcloud.disruptor.MultiProducerService;
import com.atguigu.springcloud.disruptor.MyEventModel;
import com.atguigu.springcloud.disruptor.MyEventProducer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
public abstract class BaseMultiProducerServiceTest {

    public static final int EVENT_COUNT = 10;

    @Resource
    protected MultiProducerService multiProducerService;

    public void start(CountDownLatch countDownLatch, ChildrenOperator operator) throws Exception {
        Disruptor<MyEventModel> disruptor = multiProducerService.getDisruptor();
        // 第一个生产者
        MyEventProducer producer1 = new MyEventProducer(disruptor.getRingBuffer());
        // 第二个生产者
        MyEventProducer producer2 = new MyEventProducer(disruptor.getRingBuffer());

        operator.childOperator(disruptor);

        multiProducerService.start();

        // 启动一个线程，用第一个生产者生产事件
        new Thread(() -> {
            for (int i = 0; i < EVENT_COUNT; i++) {
                log.info("publishWithProducer1 {}", i);
                producer1.producerData(String.valueOf(i));
            }
        }).start();

        // 再启动一个线程，用第二个生产者生产事件
        new Thread(() -> {
            for (int i = 0; i < EVENT_COUNT; i++) {
                log.info("publishWithProducer2 {}", i);
                try {
                    producer2.producerData(String.valueOf(i));
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                }
            }
        }).start();


        // 当前线程开始等待，前面的service.setCountDown方法已经告诉过service，
        // 等消费到expectEventCount个消息时，就执行countDownLatch.countDown方法
        // 千万注意，要调用await方法，而不是wait方法！
        countDownLatch.await();
    }

}
