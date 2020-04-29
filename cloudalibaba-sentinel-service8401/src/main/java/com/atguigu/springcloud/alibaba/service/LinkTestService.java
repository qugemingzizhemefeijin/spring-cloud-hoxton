package com.atguigu.springcloud.alibaba.service;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;

@Service
public class LinkTestService {

	@SentinelResource("getOrder")
	public String getOrder() {
		System.out.println("==================================");
		return "order";
	}

}
