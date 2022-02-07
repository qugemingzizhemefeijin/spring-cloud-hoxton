package com.atguigu.springcloud.poi.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Member implements Serializable {

    private static final long serialVersionUID = -7058945590447193041L;

    private Long id;

    private String userName;

    private String password;

    private String nickName;

    private Date birthday;

    private String phone;

    private String icon;

    private Integer gender;

}
