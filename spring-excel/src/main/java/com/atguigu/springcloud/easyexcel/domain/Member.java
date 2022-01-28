package com.atguigu.springcloud.easyexcel.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.atguigu.springcloud.easyexcel.GenderConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Member implements Serializable {

    private static final long serialVersionUID = -5058945590447193041L;

    @ExcelProperty("ID")
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String userName;

    @ExcelIgnore
    private String password;

    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickName;

    @ExcelProperty("出生日期")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    private Date birthday;

    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String phone;

    @ExcelIgnore
    private String icon;

    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    @ColumnWidth(10)
    private Integer gender;

}
