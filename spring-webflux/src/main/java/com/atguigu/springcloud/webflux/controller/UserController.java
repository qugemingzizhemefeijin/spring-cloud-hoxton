package com.atguigu.springcloud.webflux.controller;

import com.atguigu.springcloud.webflux.dao.entity.User;
import com.atguigu.springcloud.webflux.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/user/{id}")
    public Mono<User> getUser(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @GetMapping("/user/{userName}/{note}")
    public Flux<User> getUser(@PathVariable("userName") String userName, @PathVariable("note") String note){
        return userService.findByUserNameAndNote(userName,note);
    }

    @GetMapping("/user/insert/{user}")
    public Mono<User> insertUser(@Valid @PathVariable("user") User user){
        return userService.insertUser(user);
    }

    // 可以在本地注册 Validator 实现
//    @InitBinder
//    public void initBinder(DataBinder binder){
//        binder.setValidator(new UserValidator());
//    }
}
