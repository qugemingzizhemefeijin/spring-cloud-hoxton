package com.atguigu.springcloud.service;

import com.atguigu.springcloud.domain.User;

public interface IUserService {

    User getById(long id);

    void update(User user);

    void delete(long id);

    void reload();

}
