package com.atguigu.springcloud.statemachine.normal;

import com.google.common.collect.Maps;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 状态机持久化，实际环境中，当前状态往往都是从持久化介质中实时获取的，Spring Statemachine通过实现StateMachinePersist接口，write和read当前状态机的状态
 * 使用的是HashMap作为模拟存储介质，正式项目中需要使用真实的状态获取途径。
 */
@Component
public class BizStateMachinePersist implements StateMachinePersist<TurnstileStates, TurnstileEvents, Integer> {

    private static final Map<Integer, TurnstileStates> CACHE = Maps.newHashMap();

    @Override
    public void write(StateMachineContext<TurnstileStates, TurnstileEvents> stateMachineContext, Integer integer) throws Exception {
        CACHE.put(integer, stateMachineContext.getState());
    }

    @Override
    public StateMachineContext<TurnstileStates, TurnstileEvents> read(Integer integer) throws Exception {
        // 注意状态机的初识状态与配置中定义的一致
        return CACHE.containsKey(integer) ?
                new DefaultStateMachineContext<>(CACHE.get(integer), null, null, null, null, "turnstileStateMachine") :
                new DefaultStateMachineContext<>(TurnstileStates.Locked, null, null, null, null, "turnstileStateMachine");
    }

}
