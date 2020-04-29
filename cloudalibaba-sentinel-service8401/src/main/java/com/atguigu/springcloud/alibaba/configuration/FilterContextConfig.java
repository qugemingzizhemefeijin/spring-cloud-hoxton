package com.atguigu.springcloud.alibaba.configuration;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import com.alibaba.csp.sentinel.adapter.servlet.CommonFilter;

//@Configuration
public class FilterContextConfig {

	@Bean
	public FilterRegistrationBean sentinelFilterRegistration() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new CommonFilter());
		registration.addUrlPatterns("/*");
		// 入口资源关闭聚合
		registration.addInitParameter(CommonFilter.WEB_CONTEXT_UNIFY, "false");
		registration.setName("sentinelFilter");
		registration.setOrder(1);
		return registration;
	}

}
