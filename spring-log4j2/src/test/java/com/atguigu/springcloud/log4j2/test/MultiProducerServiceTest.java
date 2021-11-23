package com.atguigu.springcloud.log4j2.test;

import com.atguigu.springcloud.Log4j2Main8080;
import com.atguigu.springcloud.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Log4j2Main8080.class)
@Slf4j
public class MultiProducerServiceTest {

    public static final int EVENT_COUNT = 10;

    @Resource
    private MultiProducerService multiProducerService;

    // 两个生产者，两个消费者
    @Test
    public void testMultiProducerService1() throws Exception {
        log.info("start 两个生产者，两个消费者");

        Disruptor<MyEventModel> disruptor = multiProducerService.getDisruptor();
        // 第一个生产者
        MyEventProducer producer1 = new MyEventProducer(disruptor.getRingBuffer());
        // 第二个生产者
        MyEventProducer producer2 = new MyEventProducer(disruptor.getRingBuffer());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // 两个独立消费者，每人消费200个事件，因此一共消费400个事件
        int expectEventCount = EVENT_COUNT*4;

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        // 一号消费者
        MyEventConsumer c1 = new MyEventConsumer(pointer);

        // 二号消费者
        MyEventConsumer c2 = new MyEventConsumer(pointer);

        disruptor.handleEventsWith(c1, c2);

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

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end 两个生产者，两个消费者");
    }

}
