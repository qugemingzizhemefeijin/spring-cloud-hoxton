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

    public Person() {

    }

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String className) {
        this.name = name;
        this.age = age;
        this.className = className;
    }

}
