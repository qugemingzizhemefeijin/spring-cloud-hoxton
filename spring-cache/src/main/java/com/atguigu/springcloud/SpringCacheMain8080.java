package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringCacheMain8080 implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheMain8080.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("spring cache 启动完毕");
    }

}
