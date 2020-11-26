package com.atguigu.springcloud.controller;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;

/**
 * 消息接收者 - consumer
 *
 * @RabbitListener - 可以注解类和方法。
 *  注解类，当表当前类的对象是一个rabbit listener。
 *      监听逻辑明确，可以由更好的方法定义规范。
 *      必须配合@RabbitHandler才能实现rabbit消息消费能力，一个类可以有多个方法，但是仅有一个方法注解@RabbitHandler。
 *  注解方法，代表当前方法是一个rabbit listener处理逻辑。
 *      方便开发，一个类中可以定义若干个listener逻辑。
 *      方法定义规范可能不合理。如：一个方法的处理逻辑太多，造成方法的bad smell。
 *
 * @RabbitListener -  代表当前类型是一个rabbitmq的监听器。
 *      bindings:绑定队列
 * @QueueBinding  - @RabbitListener.bindings属性的类型。绑定一个队列。
 *      value:绑定队列， Queue类型。
 *      exchange:配置交换器， Exchange类型。
 *      key:路由键，字符串类型。
 *
 * @Queue - 队列。
 *      value:队列名称
 *      autoDelete:是否是一个临时队列。
 *          true ：当所有的consumer关闭后，自动删除queue。
 *          false：当任意一个consumer启动并创建queue后，如果queue中有消息未消费，无论是否有consumer继续执行，都保存queue。
 *
 * @Exchange - 交换器
 *      value:为交换器起个名称
 *      type:指定具体的交换器类型
 */
@Component
@RabbitListener(
	bindings=@QueueBinding(
		value=@Queue(value="queue8603",autoDelete="false"),
		exchange=@Exchange(value="top_test",type= ExchangeTypes.DIRECT),
		key="aaaa"
	)
)
public class ReceiveMessageListenerController {

	/**
	 * 消费消息的方法。采用消息队列监听机制
	 * @RabbitHandler - 代表当前方法是监听队列状态的方法，就是队列状态发生变化后，执行的消费消息的方法。
	 * 方法参数。就是处理的消息的数据载体类型。
	 */
	@RabbitHandler
	public void process(String msg){
		System.out.println("receiver..........msg: "+msg);
	}

}
