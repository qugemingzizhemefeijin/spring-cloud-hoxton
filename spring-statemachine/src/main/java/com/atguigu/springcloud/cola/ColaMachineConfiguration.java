package com.atguigu.springcloud.cola;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ColaMachineConfiguration {

    private static final String MACHINE_ID = "COLA";

    // 单个状态流传到新的状态
    @Bean("colaStateMachine")
    public StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> colaStateMachine() {
        // 创建状态机
        StateMachineBuilder<JuiceMachineStates, JuiceMachineEvents, Integer> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(JuiceMachineStates.CLOSE)
                .to(JuiceMachineStates.OPEN)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition())
                .perform(doAction());

        return builder.build(MACHINE_ID);
    }

    // 多个状态流传到新的状态
    @Bean("multiColaStateMachine")
    public StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> multiColaStateMachine() {
        StateMachineBuilder<JuiceMachineStates, JuiceMachineEvents, Integer> builder = StateMachineBuilderFactory.create();

        builder.externalTransitions()
                .fromAmong(JuiceMachineStates.CLOSE, JuiceMachineStates.FALL)
                .to(JuiceMachineStates.OPEN)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition())
                .perform(doAction());

        return builder.build(MACHINE_ID + "1");
    }

    // 内部触发流转
    @Bean("internalColaStateMachine")
    public StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> internalColaStateMachine() {
        StateMachineBuilder<JuiceMachineStates, JuiceMachineEvents, Integer> builder = StateMachineBuilderFactory.create();

        builder.internalTransition()
                .within(JuiceMachineStates.CLOSE)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition())
                .perform(doAction());

        return builder.build(MACHINE_ID + "2");
    }

    /**
     * 测试选择分支，针对同一个事件：EVENT1
     * if condition == "10", STATE1 --> STATE1
     * if condition == "20" , STATE1 --> STATE2
     * if condition == "30" , STATE1 --> STATE3
     */
    @Bean("choiceColaStateMachine")
    public StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> choiceColaStateMachine() {
        StateMachineBuilder<JuiceMachineStates, JuiceMachineEvents, Integer> builder = StateMachineBuilderFactory.create();

        builder.internalTransition()
                .within(JuiceMachineStates.CLOSE)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition1())
                .perform(doAction());

        builder.externalTransition()
                .from(JuiceMachineStates.CLOSE)
                .to(JuiceMachineStates.OPEN)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition2())
                .perform(doAction());

        builder.externalTransition()
                .from(JuiceMachineStates.CLOSE)
                .to(JuiceMachineStates.FALL)
                .on(JuiceMachineEvents.COIN)
                .when(checkCondition3())
                .perform(doAction());

        return builder.build(MACHINE_ID + "3");
    }

    @Bean("conditionNotMeet")
    public StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> conditionNotMeet() {
        StateMachineBuilder<JuiceMachineStates, JuiceMachineEvents, Integer> builder = StateMachineBuilderFactory.create();

        // 重复定义相同的状态流转
        // 会在第二次builder执行到on(JuiceMachineEvents.COIN)函数时，抛出StateMachineException异常。

        // 重复定义状态机
        // 会在第二次build同名状态机时抛出StateMachineException异常。

        builder.externalTransition()
                .from(JuiceMachineStates.CLOSE)
                .to(JuiceMachineStates.OPEN)
                .on(JuiceMachineEvents.COIN)
                .when(checkConditionFalse())
                .perform(doAction());

        // 当checkConditionFalse()执行时，永远不会满足状态流转的条件，则状态不会变化，会直接返回原来的STATE

        return builder.build(MACHINE_ID + "4");
    }

    private Condition<Integer> checkConditionFalse() {
        return ctx -> ctx == 999;
    }

    private Condition<Integer> checkCondition1() {
        return (ctx) -> ctx == 10;
    }

    private Condition<Integer> checkCondition2() {
        return (ctx) -> ctx == 20;
    }

    private Condition<Integer> checkCondition3() {
        return (ctx) -> ctx == 30;
    }

    private Condition<Integer> checkCondition() {
        return (ctx) -> true;
    }

    private Action<JuiceMachineStates, JuiceMachineEvents, Integer> doAction() {
        return (from, to, event, ctx) -> System.out.println(ctx + " is operating from:" + from + " to:" + to + " on:" + event);
    }

}
