package com.atguigu.springcloud.other;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(value = AopOrders.DISTRIBUTION_LOCK_ORDER)
public class DistributionLockAspect {

    private static final String ANNOTATION_DISTRIBUTION_LOCK = "@annotation(com.atguigu.springcloud.other.DistributionLock)";
    private final RedissonClient redissonClient;

    @Pointcut(ANNOTATION_DISTRIBUTION_LOCK)
    public void distributionLock() {
    }

    @Around(value = "distributionLock()")
    public Object doLock(ProceedingJoinPoint pjp) throws Throwable {
        DistributionLock distributionLock = getDistributionLock(pjp);

        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Object target = pjp.getTarget();
        Method currentMethod = target.getClass().getMethod(sig.getName(), sig.getParameterTypes());

        String className = target.getClass().getSimpleName();
        String methodName = currentMethod.getName();

        String prefix = StringUtils.isBlank(distributionLock.prefix()) ? methodName : distributionLock.prefix();
        String evalExpression = SpelUtil.generateKeyBySpel(distributionLock.key(), pjp);

        String lockKey = String.format("%s-%s-%s", className, prefix, evalExpression);
        RLock rLock = redissonClient.getLock(lockKey);
        Object ret;
        // - 如果指定了`leaseTime`，那么就不会启动`Watchdog`进行自动续期；
        // - 如果没有指定`leaseTime`，则会启动一个`Watchdog`每隔一段时间就对`redis`中的`key`进行续期。默认的时间间隔是10s；
        if (rLock.tryLock(distributionLock.waitTime(), distributionLock.leaseTime(), TimeUnit.MILLISECONDS)) {
            try {
                ret = pjp.proceed();
            } finally {
                rLock.unlock();
            }
        } else {
            String msg = String.format("class %s methodName %s,key %s occurred parallel request", className, methodName, evalExpression);
            throw new RuntimeException(msg);
        }
        return ret;
    }

    private DistributionLock getDistributionLock(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DistributionLock distributionLock = AnnotationUtils.findAnnotation(signature.getMethod(), DistributionLock.class);
        if (Objects.nonNull(distributionLock)) {
            return distributionLock;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DistributionLock.class);
    }

}
