package com.atguigu.springcloud.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.atguigu.springcloud.service.IMessageProvider;

@EnableBinding(Source.class) // 定义消息的推送管道
public class MessageProviderImpl implements IMessageProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageProviderImpl.class);
	
	@Resource
	private MessageChannel output; // 消息发送管道

	@Override
	public String send() {
		String serial = UUID.randomUUID().toString();
		boolean b = output.send(MessageBuilder.withPayload(serial).build());
		LOGGER.info("*****serial: " + serial);
		return String.valueOf(b);
	}
}
