### springboot 自定义方法参数解析器  HandlerMethodArgumentResolver , HandlerMethodReturnValueHandler 使用案例

#### 问题1

问：既然用到了自定义的注解，为什么不用切面来实现，而是使用自定义的参数解析器呢？
答：是因为`jackson`的反序列化动作优先级远高于切面的优先级，所以还没进入切面就已经报反序列化失败的错误了。又或者拦截AOP的话，会造成项目启动稍微会慢点。

#### 问题2

问：为什么在`controller`中注解`@RequestBody`不见了?

答：要回答这个问题，我们就得了解下`HandlerMethodArgumentResolverComposite`这个类了，以下简称`Composite`。

`SpringMVC`在启动时会将所有的参数解析器放到`Composite`中，`Composite`是所有参数的一个集合。

当对参数进行解析时就会从该参数解析器集合中选择一个支持对`parameter`解析的参数解析器，然后使用该解析器进行参数解析。

又因为`@RequestBody`所以使用的参数解析器`RequestResponseBodyMethodProcessor`优先级高于我们自定义的参数解析器，所以如果共用会被前者拦截解析，所以为了正常使用，我们需要将`@RequestBody`注解去掉。

```
/**
 * Find a registered {@link HandlerMethodArgumentResolver} that supports
 * the given method parameter.
 */
@Nullable
private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
    HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
    if (result == null) {
        for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                result = resolver;
                this.argumentResolverCache.put(parameter, result);
                break;
            }
        }
    }
    return result;
}
```

`HandlerMethodReturnValueHandler`的使用问题是一样的。可以用来对返回值进行再一次包装。`spring`的默认处理类是`RequestResponseBodyMethodProcessor`，它是根据判断是否有`@ResponseBody`注解来处理的
