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
//@EnableConfigurationProperties({TaskThreadPoolConfig.class}) // 开启配置属性支持
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

	// 启动源码
	/*
	public ConfigurableApplicationContext run(String... args) {
		long startTime = System.nanoTime();
		DefaultBootstrapContext bootstrapContext = createBootstrapContext();
		...
	}

	@PostConstruct和InitializingBean是在refreshContext的地方调用，代码的执行过程要
	早于@EventListener(ApplicationStartedEvent.class)
	早于ApplicationRunner/CommandLineRunner
	早于@EventListener(ApplicationReadyEvent.class)
	并且默认是单线程执行，所以得出初步结论；
	执行流程：
	1.@PostConstruct和InitializingBean (顺序下面有介绍)
	2.@EventListener(ApplicationStartedEvent.class)
	3.ApplicationRunner/CommandLineRunner
	4.@EventListener(ApplicationReadyEvent.class)
	 */

	@Override
	public void run(String... args) throws Exception {
		log.info("yml include 启动完毕，当前环境：{}", profileActive);
		log.info("mysqlUrl = {}", mysqlUrl);
		log.info("redisUrl = {}", redisUrl);
	}

	@EventListener
	public void serviceStart(ApplicationReadyEvent event){
		log.info("服务已启动，当前环境：{}", profileActive);
	}

	/*
	protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
		...
	}

	// @PostConstruct和InitializingBean是被依次调用的，并且是在同一线程执行
	1.@PostConstruct
	2.InitializingBean
	3.@EventListener(ApplicationStartedEvent.class)
	4.ApplicationRunner/CommandLineRunner
	5.@EventListener(ApplicationReadyEvent.class)
	 */

}
