package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class RemoteRPCFeignMain8090 implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String profileActive;

    public static void main(String[] args) {
        SpringApplication.run(RemoteRPCFeignMain8090.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("remote feign service 启动完毕，当前环境：{}", profileActive);
    }

}