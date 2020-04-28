package com.atguigu.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {

	@Bean
	//@LoadBalanced//加上次注解才能访问到eureka上的微服物名称的接口，否则会报UnknownHostException异常
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
