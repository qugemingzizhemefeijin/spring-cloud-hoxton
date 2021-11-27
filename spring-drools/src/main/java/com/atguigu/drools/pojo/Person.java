package com.atguigu.drools.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 学生
 */
@Data
public class Person {

    private String name;//姓名
    private int age;//年龄
    private String className;//所在班级
    private School school;
    private Double dous;

    private Date date;

    private Date ldate;

}
