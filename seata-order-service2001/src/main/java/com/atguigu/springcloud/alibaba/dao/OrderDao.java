package com.atguigu.springcloud.alibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.atguigu.springcloud.alibaba.domain.Order;

@Mapper
public interface OrderDao {
	
	// 1 新建订单
	void create(Order order);

	// 2 修改订单状态，从零改为1
	void update(@Param("userId") Long userId, @Param("status") Integer status);

}