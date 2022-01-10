package com.atguigu.springcloud.configuration;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Md5Verify {

    /**
     * 是否启用验签功能，默认验签
     */
    boolean verifySign() default true;

}
