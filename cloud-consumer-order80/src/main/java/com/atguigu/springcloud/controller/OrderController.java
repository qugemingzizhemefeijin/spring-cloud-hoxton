package com.atguigu.springcloud.controller;

import java.net.URI;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.lb.LoadBalancer;

@RestController
@SuppressWarnings({"unchecked","rawtypes"})
public class OrderController {
	
	//public static final String PAYMENT_URL = "http://localhost:8001";
	
	public static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
	
	@Resource
    private RestTemplate restTemplate;
	
	@Resource
	private LoadBalancer loadBalancer;

    @Resource
    private DiscoveryClient discoveryClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@GetMapping("/consumer/payment/create")
	public CommonResult<Payment> create(Payment payment) {
		return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, CommonResult.class);
	}
	
	@GetMapping("/consumer/payment/createEntity")
	public CommonResult<Payment> createEntity(Payment payment) {
		return restTemplate.postForEntity(PAYMENT_URL + "/payment/create", payment, CommonResult.class).getBody();
	}

	@GetMapping("/consumer/payment/get/{id}")
	public CommonResult<Payment> getPayment(@PathVariable("id") Long id) {
		return restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
	}

	@GetMapping("/consumer/payment/getForEntity/{id}")
	public CommonResult<Payment> getPayment2(@PathVariable("id") Long id) {
		ResponseEntity<CommonResult> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
		
		if(entity.getStatusCode().is2xxSuccessful()) {
			LOGGER.info("statusCode = {}" , entity.getStatusCode());
			
			return entity.getBody();
		} else {
			return new CommonResult<>(444, "操作失败");
		}
	}
	
	@GetMapping("/consumer/payment/lb")
	public String getPaymentLB() {
		List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
		
		if (instances == null || instances.size() <= 0) {
			return null;
		}
		
		ServiceInstance serviceInstance = loadBalancer.instances(instances);
		URI uri = serviceInstance.getUri();

		return restTemplate.getForObject(uri + "/payment/lb", String.class);
	}

}
