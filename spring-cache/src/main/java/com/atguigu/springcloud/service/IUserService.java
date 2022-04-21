package com.atguigu.springcloud.service;

import com.atguigu.springcloud.domain.User;

public interface IUserService {

    User getById(long id);

    User update(User user);

    void delete(long id);

    void reload();

}
