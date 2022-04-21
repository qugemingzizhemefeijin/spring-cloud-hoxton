package com.atguigu.springcloud.xcache;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cached {

    String cacheName();

    String key();    //支持springEl表达式

    long l2TimeOut() default 120;

    CacheType type() default CacheType.FULL;

}
