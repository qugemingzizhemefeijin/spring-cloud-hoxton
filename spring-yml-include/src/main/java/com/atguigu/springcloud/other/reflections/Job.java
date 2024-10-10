package com.atguigu.springcloud.other.reflections;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Job {

    String jobName() default "";

    int retry() default 3;

}
