package com.atguigu.springcloud.jasypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class JasyptTestBean implements InitializingBean {

    @Value("${ceshi.name}")
    private String name;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("jasypt ====================>>>>>>>>>>>>> name = {}", name);
    }

}
