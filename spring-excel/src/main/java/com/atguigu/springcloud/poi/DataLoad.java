package com.atguigu.springcloud.poi;

import java.util.List;

@FunctionalInterface
public interface DataLoad<T> {

    List<T> load(int start , int pagesize);

}
