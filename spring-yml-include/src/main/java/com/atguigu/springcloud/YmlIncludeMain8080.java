package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class YmlIncludeMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	@Value("${mysql.url}")
	private String mysqlUrl;

	@Value("${redis.url}")
	private String redisUrl;
	
	public static void main(String[] args) {
		SpringApplication.run(YmlIncludeMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("yml include 启动完毕，当前环境：{}", profileActive);
		log.info("mysqlUrl = {}", mysqlUrl);
		log.info("redisUrl = {}", redisUrl);
	}

}
