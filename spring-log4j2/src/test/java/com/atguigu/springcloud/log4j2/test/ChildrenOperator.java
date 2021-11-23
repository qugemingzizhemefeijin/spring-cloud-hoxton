package com.atguigu.springcloud.log4j2.test;

import com.atguigu.springcloud.disruptor.MyEventModel;
import com.lmax.disruptor.dsl.Disruptor;

@FunctionalInterface
public interface ChildrenOperator {

    void childOperator(Disruptor<MyEventModel> disruptor);

}
