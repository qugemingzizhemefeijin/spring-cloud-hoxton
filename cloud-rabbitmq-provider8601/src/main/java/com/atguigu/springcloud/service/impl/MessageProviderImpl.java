package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.IMessageProvider;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProviderImpl implements IMessageProvider {
	
	@Autowired
	private AmqpTemplate rabbitAmqpTemplate;

	@Override
	public void send() {
		/**
		 * convertAndSend - 转换并发送消息的template方法。
		 * 是将传入的普通java对象，转换为rabbitmq中需要的message类型对象，并发送消息到rabbitmq中。
		 * 参数一：交换器名称。 类型是String
		 * 参数二：路由键。 类型是String
		 * 参数三：消息，是要发送的消息内容对象。类型是Object
		 */
		this.rabbitAmqpTemplate.convertAndSend("top_test", "aaaa", "Hello Rabbit!");
	}
}
