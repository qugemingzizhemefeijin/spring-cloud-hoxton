package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class SpringListenerMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(SpringListenerMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("spring listener 启动完毕，当前环境：{}", profileActive);
	}

}
