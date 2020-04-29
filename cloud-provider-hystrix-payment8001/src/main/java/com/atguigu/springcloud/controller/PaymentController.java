package com.atguigu.springcloud.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.service.PaymentService;

@RestController
public class PaymentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
	
	@Resource
	private PaymentService paymentService;

	@Value("${server.port}")
	private String serverPort;

	/**
	 * 测试正常的
	 */
	@GetMapping("/payment/hystrix/ok/{id}")
	public String paymentInfo_OK(@PathVariable("id") Integer id) {
		String result = paymentService.paymentInfo_OK(id);
		LOGGER.info("*****result: " + result);
		return result;
	}

	/**
	 * 测试超时
	 */
	@GetMapping("/payment/hystrix/timeout/{id}")
	public String paymentInfo_TimeOut(@PathVariable("id") Integer id) {
		String result = paymentService.paymentInfo_TimeOut(id);
		LOGGER.info("*****result: " + result);
		return result;
	}

	/**
	 * 测试熔断
	 */
	@GetMapping("/payment/circuit/{id}")
	public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
		String result = paymentService.paymentCircuitBreaker(id);
		LOGGER.info("****result: " + result);
		return result;
	}
}
