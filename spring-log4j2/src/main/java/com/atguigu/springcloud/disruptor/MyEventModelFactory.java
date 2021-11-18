package com.atguigu.springcloud.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * 消息生产工厂
 */
public class MyEventModelFactory implements EventFactory<MyEventModel> {

    @Override
    public MyEventModel newInstance() {
        return new MyEventModel();
    }

}
