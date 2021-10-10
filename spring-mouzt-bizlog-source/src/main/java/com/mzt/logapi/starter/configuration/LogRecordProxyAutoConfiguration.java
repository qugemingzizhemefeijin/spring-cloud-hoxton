package com.mzt.logapi.starter.configuration;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 在Spring4.2以后，@Import还支持导入普通的没有@Configuration注解的类，并将其实例化加入IOC容器中。<br>
 * ImportAware接口的setImportMetadata方法,然后通过其metadata拿到了@EnableRedissonHttpSession注解上的这两个参数，这样就实现了通过注解参数来对配置类作设置的功能。<br>
 * 去除@Configuration注解，则ImportAware失效，无法触发setImportMetadata方法的调用。
 */
@Slf4j
@Configuration
public class LogRecordProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableLogRecord;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        // classValuesAsString 是否将类引用转换为String 在返回的 Map 中作为值暴露的类名，而不是 Class 可能需要先加载的引用
        this.enableLogRecord = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
        if (this.enableLogRecord == null) {
            log.info("@EnableCaching is not present on importing class");
        }
    }

}
