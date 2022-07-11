package com.atguigu.springcloud;

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

	@Resource
	private BuilderStatemachineConfigurer builder;

	public static void main(String[] args) {
		SpringApplication.run(StateMachineMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("State Machine 启动完毕");

		this.testStateChange();

		log.info("==================");
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
