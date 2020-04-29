package com.atguigu.springcloud.controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;

@SuppressWarnings({"unchecked","rawtypes"})
@RestController
public class PaymentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);
	
	@Resource
	private PaymentService paymentService;

	@Value("${server.port}")
	private String serverPort;
	
	//用于服务暴露
	@Resource
	private DiscoveryClient discoveryClient;

	@PostMapping(value = "/payment/create")
	public CommonResult create(@RequestBody Payment payment) {
		int result = paymentService.create(payment);
		LOGGER.info("*****插入结果：" + result);

		if (result > 0) {
			return new CommonResult(200, "插入数据库成功,serverPort: " + serverPort, result);
		} else {
			return new CommonResult(444, "插入数据库失败", null);
		}
	}

	@GetMapping(value = "/payment/get/{id}")
	public CommonResult<Payment> getPaymentById(@PathVariable("id") Long id, HttpServletRequest request) {
		Payment payment = paymentService.getPaymentById(id);
		
		for(Map.Entry<String,String[]> me : request.getParameterMap().entrySet()) {
			LOGGER.info(me.getKey()+"="+me.getValue()[0]);
		}
		
		if (payment != null) {
			return new CommonResult(200, "查询成功,serverPort:  " + serverPort, payment);
		} else {
			return new CommonResult(444, "没有对应记录,查询ID: " + id, null);
		}
	}
	
	//展示所有的暴露服务的主机信息
	//需要在主启动类上添加@EnableDiscoveryClient注解
	@GetMapping(value = "/payment/discovery")
	public Object discovery() {
		List<String> services = discoveryClient.getServices();
        for (String element : services) {
        	LOGGER.info("*****element: "+element);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            LOGGER.info(instance.getServiceId()+"\t"+instance.getHost()+"\t"+instance.getPort()+"\t"+instance.getUri());
        }

        return this.discoveryClient;
	}
	
	@GetMapping("/payment/lb")
	public String getPaymentLB() {
		return this.serverPort;
	}
	
	@GetMapping(value = "/payment/feign/timeout")
	public String paymentFeignTimeout() {
		// 业务逻辑处理正确，但是需要耗费3秒钟
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return serverPort;
	}
	
	@GetMapping("/payment/zipkin")
	public String paymentZipkin() {
		return "HI, I'M payment zipkin server , welcome to atguigu,^_^";
	}

}