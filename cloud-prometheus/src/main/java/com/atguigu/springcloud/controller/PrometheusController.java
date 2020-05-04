package com.atguigu.springcloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusController {
	
	@GetMapping("/test")
	public String prometheusTest() {
		return "Prometheus";
	}

}
