package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MultiProducerService {

    private final Disruptor<MyEventModel> disruptor;

    private final AtomicInteger consumer = new AtomicInteger();

    public MultiProducerService() {
        // 实例化disruptor（过期了是因为不建议传入线程池，而是传入ThreadFactory来让Disruptor底层来创建线程，防止线程创建失败）
        disruptor = new Disruptor<>(
                MyEventModel::new,                   // 消息工厂
                16,                    // ringBuffer容器最大容量长度
                Executors.newCachedThreadPool(),                            // 线程池，最好自定义一个
                ProducerType.MULTI,                 // 多生产者模式
                new BlockingWaitStrategy()           // 等待策略
        );
    }

    public void start() {
        // 启动disruptor
        disruptor.start();
    }

    public void stop() {
        disruptor.shutdown();
        disruptor.shutdown();
    }

    public Disruptor<MyEventModel> getDisruptor() {
        return disruptor;
    }

    public int consumerCounter() {
        return consumer.incrementAndGet();
    }

    public int getConsumer() {
        return consumer.get();
    }
}
