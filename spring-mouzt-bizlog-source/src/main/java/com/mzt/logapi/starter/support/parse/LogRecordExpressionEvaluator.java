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

    private final Map<ExpressionKey, Expression> expressionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    public String parseExpression(String conditionExpression, AnnotatedElementKey methodKey, EvaluationContext evalContext) {
        Object value = getExpression(this.expressionCache, methodKey, conditionExpression).getValue(evalContext, Object.class);
        return value == null ? "" : value.toString();
    }

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
