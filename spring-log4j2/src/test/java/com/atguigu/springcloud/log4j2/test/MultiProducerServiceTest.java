package com.atguigu.springcloud.log4j2.test;

import com.atguigu.springcloud.Log4j2Main8080;
import com.atguigu.springcloud.disruptor.MyEventConsumer;
import com.atguigu.springcloud.disruptor.MyEventPointer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Log4j2Main8080.class)
@Slf4j
public class MultiProducerServiceTest extends BaseMultiProducerServiceTest {

    @Test
    public void testMultiProducerService1() throws Exception {
        log.info("start 两个生产者，两个消费者");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // 两个独立消费者，每人消费200个事件，因此一共消费400个事件
        int expectEventCount = EVENT_COUNT*4;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);

            disruptor.handleEventsWith(c1, c2);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end 两个生产者，两个消费者");
    }

    // C1、C2独立消费，C3依赖C1和C2
    @Test
    public void testMultiProducerService2() throws Exception {
        log.info("start 两个生产者，三个消费者，C1、C2独立消费，C3依赖C1和C2");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // 两个独立消费者，一个独立消费者，每人消费200个事件，因此一共消费600个事件
        int expectEventCount = EVENT_COUNT*6;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);

            // C1、C2独立消费
            disruptor.handleEventsWith(c1, c2)
                    // C3依赖C1和C2
                    .then(c3);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end 两个生产者，三个消费者，C1、C2独立消费，C3依赖C1和C2");
    }

    // C1独立消费，C2和C3也独立消费，但依赖C1，C4依赖C2和C3
    @Test
    public void testMultiProducerService3() throws Exception {
        log.info("start 两个生产者，C1独立消费，C2和C3也独立消费，但依赖C1，C4依赖C2和C3");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // C1独立消费，C2和C3也独立消费，但依赖C1，C4依赖C2和C3，因此一共消费800个事件
        int expectEventCount = EVENT_COUNT*8;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);
            // 四号消费者
            MyEventConsumer c4 = new MyEventConsumer("c4" ,pointer);

            // C1、C2独立消费
            disruptor.handleEventsWith(c1)
                    // C2和C3也独立消费
                    .then(c2, c3)
                    // C4依赖C2和C3
                    .then(c4);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end 两个生产者，C1独立消费，C2和C3也独立消费，但依赖C1，C4依赖C2和C3");
    }

    // C1和C2独立消费，C3和C4也是独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
    @Test
    public void testMultiProducerService4() throws Exception {
        log.info("start 两个生产者，C1和C2独立消费，C3和C4也是独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // C1和C2独立消费，C3和C4也是独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4，因此一共消费1000个事件
        int expectEventCount = EVENT_COUNT*10;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);
            // 四号消费者
            MyEventConsumer c4 = new MyEventConsumer("c4" ,pointer);
            // 五号消费者
            MyEventConsumer c5 = new MyEventConsumer("c5" ,pointer);

            // C1、C2独立消费
            disruptor.handleEventsWith(c1, c2)
                    // C3和C4也是独立消费，但C3和C4都依赖C1和C2
                    .then(c3, c4)
                    // 然后C5依赖C3和C4
                    .then(c5);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end C1和C2独立消费，C3和C4也是独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");
    }

    // C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
    @Test
    public void testMultiProducerService5() throws Exception {
        log.info("start 两个生产者，C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4，因此一共消费600个事件
        int expectEventCount = EVENT_COUNT*6;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);
            // 四号消费者
            MyEventConsumer c4 = new MyEventConsumer("c4" ,pointer);
            // 五号消费者
            MyEventConsumer c5 = new MyEventConsumer("c5" ,pointer);

            // C1和C2共同消费
            disruptor.handleEventsWithWorkerPool(c1, c2)
                    // C3和C4也是共同消费，但C3和C4都依赖C1和C2
                    .thenHandleEventsWithWorkerPool(c3, c4)
                    // 然后C5依赖C3和C4
                    .thenHandleEventsWithWorkerPool(c5);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");
    }

    // C1和C2共同消费，C3和C4独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
    @Test
    public void testMultiProducerService6() throws Exception {
        log.info("start 两个生产者，C1和C2共同消费，C3和C4独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // C1和C2共同消费，C3和C4也是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4，因此一共消费800个事件
        int expectEventCount = EVENT_COUNT*8;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);
            // 四号消费者
            MyEventConsumer c4 = new MyEventConsumer("c4" ,pointer);
            // 五号消费者
            MyEventConsumer c5 = new MyEventConsumer("c5" ,pointer);

            // C1和C2共同消费
            disruptor.handleEventsWithWorkerPool(c1, c2)
                    // C3和C4独立消费，但C3和C4都依赖C1和C2
                    .then(c3, c4)
                    // 然后C5依赖C3和C4
                    .then(c5);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end C1和C2共同消费，C3和C4独立消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");
    }

    // C1和C2独立消费，C3和C4是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4
    @Test
    public void testMultiProducerService7() throws Exception {
        log.info("start 两个生产者，C1和C2独立消费，C3和C4是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");

        // 两个生产者，每个生产100个事件，一共生产两百个事件
        // C1和C2独立消费，C3和C4是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4，因此一共消费800个事件
        int expectEventCount = EVENT_COUNT*8;
        CountDownLatch countDownLatch = new CountDownLatch(1);

        MyEventPointer pointer = new MyEventPointer(expectEventCount, multiProducerService, countDownLatch);

        this.start(countDownLatch, disruptor -> {
            // 一号消费者
            MyEventConsumer c1 = new MyEventConsumer("c1" ,pointer);
            // 二号消费者
            MyEventConsumer c2 = new MyEventConsumer("c2" ,pointer);
            // 三号消费者
            MyEventConsumer c3 = new MyEventConsumer("c3" ,pointer);
            // 四号消费者
            MyEventConsumer c4 = new MyEventConsumer("c4" ,pointer);
            // 五号消费者
            MyEventConsumer c5 = new MyEventConsumer("c5" ,pointer);

            // C1和C2独立消费
            disruptor.handleEventsWith(c1, c2)
                    // C3和C4是共同消费，但C3和C4都依赖C1和C2
                    .thenHandleEventsWithWorkerPool(c3, c4)
                    // 然后C5依赖C3和C4
                    .then(c5);
        });

        // 消费的事件总数应该等于发布的事件数
        assertEquals(expectEventCount, multiProducerService.getConsumer());

        log.info("end C1和C2独立消费，C3和C4是共同消费，但C3和C4都依赖C1和C2，然后C5依赖C3和C4");
    }

}
