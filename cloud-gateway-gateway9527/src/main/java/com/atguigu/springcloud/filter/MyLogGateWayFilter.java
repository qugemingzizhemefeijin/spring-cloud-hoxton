package com.atguigu.springcloud.filter;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyLogGateWayFilter implements GlobalFilter, Ordered {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyLogGateWayFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		LOGGER.info("***********come in MyLogGateWayFilter:  " + new Date());

		String uname = exchange.getRequest().getQueryParams().getFirst("uname");

		if (uname == null) {
			LOGGER.info("*******用户名为null，非法用户，o(╥﹏╥)o");
			exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
			return exchange.getResponse().setComplete();
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}
}
