package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.tio.websocket.starter.EnableTioWebSocketServer;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
// 显式配置了 @EnableWebMvc，spring boot访问静态资源会失败
// @EnableWebMvc
@EnableTioWebSocketServer
public class WebSocketMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(WebSocketMain8080.class, args);
	}
	
	@Override
	public void run(String... args) {
		log.info("web socket 启动完毕，当前环境：{}", profileActive);
	}

}
