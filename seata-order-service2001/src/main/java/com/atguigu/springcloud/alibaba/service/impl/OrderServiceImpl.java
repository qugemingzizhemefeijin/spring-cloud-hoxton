package com.atguigu.springcloud.alibaba.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.atguigu.springcloud.alibaba.dao.OrderDao;
import com.atguigu.springcloud.alibaba.domain.Order;
import com.atguigu.springcloud.alibaba.service.AccountService;
import com.atguigu.springcloud.alibaba.service.OrderService;
import com.atguigu.springcloud.alibaba.service.StorageService;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class OrderServiceImpl implements OrderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private StorageService storageService;
	
	@Resource
	private AccountService accountService;

	/**
	 * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态 简单说：下订单->扣库存->减余额->改状态
	 */
	@Override
	@GlobalTransactional(name = "cg-create-order", rollbackFor = Exception.class)
	public void create(Order order) {
		LOGGER.info("----->开始新建订单");
		// 1 新建订单
		orderDao.create(order);

		// 2 扣减库存
		LOGGER.info("----->订单微服务开始调用库存，做扣减Count");
		storageService.decrease(order.getProductId(), order.getCount());
		LOGGER.info("----->订单微服务开始调用库存，做扣减end");

		// 3 扣减账户
		LOGGER.info("----->订单微服务开始调用账户，做扣减Money");
		accountService.decrease(order.getUserId(), order.getMoney());
		LOGGER.info("----->订单微服务开始调用账户，做扣减end");
		
		int age = 10/0;
		
		// 4 修改订单状态，从零到1,1代表已经完成
		LOGGER.info("----->修改订单状态开始");
		orderDao.update(order.getUserId(), 0);
		LOGGER.info("----->修改订单状态结束");

		LOGGER.info("----->下订单结束了，O(∩_∩)O哈哈~");

	}
}
