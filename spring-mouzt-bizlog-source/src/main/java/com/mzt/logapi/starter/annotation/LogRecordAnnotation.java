package com.mzt.logapi.starter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {

    String success();

    String fail() default "";

    String operator() default "";

    String prefix();

    String bizNo();

    String category() default "";

    String detail() default "";

    String condition() default "";

}
