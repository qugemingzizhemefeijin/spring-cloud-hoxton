package com.atguigu.springcloud.other.desensitization.jackson;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = -1430741955061947768L;

    @Sensitivity(strategy = SensitiveEnum.USERNAME)
    private String username;

    @Sensitivity(strategy = SensitiveEnum.PHONE)
    private String cellphone;

    @Sensitivity(strategy = SensitiveEnum.ADDRESS)
    private String address;

    @Sensitivity(strategy = SensitiveEnum.EMAIL)
    private String email;

    private Integer age;

}
