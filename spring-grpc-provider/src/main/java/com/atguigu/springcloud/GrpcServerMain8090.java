package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GrpcServerMain8090 implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(GrpcServerMain8090.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("GRPC SERVER 启动完毕");
	}

}
