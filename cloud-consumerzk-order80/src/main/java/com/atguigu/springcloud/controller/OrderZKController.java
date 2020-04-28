package com.atguigu.springcloud.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderZKController {

	public static final String INVOKE_URL = "http://cloud-provider-payment";

	@Resource
	private RestTemplate restTemplate;

	@GetMapping(value = "/consumer/payment/zk")
	public String paymentInfo() {
		String result = restTemplate.getForObject(INVOKE_URL + "/payment/zk", String.class);
		return result;
	}

}
