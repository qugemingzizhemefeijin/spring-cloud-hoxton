package com.mzt.logapi.starter.annotation;

import com.mzt.logapi.starter.LogRecordConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogRecordConfigureSelector.class)
public @interface EnableLogRecord {

    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

    /**
     * Indicate how caching advice should be applied. The default is {@link AdviceMode#PROXY}.
     *
     * @return 代理方式
     * @see AdviceMode
     */
    AdviceMode mode() default AdviceMode.PROXY;

}
