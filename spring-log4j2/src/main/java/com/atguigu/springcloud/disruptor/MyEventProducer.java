package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.RingBuffer;

public class MyEventProducer {

    private final RingBuffer<MyEventModel> ringBuffer;

    public MyEventProducer(RingBuffer<MyEventModel> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 生产数据信息
     * @param data String
     */
    public void producerData(String data){
        // 从ringBuffer获取可用sequence序号
        long sequence = ringBuffer.next();
        try {
            // 根据sequence获取sequence对应的Event
            // 这个Event是一个没有赋值具体数据的对象
            MyEventModel testEvent = ringBuffer.get(sequence);
            testEvent.setData(data);
        } finally {
            //提交发布
            ringBuffer.publish(sequence);
        }
    }

}
