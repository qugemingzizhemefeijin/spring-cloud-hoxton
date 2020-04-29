package com.atguigu.springcloud.alibaba.controller;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.springcloud.alibaba.service.LinkTestService;

@RestController
public class FlowLimitController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowLimitController.class);
	
	@Autowired
	private LinkTestService linkTestService;
	
	//测试链路
	@GetMapping("/test-linka")
	public String testLinkA() {
		linkTestService.getOrder();
		return "------testLinkA";
	}
	
	//测试链路
	@GetMapping("/test-linkb")
	public String testLinkB() {
		linkTestService.getOrder();
		return "------testLinkB";
	}
	
	@GetMapping("/testA")
	public String testA() {
		return "------testA";
	}
	
	@GetMapping("/testA2")
	public String testA2() {
		try {//应用于线程流控
			TimeUnit.SECONDS.sleep(8);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "------testA2";
	}

	@GetMapping("/testB")
	public String testB() {
		LOGGER.info(Thread.currentThread().getName() + "\t" + "...testB");
		return "------testB";
	}

	@GetMapping("/testD")
	public String testD() {
		// try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) {
		// e.printStackTrace(); }
		// log.info("testD 测试RT");

		LOGGER.info("testD 异常比例");
		int age = 10 / 0;
		return "------testD";
	}

	@GetMapping("/testE")
	public String testE() {
		LOGGER.info("testE 测试异常数");
		int age = 10 / 0;
		return "------testE 测试异常数";
	}

	@GetMapping("/testHotKey")
	@SentinelResource(value = "testHotKey", blockHandler = "deal_testHotKey")
	public String testHotKey(@RequestParam(value = "p1", required = false) String p1,
			@RequestParam(value = "p2", required = false) String p2) {
		// int age = 10/0;
		return "------testHotKey";
	}

	public String deal_testHotKey(String p1, String p2, BlockException exception) {
		return "------deal_testHotKey,o(╥﹏╥)o"; // sentinel系统默认的提示：Blocked by Sentinel (flow limiting)
	}

}
