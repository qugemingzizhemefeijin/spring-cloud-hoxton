package com.mzt.logapi.starter.support.aop;

import com.mzt.logapi.beans.LogRecordOps;
import com.mzt.logapi.starter.annotation.LogRecordAnnotation;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 这个类就是专门用于解析 LogRecordAnnotation 注解使用的。
 */
public class LogRecordOperationSource {

    public Collection<LogRecordOps> computeLogRecordOperations(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // 该方法可能在一个接口上，但我们需要来自目标类的属性。
        // 如果目标类为空，则方法不变。
        // 说白了就是可能method对象是interface的方法，可以通过targetClass来查找到其实现类的method
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // 如果是个交接方法，则找到真正的那个实现的方法
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        return parseLogRecordAnnotations(specificMethod);
    }

    /**
     * 解析方法上的 LogRecordAnnotation 注解
     * @param ae 被代理的目标方法
     * @return LogRecordAnnotation 注解 经过解析后 转换成的 LogRecordOps集合
     */
    private Collection<LogRecordOps> parseLogRecordAnnotations(AnnotatedElement ae) {
        // MergedAnnotations内部定义了一个枚举类SearchStrategy，表示查找注解的搜索策略。

        // 1.DIRECT：仅查找直接声明的注释，而无需考虑@Inherited注释，也无需搜索超类或已实现的接口。
        // 2.INHERITED_ANNOTATIONS：查找所有直接声明的注解以及任何@Inherited超类注解。 该策略仅在与Class类型一起使用时才真正有用，
        // 因为所有其他带注解的元素都将忽略@Inherited注释。 此策略不搜索已实现的接口。
        // 3.SUPERCLASS：查找所有直接声明的注解和超类注解。 该策略与INHERITED_ANNOTATIONS相似，不同之处在于注解不需要使用@Inherited进行元注解。 此策略不搜索已实现的接口。
        // 4.TYPE_HIERARCHY：对整个类型层次进行完整搜索，包括超类和已实现的接口。 超类注解不需要使用@Inherited进行元注解。
        // 5.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES：对来源和所有封闭的类执行整个类型层次结构的完整搜索。
        // 该策略与TYPE_HIERARCHY相似，不同之处在于还搜索了封闭类。 超类注解不需要使用@Inherited进行元注解。 搜索方法源时，此策略与TYPE_HIERARCHY相同。

        // AnnotationFilter用于过滤调用指定注解类型的接口，内部定义了4个常用的AnnotationFilter对象的静态变量：
        // 1.PLAIN：与java.lang和org.springframework.lang包及其子包中的注解匹配。
        // 2.JAVA：与java和javax包及其子包中的注解匹配。
        // 3.ALL：始终匹配，可以在根本不存在任何相关注释类型时使用。
        // 4.NONE：永远不匹配，可以在不需要过滤时使用（允许存在任何注释类型）。

        // findMergedAnnotation 方法可一次性找出父类和接口、父类方法和接口方法上的注解，对应 SearchStrategy.TYPE_HIERARCHY
        // getAllMergedAnnotations 对应 SearchStrategy.INHERITED_ANNOTATIONS
        Collection<LogRecordAnnotation> logRecordAnnotationAnnotations = AnnotatedElementUtils.getAllMergedAnnotations(ae, LogRecordAnnotation.class);
        Collection<LogRecordOps> ret = null;
        if (!logRecordAnnotationAnnotations.isEmpty()) {
            ret = lazyInit(ret);
            for (LogRecordAnnotation recordAnnotation : logRecordAnnotationAnnotations) {
                // 解析 LogRecordAnnotation 注解为 LogRecordOps对象
                ret.add(parseLogRecordAnnotation(ae, recordAnnotation));
            }
        }
        return ret;
    }

    /**
     * 解析 LogRecordAnnotation 注解为 LogRecordOps对象
     * @param ae               实际的方法
     * @param recordAnnotation LogRecordAnnotation注解
     * @return LogRecordOps
     */
    private LogRecordOps parseLogRecordAnnotation(AnnotatedElement ae, LogRecordAnnotation recordAnnotation) {
        LogRecordOps recordOps = LogRecordOps.builder()
                .successLogTemplate(recordAnnotation.success())
                .failLogTemplate(recordAnnotation.fail())
                .bizKey(recordAnnotation.prefix().concat("_").concat(recordAnnotation.bizNo()))
                .bizNo(recordAnnotation.bizNo())
                .operatorId(recordAnnotation.operator())
                .category(StringUtils.isEmpty(recordAnnotation.category()) ? recordAnnotation.prefix() : recordAnnotation.category())
                .detail(recordAnnotation.detail())
                .condition(recordAnnotation.condition())
                .build();
        // 如果注解没有添加 success 模块并且也没有添加 fail 模板，则抛出异常。
        validateLogRecordOperation(ae, recordOps);
        return recordOps;
    }

    private void validateLogRecordOperation(AnnotatedElement ae, LogRecordOps recordOps) {
        if (!StringUtils.hasText(recordOps.getSuccessLogTemplate()) && !StringUtils.hasText(recordOps.getFailLogTemplate())) {
            throw new IllegalStateException("Invalid logRecord annotation configuration on '" +
                    ae.toString() + "'. 'one of successTemplate and failLogTemplate' attribute must be set.");
        }
    }

    /**
     * 如果ops为空，则初始化为ArrayList，否则直接返回
     * @param ops Collection<LogRecordOps>
     * @return Collection<LogRecordOps>
     */
    private Collection<LogRecordOps> lazyInit(Collection<LogRecordOps> ops) {
        return (ops != null ? ops : new ArrayList<>(1));
    }

}
