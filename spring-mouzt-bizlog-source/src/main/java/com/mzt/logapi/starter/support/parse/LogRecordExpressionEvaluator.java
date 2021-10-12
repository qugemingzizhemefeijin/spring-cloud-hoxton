package com.mzt.logapi.starter.support.parse;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CachedExpressionEvaluator 是用于计算缓存注解上 SpEL 表达式值的工具类
 */
public class LogRecordExpressionEvaluator extends CachedExpressionEvaluator {

    /**
     * 这里主动是提供给 CachedExpressionEvaluator 用于缓存表达式解析后的Expression对象的容器（不然每次遇到都要进行解析一遍，耗损性能）
     */
    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    /**
     * 用于存储被代理方法和目标Class指向的实际目标方法
     */
    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * 计算表达式并返回结果
     * @param conditionExpression 表达式字符串
     * @param methodKey           方法和类描述key，适合做映射关系的key
     * @param evalContext         模板表达式变量保持的上下文对象
     * @return String 解析后的结果值
     */
    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        Object value = getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, Object.class);
        return value == null ? "" : value.toString();
    }

    /**
     * 创建基于方法的表达式计算上下文对象
     * @param method      被拦截的方法
     * @param args        传入方法的参数
     * @param targetClass 真正方法的目标Class
     * @param result      方法执行后的返回值
     * @param errorMsg    方法执行后抛出的异常信息
     * @param beanFactory BeanFactory（实际就是Spring容器）
     * @return EvaluationContext
     */
    public EvaluationContext createEvaluationContext(Method method, Object[] args, Class<?> targetClass,
                                                     Object result, String errorMsg, BeanFactory beanFactory) {
        // 获取真正的调用方法
        Method targetMethod = getTargetMethod(targetClass, method);
        // 创建基于方法的表达式计算上下文
        LogRecordEvaluationContext evaluationContext = new LogRecordEvaluationContext(
                null, targetMethod, args, getParameterNameDiscoverer(), result, errorMsg);
        if (beanFactory != null) {
            // 设置bean解析器，这样就可以在模板中支持spring维护的bean中的方法了，可以使用 {{@userQueryService.getDesc()}} 这种方式执行指定的bean方法
            evaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        }
        return evaluationContext;
    }

    /**
     * 从代理对象上的一个方法，找到真实对象上对应的方法。此方法会维持缓存。
     * @param targetClass 真实的对象类型
     * @param method      被代理的方法（可能为AOP的方法）
     * @return Method
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (targetMethod == null) {
            // 从代理对象上的一个方法，找到真实对象上对应的方法
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }

}
