package com.atguigu.springcloud.statemachine.builder;

import com.atguigu.springcloud.statemachine.normal.TurnstileEvents;
import com.atguigu.springcloud.statemachine.normal.TurnstileStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * 通过Builder构建工厂【推荐】
 */
@Slf4j
@Component
public class BuilderStatemachineConfigurer implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public static final String STATE_MACHINE_NAME = "TEST_STATE_MACHINE";

    /**
     * Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContext方法
     *
     * @param applicationContext
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /*@Bean
    public StateMachineApplicationEventListener contextListener() {
        return new StateMachineApplicationEventListener();
    }*/

    // 状态机实例添加Listener
    /*@Bean
    public StateMachineEventListener stateMachineEventListener() {
        StateMachineEventListener listener = new StateMachineEventListener();
        stateMachine.addStateListener(listener);
        return listener;
    }*/

    public StateMachine<TurnstileStates, TurnstileEvents> getStateMachine(String id) throws Exception {
        Builder<TurnstileStates, TurnstileEvents> builder = StateMachineBuilder.builder();

        // 构建状态机的所有的状态以及初始化状态
        builder.configureStates()
                .withStates()
                .initial(TurnstileStates.Locked)
                //.end(TurnstileStates.Locked)
                .states(EnumSet.allOf(TurnstileStates.class));

        // 构建状态机的过渡条件
        builder.configureTransitions()
                .withExternal()
                .source(TurnstileStates.Locked).target(TurnstileStates.Unlocked)
                .event(TurnstileEvents.COIN).action(turnstileUnlock())
                .and()
                .withExternal()
                .source(TurnstileStates.Unlocked).target(TurnstileStates.Locked)
                .event(TurnstileEvents.PUSH).action(customerPassAndLock());

        // 构建状态机的信息
        builder.configureConfiguration()
                .withConfiguration()
                .machineId(STATE_MACHINE_NAME)
                .beanFactory(this.applicationContext)
                .listener(new StateMachineListenerAdapter<>());

        return builder.build();
    }

    public Action<TurnstileStates, TurnstileEvents> turnstileUnlock() {
        return context -> log.info("解锁旋转门，以便游客能够通过");
    }

    public Action<TurnstileStates, TurnstileEvents> customerPassAndLock() {
        return context -> log.info("当游客通过，锁定旋转门");
    }

}
