package com.atguigu.springcloud;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class PaymentMain8000 implements CommandLineRunner {
	
	@Autowired
    private StringEncryptor codeSheepEncryptorBean;
	
	@Autowired
    private ApplicationContext appCtx;
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentMain8000.class, args);
	}
	
	@Bean
    public MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

	@Override
	public void run(String... args) throws Exception {
		Environment environment = appCtx.getBean(Environment.class);
		
		//https://www.toutiao.com/i6819460523768152583/?tt_from=weixin&utm_campaign=client_share&wxshare_count=1&timestamp=1588583421&app=news_article&utm_source=weixin&utm_medium=toutiao_android&req_id=202005041710210100180851470CE7D4C7&group_id=6819460523768152583
		
		// 首先获取配置文件里的配置项
        String mysqlOriginPswd = environment.getProperty("aa.cc");
		
		System.out.println("加密后的密文是："+codeSheepEncryptorBean.encrypt("abceld"));
		// 打印解密后的结果
        System.out.println("原始明文密码为：" + mysqlOriginPswd);
		System.out.println("启动完毕++++++++++");
	}

}
