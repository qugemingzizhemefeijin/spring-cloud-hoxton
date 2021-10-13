package com.mzt.logapi.starter.support.aop;

import org.springframework.aop.ClassFilter;

/**
 *
 * ClassFilter与MethodMatcher（实际就是 LogRecordPointcut 继承的 StaticMethodMatcherPointcut）分别用于在不同的级别上限定Joinpoint的匹配范围，满足不同粒度的匹配。<br>
 * <ol>
 *     <li>ClassFilter限定在类级别上，MethodMatcher限定在方法级别上</li>
 *     <li>但是SpringAop主要支持在方法级别上的匹配，所以对类级别的匹配支持相对简单一些</li>
 * </ol>
 * 当传入的clazz与Pointcut规定的类型一致时，则返回true，否则返回false,返回为true时，则表示对这个类进行植入操作，
 * 当类型对Joinpoint的匹配不产生影响的时候，可以让Pointcut接口中的ClassFilter.getClassFilter()方法直接返回TrueClassFilter.INSTANCE,则表示对系统中的所有对象进行Joinpoint匹配；<br>
 *
 */
public class LogRecordClassFilter implements ClassFilter {

    private String[] basePackages;

    @Override
    public boolean matches(Class<?> clazz) {
        String[] bs = this.basePackages;
        if(bs == null || bs.length == 0) {
            return true;
        }
        for(String packages : bs) {
            if(clazz.getName().startsWith(packages)) {
                return true;
            }
        }
        return false;
    }

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }
}
