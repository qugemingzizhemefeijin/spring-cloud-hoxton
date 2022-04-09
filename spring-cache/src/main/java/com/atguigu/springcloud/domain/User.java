package com.atguigu.springcloud.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1335596807697122321L;

    private long id;

    private String username;

    private String password;

}
