package com.atguigu.springcloud;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//@EnableTransactionManagement
@Import({InfrastructureAdvisorAutoProxyCreator.class}) // https://blog.csdn.net/jxch____/article/details/110678491
@EnableLogRecord(tenant = "com.atguigu.springcloud")
public class MouztBizlogMain8080 implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(MouztBizlogMain8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("mouzt bizlog 启动完毕++++++++++");
	}

}
