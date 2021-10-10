package com.mzt.logapi.starter.support.aop;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;

/**
 *
 * Spring通过org.springframework.aop.Pointcut接口描述切点，Pointcut由ClassFilter和MethodMatcher构成，
 * 通过ClassFilter定位到某些特定的类，通过MethodMatcher定位到某些特定的方法。这样，Pointcut就拥有了描述某些类的某些特定方法的能力。
 * <br><br>
 *
 * ClassFilter只定义了一个方法matches(Class clazz),其参数代表一个被检测的类，该方法判别被检测的类是否匹配过滤条件。
 * <br><br>
 *
 * Spring提供了两种方法匹配器：静态方法匹配器和动态方法匹配器。
 * <ol>
 * <li>静态方法匹配器：仅对方法名签名（包括方法名和入参类型及顺序）进行匹配。静态匹配仅判断一次。</li>
 * <li>动态方法匹配器：会在运行期间检查方法入参的值。动态匹配因为每次调用方法的入参可能不一样，导致每次调用方法都必须判断，因此动态匹配对性能的影响较大。一般情况，动态匹配不常使用。</li>
 * </ol>
 *
 * <br>
 * 方法匹配器的类型由MethodMatcher接口的isRuntime()方法决定，返回false表示是静态方法匹配器，返回true表示是动态方法匹配器。
 * <br><br>
 *
 * 切点类型：
 * <ol>
 * <li>
 *     静态方法切点：org.springframework.aop.support.StaticMethodMatcherPointcut<br>
 *     StaticMethodMatcherPointcut是静态方法切点的抽象基类，默认情况下匹配所有的类。<br>
 *     StaticMethodMatcherPointcut有两个重要的子类：NameMethodMatcherPointcut和AbstractRegexMethodPoint。<br>
 *     前者提供简单的字符串匹配方法签名，后者使用正则表达式匹配方法签名。<br>
 * </li>
 * <li>
 *     动态方法切点：org.springframework.aop.support.DynamicMethodMatcherPointcut<br>
 *     DynamicMethodMatcherPointcut是动态方法切点的抽象基类，默认情况下它匹配所有的类。DynamicMethodMatcherPointcut已过时！！
 *     使用DefaultPointcutAdvisor和DynamicMethodPointcut动态方法匹配器代替。
 * </li>
 * <li>注解切点</li>
 * <li>表达式切点</li>
 * <li>流程切点</li>
 * <li>复合切点</li>
 * </ol>
 *
 * 相关知识点：<br>
 * <a href="https://www.cnblogs.com/HigginCui/p/6323131.html">https://www.cnblogs.com/HigginCui/p/6323131.html</a><br>
 * <a href="https://blog.csdn.net/fengxing_2/article/details/86607499">https://blog.csdn.net/fengxing_2/article/details/86607499</a>
 *
 * @see org.springframework.aop.support.StaticMethodMatcherPointcut
 *
 */
public class LogRecordPointcut extends StaticMethodMatcherPointcut {

    private LogRecordOperationSource logRecordOperationSource;

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return !CollectionUtils.isEmpty(logRecordOperationSource.computeLogRecordOperations(method, targetClass));
    }

    void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

}
