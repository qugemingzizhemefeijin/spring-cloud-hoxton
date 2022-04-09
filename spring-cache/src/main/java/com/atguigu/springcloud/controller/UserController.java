package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.domain.User;
import com.atguigu.springcloud.service.IUserService;
import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping("/get/{id}")
    public User get(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping("/update")
    public Map<String, Object> update(User user) {
        userService.update(user);

        return ImmutableMap.of("success", true);
    }

    @GetMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable long id) {
        userService.delete(id);

        return ImmutableMap.of("success", true);
    }

    @GetMapping("/reload")
    public Map<String, Object> reload() {
        userService.reload();

        return ImmutableMap.of("success", true);
    }

}
