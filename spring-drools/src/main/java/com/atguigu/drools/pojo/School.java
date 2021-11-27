package com.atguigu.drools.pojo;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 学校
 */
@Data
public class School {

    private String className;
    private String classCount;
    private String[] classNameArray;
    private List classNameList;
    private Set classNameSet;
    private Map classNameMap;

}
