package com.atguigu.springcloud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JWTMain8000 implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(JWTMain8000.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("JWT 启动完毕++++++++++");
	}

}
