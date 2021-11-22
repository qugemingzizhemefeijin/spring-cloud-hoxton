package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyEventTest {

    public static void init() {
        // MyEventModelFactory eventFactory = new MyEventModelFactory();
        int ringBufferSize = 1024 * 1024;

        ExecutorService executor = Executors.newCachedThreadPool();

        // 实例化disruptor（过期了是因为不建议传入线程池，而是传入ThreadFactory来让Disruptor底层来创建线程，防止线程创建失败）
        Disruptor<MyEventModel> disruptor = new Disruptor<>(
                MyEventModel::new,                   // 消息工厂
                ringBufferSize,                 // ringBuffer容器最大容量长度
                executor,                       // 线程池，最好自定义一个
                ProducerType.SINGLE,            // 单生产者模式
                new BlockingWaitStrategy()      // 等待策略
        );

        // 添加消费者监听 把MyEventConsumer绑定到disruptor
        //disruptor.handleEventsWith(new MyEventConsumer(System.out::println));

        // 调用handleEventsWithWorkerPool，表示创建的多个消费者以共同消费的模式消费
        disruptor.handleEventsWithWorkerPool(
                new MyWorkHandler(System.out::println, 1),
                new MyWorkHandler(System.out::println, 2)
        );
        // 启动disruptor
        disruptor.start();

        // 获取实际存储数据的容器RingBuffer
        RingBuffer<MyEventModel> ringBuffer = disruptor.getRingBuffer();
        // 生产发送数据
        MyEventProducer producer = new MyEventProducer(ringBuffer);
        for (int i = 0; i < 100; i++) {
            producer.producerData(String.valueOf(i));
        }

        disruptor.shutdown();
        executor.shutdown();
    }

}
