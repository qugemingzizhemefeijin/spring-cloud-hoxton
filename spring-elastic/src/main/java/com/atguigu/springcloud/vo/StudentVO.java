package com.atguigu.springcloud.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StudentVO implements Serializable {

    private static final long serialVersionUID = 7219443849909691125L;

    private String name;//姓名
    private Integer age ;//年龄
    private String sex ;//性别
    private String grade ;//年级
    private String subject ;//科目
    private Integer score ;//分数
    private Integer offset;
    private Integer pageSize;

}
