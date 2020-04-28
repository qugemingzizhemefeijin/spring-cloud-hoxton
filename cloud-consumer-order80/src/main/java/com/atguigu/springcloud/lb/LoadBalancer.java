package com.atguigu.springcloud.lb;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;

public interface LoadBalancer {
	
	ServiceInstance instances(List<ServiceInstance> serviceInstances);

}
