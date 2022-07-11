package com.atguigu.springcloud.statemachine.builder;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class StateMachineListenerAdapter<TurnstileStates, TurnstileEvents> implements StateMachineListener<TurnstileStates, TurnstileEvents> {

    @Override
    public void stateChanged(State<TurnstileStates, TurnstileEvents> from, State<TurnstileStates, TurnstileEvents> to) {

    }

    @Override
    public void stateEntered(State<TurnstileStates, TurnstileEvents> state) {

    }

    @Override
    public void stateExited(State<TurnstileStates, TurnstileEvents> state) {

    }

    @Override
    public void eventNotAccepted(Message<TurnstileEvents> event) {

    }

    @Override
    public void transition(Transition<TurnstileStates, TurnstileEvents> transition) {

    }

    @Override
    public void transitionStarted(Transition<TurnstileStates, TurnstileEvents> transition) {

    }

    @Override
    public void transitionEnded(Transition<TurnstileStates, TurnstileEvents> transition) {

    }

    @Override
    public void stateMachineStarted(StateMachine<TurnstileStates, TurnstileEvents> stateMachine) {

    }

    @Override
    public void stateMachineStopped(StateMachine<TurnstileStates, TurnstileEvents> stateMachine) {

    }

    @Override
    public void stateMachineError(StateMachine<TurnstileStates, TurnstileEvents> stateMachine, Exception exception) {

    }

    @Override
    public void extendedStateChanged(Object key, Object value) {

    }

    @Override
    public void stateContext(StateContext<TurnstileStates, TurnstileEvents> stateContext) {

    }

}
