package com.atguigu.springcloud;

import com.atguigu.springcloud.annotation.ServiceScan;
import com.atguigu.springcloud.service.OrderService;
import com.atguigu.springcloud.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

//ImportSelector主要是实现些比较复杂有逻辑性的bean装载，我们可以在selectImports做下逻辑判断，比如@ComponentScan像这个扫描器
@SpringBootApplication
//@Import({UserImportSelector.class, UserServiceImpl.class})
@ServiceScan("com.atguigu.springcloud.service.impl")
public class ImportSelectorMain8000 implements CommandLineRunner {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ImportSelectorMain8000.class, args);

		OrderService orderService = context.getBean(OrderService.class);
		orderService.print("World!");

		UserService userService = context.getBean(UserService.class);
		userService.register("small orange");
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("Import Selector 启动完毕++++++++++");
	}

}
