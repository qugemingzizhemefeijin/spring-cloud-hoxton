package com.atguigu.springcloud.statemachine.builder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.*;

import java.util.Map;

/**
 * Builder模式，不好用此方法。因为 StateMachine 对象不是属于spring管理
 */
@Slf4j
@WithStateMachine(id = BuilderStatemachineConfigurer.STATE_MACHINE_NAME)
public class StateMachineAction {

    @OnTransition(source = "Unlocked", target = "Locked")
    public void fromUnlockedToLocked() {
        log.info("I bei Locked");
    }

    @OnTransition
    public void anyTransition() {
        log.info("Hello anyTransition");
    }

    @OnTransition(source = "Locked", target = "Unlocked")
    public void fromLockToUnlocked(@EventHeaders Map<String, Object> headers, ExtendedState extendedState) {
        log.info("I bei Unlocked");
    }

    @OnTransition
    public void anyTransition(
            @EventHeaders Map<String, Object> headers,
            @EventHeader("orderId") Object orderId,
            @EventHeader(name = "payChannel", required = false) String payChannel,
            ExtendedState extendedState,
            StateMachine<String, String> stateMachine,
            Message<String> message,
            Exception e) {
        log.info("orderId => {}", orderId);
        log.info("payChannel => {}", payChannel);
    }

    @OnStateChanged
    public void anyStateChange() {
        log.info("Hello anyStateChange");
    }

    @OnEventNotAccepted
    public void anyEventNotAccepted() {
        log.info("Hello anyEventNotAccepted");
    }

    @OnEventNotAccepted(event = "COIN")
    public void e1EventNotAccepted() {
        log.info("Hello e1EventNotAccepted");
    }

}
