package com.atguigu.springcloud.other.desensitization.logback;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpFactUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpFactUtils.ctx = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (ctx == null) {
            return null;
        }
        return ctx.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        if (ctx == null) {
            return null;
        }
        return ctx.getBean(name, clazz);
    }

}
