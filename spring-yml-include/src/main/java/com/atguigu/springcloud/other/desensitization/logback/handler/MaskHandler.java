package com.atguigu.springcloud.other.desensitization.logback.handler;

public interface MaskHandler {

    // 正则匹配方式
    String regex(String str);

    // 关键字匹配方式
    String keyword(String str);

}
