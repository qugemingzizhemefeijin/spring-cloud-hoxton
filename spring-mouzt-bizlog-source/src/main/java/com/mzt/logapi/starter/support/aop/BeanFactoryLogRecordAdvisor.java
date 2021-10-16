package com.mzt.logapi.starter.support.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 *
 * 如果想对一个bean中的特定方法进行切面编程，而不是所有的方法，就需要设置pointcut了，pointcut允许拦截一个方法通过 方法名 ，一个 pointcut必须和一个advisor想关联。<br><br>
 *
 * <p>advice在方法执行前(before)后(after)做出相应的响应。通常是定义一些实现接口的类，然后实现相应的方法，比如：
 * before对应的实现MethodBeforeAdvice接口，
 * after对应的实现AfterReturningAdvice，
 * around对应的实现MethodInterceptor接口，
 * after throwing对应的实现ThrowsAdvice接口。
 *
 * <pre>
 * public interface Advisor {
        //@since 5.0 Spring5以后才有的  空通知  一般当作默认值
        Advice EMPTY_ADVICE = new Advice() {};

        // 该Advisor 持有的通知器
        Advice getAdvice();
        // 这个有点意思：Spring所有的实现类都是return true(官方说暂时还没有应用到)
        // 注意：生成的Advisor是单例还是多例不由isPerInstance()的返回结果决定，而由自己在定义bean的时候控制
        // 理解：和类共享（per-class）或基于实例（per-instance）相关  类共享：类比静态变量   实例共享：类比实例变量
        boolean isPerInstance();
  }
 * </pre>
 *
 * 它的继承体系主要有如下两个，<br>
 * PointcutAdvisor和IntroductionAdvisor，IntroductionAdvisor与PointcutAdvisor最本质上的区别就是：<br>
 * IntroductionAdvisor只能应用于类级别的拦截，只能使用Introduction型的Advice。<br>
 * 而不能像PointcutAdvisor那样，可以使用任何类型的Pointcut，以及几乎任何类型的Advice。<br><br>
 *
 * <a href="https://blog.csdn.net/f641385712/article/details/89178421">https://blog.csdn.net/f641385712/article/details/89178421</a><br>
 * <a href="https://cloud.tencent.com/developer/article/1497782">https://cloud.tencent.com/developer/article/1497782</a>
 *
 */
public class BeanFactoryLogRecordAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final LogRecordPointcut pointcut = new LogRecordPointcut();

    private LogRecordOperationSource logRecordOperationSource;

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
        pointcut.setLogRecordOperationSource(logRecordOperationSource);
    }

}
