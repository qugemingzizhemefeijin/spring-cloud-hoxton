package com.atguigu.springcloud.statemachine.builder;

import org.springframework.context.ApplicationListener;
import org.springframework.statemachine.event.StateMachineEvent;

/**
 * 状态机中的事件类
 * OnTransitionStartEvent、
 * OnTransitionEvent、
 * OnTransitionEndEvent、
 * OnStateExiteEvent、
 * OnStateEntryEvent、
 * OnStateChangeEvent、
 * OnStateMachineStart、
 * OnStateMachineStop
 * 等都是 ApplicationEvent 的子类，因此可以用Spring 中的ApplicationListener。
 *
 * 缺点：Spring Statemachine官方认为Spring ApplicationContext 并不是一个很快的事件总线。从提升性能方面考虑，推荐使用StateMachineListener方式。
 */
public class StateMachineApplicationEventListener implements ApplicationListener<StateMachineEvent> {

    @Override
    public void onApplicationEvent(StateMachineEvent event) {
        //监听事件处理逻辑
    }

}
