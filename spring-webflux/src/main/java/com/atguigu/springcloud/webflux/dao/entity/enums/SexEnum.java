package com.atguigu.springcloud.webflux.dao.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum SexEnum {

    MALE(1,"男"),
    FEMALE(2,"女");

    private final int code;
    private final String title;


    public static SexEnum getSexEnum(int code){
       return Arrays.stream(SexEnum.values()).filter(item-> Objects.equals(code,item.getCode()))
                .findFirst()
                .orElse(null);
    }
}
