package com.atguigu.springcloud.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@AllArgsConstructor
public class MyWebConfig implements WebMvcConfigurer {

    private final Md5VerifyArgumentResolver md5VerifyArgumentResolver;

    /**
     * 将自定义的方法参数解析器加入到配置类中
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(md5VerifyArgumentResolver);
    }

    // 调用HandlerMethodArgumentResolver的supportsParameter方法查看是否支持解析这个参数类型
    // 由于spring boot 的 RequestResponseBodyMethodProcessor（@RequestBody的解析器）在我们的解析器之前，所以导致spring boot 使用的还是默认解析器
    // 于是解决方法就简单了，去除接口中的@RequestBody注解，可以取出@RestController
    //@Override
    //public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
    //    handlers.add(resultBodyReturnValueHandler);
    //}

}
