package com.mzt.logapi.starter.configuration;

import com.mzt.logapi.service.IFunctionService;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.service.IOperatorGetService;
import com.mzt.logapi.service.IParseFunction;
import com.mzt.logapi.service.impl.*;
import com.mzt.logapi.starter.annotation.EnableLogRecord;
import com.mzt.logapi.starter.support.aop.BeanFactoryLogRecordAdvisor;
import com.mzt.logapi.starter.support.aop.LogRecordClassFilter;
import com.mzt.logapi.starter.support.aop.LogRecordInterceptor;
import com.mzt.logapi.starter.support.aop.LogRecordOperationSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * 在Spring4.2以后，@Import还支持导入普通的没有@Configuration注解的类，并将其实例化加入IOC容器中。<br>
 * ImportAware接口的setImportMetadata方法,然后通过其metadata拿到了@EnableLogRecord注解，这样就实现了通过注解参数来对配置类作设置的功能。<br>
 * 去除@Configuration注解，则ImportAware失效，无法触发setImportMetadata方法的调用。
 */
@Slf4j
@Configuration
public class LogRecordProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableLogRecord;

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordOperationSource logRecordOperationSource() {
        return new LogRecordOperationSource();
    }

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    @ConditionalOnMissingBean(IParseFunction.class)
    public DefaultParseFunction parseFunction() {
        return new DefaultParseFunction();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLogRecordAdvisor logRecordAdvisor(IFunctionService functionService) {
        BeanFactoryLogRecordAdvisor advisor = new BeanFactoryLogRecordAdvisor();
        advisor.setLogRecordOperationSource(logRecordOperationSource());
        advisor.setAdvice(logRecordInterceptor(functionService));
        advisor.setClassFilter(logRecordClassFilter());
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordClassFilter logRecordClassFilter() {
        LogRecordClassFilter filter = new LogRecordClassFilter();
        if(enableLogRecord != null) {
            filter.setBasePackages(enableLogRecord.getStringArray("value"));
        }
        return filter;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordInterceptor logRecordInterceptor(IFunctionService functionService) {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordOperationSource(logRecordOperationSource());
        interceptor.setFunctionService(functionService);
        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean(IOperatorGetService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IOperatorGetService operatorGetService() {
        return new DefaultOperatorGetServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService recordService() {
        return new DefaultLogRecordServiceImpl();
    }

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
