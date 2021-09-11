package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RedissonMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.password}")
	private String password;

	@Bean
	public RedissonClient redissonClient(){
		Config config = new Config();
		String url = "redis://" + host + ":" + port;
		config.useSingleServer().setAddress(url).setPassword(password);

		try {
			return Redisson.create(config);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RedissonMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("yml include 启动完毕，当前环境：{}", profileActive);
	}

}
