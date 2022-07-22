package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableReactiveMongoRepositories(basePackages = "com.atguigu.springcloud.webflux.dao.repository")
public class WebFluxMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(WebFluxMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("web flux 启动完毕，当前环境：{}", profileActive);
	}

}
