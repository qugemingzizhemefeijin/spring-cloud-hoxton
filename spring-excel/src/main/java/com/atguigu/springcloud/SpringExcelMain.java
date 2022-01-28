package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringExcelMain implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(SpringExcelMain.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("spring excel 启动完毕，当前环境：{}", profileActive);
	}

}
