package com.atguigu.springcloud.webflux.core;

import com.atguigu.springcloud.webflux.dao.entity.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (Objects.equals(user.getUserName(),"xxx")){
            errors.rejectValue("userName","","用户名不能为xxx");
        }
    }
}
