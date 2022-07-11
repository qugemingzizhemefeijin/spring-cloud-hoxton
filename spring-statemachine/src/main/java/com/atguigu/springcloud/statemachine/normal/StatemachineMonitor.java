package com.atguigu.springcloud.statemachine.normal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.annotation.OnStateChanged;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * 定义动作监听类，StatemachineMonitor（名称随意），添加注解@WithStateMachine。
 * 本例中使用id进行状态机绑定，根据文档定义，可以使用name和id两种属性绑定需要监听的状态机实例。
 * 如果不定义任何name或者id，默认监听名称为stateMachine的状态机。
 */
@Slf4j
@WithStateMachine(id = "turnstileStateMachine")
public class StatemachineMonitor {

    @OnTransition
    public void anyTransition() {
        log.info("--- OnTransition --- init");
    }

    @OnTransition(target = "Unlocked")
    public void toState1() {
        log.info("--- OnTransition --- toState1");
    }

    @OnStateChanged(source = "Unlocked")
    public void fromState1() {
        log.info("--- OnTransition --- fromState1");
    }

}
