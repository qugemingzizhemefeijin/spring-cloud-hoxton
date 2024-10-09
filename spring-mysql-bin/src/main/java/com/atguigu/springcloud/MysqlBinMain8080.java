package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
public class MysqlBinMain8080 implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String profileActive;

    public static void main(String[] args) {
        SpringApplication.run(MysqlBinMain8080.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("encrypt main 启动完毕，当前环境：{}", profileActive);
    }

    @EventListener
    public void serviceStart(ApplicationReadyEvent event) {
        log.info("服务已启动，当前环境：{}", profileActive);
    }

}
