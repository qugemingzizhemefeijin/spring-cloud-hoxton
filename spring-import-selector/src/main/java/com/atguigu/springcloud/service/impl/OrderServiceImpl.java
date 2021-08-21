package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.OrderService;

public class OrderServiceImpl implements OrderService {

    @Override
    public void print(String s) {
        System.out.println("Hello => " + s);
    }

}
