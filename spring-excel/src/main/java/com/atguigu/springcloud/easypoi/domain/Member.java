package com.atguigu.springcloud.easypoi.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.*;

import java.util.Date;

/**
 * 会员
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Member {

    @Excel(name = "ID", width = 10)
    private Long id;

    @Excel(name = "用户名", width = 20, needMerge = true)
    private String userName;

    private String password;

    @Excel(name = "昵称", width = 20, needMerge = true)
    private String nickName;

    @Excel(name = "出生日期", width = 20, format = "yyyy-MM-dd")
    private Date birthday;

    @Excel(name = "手机号", width = 20, needMerge = true, desensitizationRule = "3_4")
    private String phone;

    private String icon;

    @Excel(name = "性别", width = 10, replace = {"男_0", "女_1"})
    private Integer gender;

}
