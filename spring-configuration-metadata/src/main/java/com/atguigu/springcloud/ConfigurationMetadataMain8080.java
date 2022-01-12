package com.atguigu.springcloud;

import com.atguigu.springcloud.configuration.CustomBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.Resource;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ConfigurationMetadataMain8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	@Resource
	private CustomBean customBean;

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationMetadataMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("configuration metadata 启动完毕，当前环境：{}", profileActive);

		log.info("customBean = {}", customBean);
	}

}
