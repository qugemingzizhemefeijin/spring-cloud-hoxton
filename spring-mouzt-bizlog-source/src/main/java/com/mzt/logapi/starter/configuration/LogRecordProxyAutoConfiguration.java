package com.mzt.logapi.starter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 在Spring4.2以后，@Import还支持导入普通的没有@Configuration注解的类，并将其实例化加入IOC容器中。<br>
 * ImportAware接口的setImportMetadata方法,然后通过其metadata拿到了@EnableRedissonHttpSession注解上的这两个参数，这样就实现了通过注解参数来对配置类作设置的功能。<br>
 * 去除@Configuration注解，则ImportAware失效，无法触发setImportMetadata方法的调用。
 */
@Slf4j
@Configuration
public class LogRecordProxyAutoConfiguration implements ImportAware {

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {

    }

}
