package com.atguigu.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

// 模拟OOM -Xmx30m，这里有个坑就是发邮件是同步的，会卡。得想办法怎么替换成异步的。
@Slf4j
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class Log4j2Main8080 implements CommandLineRunner {

	@Value("${spring.profiles.active}")
	private String profileActive;

	public static void main(String[] args) {
		SpringApplication.run(Log4j2Main8080.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("log4j2 启动完毕，当前环境：{}", profileActive);

		/*Thread t = new Thread(() -> {
			MyEventTest.init();

			while(true) {
				IntStream.range(0, 2).mapToObj(String::valueOf).forEach(log::info);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					log.error(e.getMessage(), e);
				}
			}
		});
		t.setDaemon(true);
		t.start();*/
	}

}
