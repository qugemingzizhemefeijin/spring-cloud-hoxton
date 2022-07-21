package com.atguigu.springcloud;

import com.atguigu.springcloud.cola.JuiceMachineEvents;
import com.atguigu.springcloud.cola.JuiceMachineStates;
import com.atguigu.springcloud.statemachine.builder.BuilderStatemachineConfigurer;
import com.atguigu.springcloud.statemachine.normal.TurnstileEvents;
import com.atguigu.springcloud.statemachine.normal.TurnstileStates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;

import javax.annotation.Resource;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class StateMachineMain8080 implements CommandLineRunner {

	//@Resource
	//private StateMachine<TurnstileStates, TurnstileEvents> stateMachine;

	@Resource
	private StateMachineFactory<TurnstileStates, TurnstileEvents> stateMachineFactory;

	@Resource
	private StateMachinePersister<TurnstileStates, TurnstileEvents, Integer> stateMachinePersister;

	@Resource(name = "colaStateMachine")
	private com.alibaba.cola.statemachine.StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> stateMachine;

	@Resource(name = "multiColaStateMachine")
	private com.alibaba.cola.statemachine.StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> multiColaStateMachine;

	@Resource(name = "internalColaStateMachine")
	private com.alibaba.cola.statemachine.StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> internalColaStateMachine;

	@Resource(name = "choiceColaStateMachine")
	private com.alibaba.cola.statemachine.StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> choiceColaStateMachine;

	@Resource(name = "conditionNotMeet")
	private com.alibaba.cola.statemachine.StateMachine<JuiceMachineStates, JuiceMachineEvents, Integer> conditionNotMeet;

	@Resource
	private BuilderStatemachineConfigurer builder;

	public static void main(String[] args) {
		SpringApplication.run(StateMachineMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("State Machine 启动完毕");

		// spring state machine
		// this.testStateChange();

		// cola state machine
		// this.testJuiceStateChange();

		// cola choice state machine
		// this.testChoiceStateChange();

		// cola not meet state machine
		this.testNotMeetStateChange();

		log.info("==================");
	}

	private void testNotMeetStateChange() {
		// 当checkConditionFalse()执行时，永远不会满足状态流转的条件，则状态不会变化，会直接返回原来的STATE
		JuiceMachineStates target = conditionNotMeet.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 1);
		log.info("not meet change status success {}", target);
	}

	private void testChoiceStateChange() {
		JuiceMachineStates target1 = choiceColaStateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 10);
		log.info("choice change status success {}", target1);
		JuiceMachineStates target2 = choiceColaStateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 20);
		log.info("choice change status success {}", target2);
		JuiceMachineStates target3 = choiceColaStateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 30);
		log.info("choice change status success {}", target3);

		// 还可以生成PlantUML
		String uml = choiceColaStateMachine.generatePlantUML();
		log.info(uml);
	}

	private void testJuiceStateChange() {
		JuiceMachineStates target = stateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 1);
		log.info("change status success {}", target);

		//target = multiColaStateMachine.fireEvent(JuiceMachineStates.OPEN, JuiceMachineEvents.COIN, 2);
		//log.info("multi change status success {}", target);

		target = multiColaStateMachine.fireEvent(JuiceMachineStates.FALL, JuiceMachineEvents.COIN, 2);
		log.info("multi change status success {}", target);

		// 状态内部触发流转
		internalColaStateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.REACH, 3);
		target = stateMachine.fireEvent(JuiceMachineStates.CLOSE, JuiceMachineEvents.COIN, 4);
		log.info("internal change status success {}", target);
	}

	private void testStateChange() throws Exception {
		//StateMachine<TurnstileStates, TurnstileEvents> stateMachine = stateMachineFactory.getStateMachine();
		StateMachine<TurnstileStates, TurnstileEvents> stateMachine = builder.getStateMachine("V1");
		this.testStateChange(stateMachine);
	}

	private void testStateChange(StateMachine<TurnstileStates, TurnstileEvents> stateMachine) throws Exception {
		stateMachine.start();

		log.info("--- coin ---");
		stateMachinePersister.restore(stateMachine, 1);
		stateMachine.sendEvent(TurnstileEvents.COIN);
		stateMachinePersister.persist(stateMachine, 1);

		log.info("--- coin ---");
		stateMachinePersister.restore(stateMachine, 1);
		stateMachine.sendEvent(TurnstileEvents.COIN);
		stateMachinePersister.persist(stateMachine, 1);

		log.info("--- push ---");
		stateMachinePersister.restore(stateMachine, 1);
		stateMachine.sendEvent(TurnstileEvents.PUSH);
		stateMachinePersister.persist(stateMachine, 1);

		log.info("--- push ---");
		stateMachinePersister.restore(stateMachine, 1);
		stateMachine.sendEvent(TurnstileEvents.PUSH);
		stateMachinePersister.persist(stateMachine, 1);

		log.info("--- message ---");
		Message<TurnstileEvents> message = MessageBuilder.withPayload(TurnstileEvents.PUSH)
				.setHeader("orderId", "1234567890")
				.setHeader("payChannel", "AliPay")
				.build();
		stateMachine.sendEvent(message);

		stateMachine.stop();
	}

}
