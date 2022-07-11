package com.atguigu.springcloud.statemachine.normal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import java.util.EnumSet;

/**
 * 缺点：依赖@Configuration注解和Spring应用上下文，即编译时就需要指定状态机配置。
 */
@Slf4j
//@Configuration
//@EnableStateMachine
public class StatemachineConfigurer extends EnumStateMachineConfigurerAdapter<TurnstileStates, TurnstileEvents> {

    @Autowired
    private BizStateMachinePersist bizStateMachinePersist;

    @Bean
    public StateMachinePersister<TurnstileStates, TurnstileEvents, Integer> stateMachinePersist() {
        return new DefaultStateMachinePersister<>(bizStateMachinePersist);
    }

    @Override
    public void configure(StateMachineStateConfigurer<TurnstileStates, TurnstileEvents> states) throws Exception {
        states.withStates()
              // 初识状态：Locked
              .initial(TurnstileStates.Locked)
              .states(EnumSet.allOf(TurnstileStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<TurnstileStates, TurnstileEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(TurnstileStates.Locked).target(TurnstileStates.Unlocked)
                .event(TurnstileEvents.COIN).action(turnstileUnlock())
                .and()
                .withExternal()
                .source(TurnstileStates.Unlocked).target(TurnstileStates.Locked)
                .event(TurnstileEvents.PUSH).action(customerPassAndLock());
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<TurnstileStates, TurnstileEvents> config) throws Exception {
        config.withConfiguration().machineId("turnstileStateMachine");
    }

    public Action<TurnstileStates, TurnstileEvents> turnstileUnlock() {
        return context -> log.info("解锁旋转门，以便游客能够通过");
    }

    public Action<TurnstileStates, TurnstileEvents> customerPassAndLock() {
        return context -> log.info("当游客通过，锁定旋转门");
    }

}
