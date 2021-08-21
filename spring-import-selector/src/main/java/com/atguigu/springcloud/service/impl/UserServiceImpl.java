package com.atguigu.springcloud.service.impl;

import com.atguigu.springcloud.service.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public void register(String name) {
        System.out.println("user => " + name + " register~");
    }

}
