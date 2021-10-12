package com.mzt.logapi.starter.support.aop;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.beans.LogRecordOps;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.ILogRecordService;
import com.mzt.logapi.service.IOperatorGetService;
import com.mzt.logapi.starter.support.parse.LogRecordValueParser;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * 这个类的功能就是对AOP的方法进行拦截后，解析模板信息并对日志注解进行处理。<br><br>
 *
 * 实现MethodInterceptor 接口，在调用目标对象的方法时，就可以实现在调用方法之前、调用方法过程中、调用方法之后对其进行控制。<br><br>
 *
 * MethodInterceptor 接口可以实现MethodBeforeAdvice接口、AfterReturningAdvice接口、ThrowsAdvice接口这三个接口能够所能够实现的功能，
 * 但是应该谨慎使用MethodInterceptor 接口，很可能因为一时的疏忽忘记最重要的MethodInvocation而造成对目标对象方法调用失效，或者不能达到预期的设想。<br><br>
 *
 */
@Slf4j
public class LogRecordInterceptor extends LogRecordValueParser implements InitializingBean, MethodInterceptor, Serializable {

    // 用于对方法的注解进行解析
    private LogRecordOperationSource logRecordOperationSource;

    // 对最终解析成的日志进行处理（可以选择存储数据库/直接打印等）
    private ILogRecordService bizLogService;

    // 主要用于获取当前执行人的信息
    private IOperatorGetService operatorGetService;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 处理拦截后的方法
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    /**
     * 对aop拦截的方法进行处理
     * @param invoker 方法的执行器
     * @param target  目标对象
     * @param method  方法
     * @param args    参数
     * @return Object 方法的返回值
     * @throws Throwable
     */
    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        // 这个是获取最下层的被代理的目标类Class对象
        Class<?> targetClass = getTargetClass(target);
        Object ret = null;
        MethodExecuteResult methodExecuteResult = new MethodExecuteResult(true, null, "");
        // 这个用于存储方法内部的上线文变量，在此方法执行完成后，会直接清空。可以想象成是一个java线程栈的样子。进入一个方法申请一个栈空间，结束一个方法，销毁申请的栈空间。
        LogRecordContext.putEmptySpan();
        // 用于存储 LogRecordAnnotation 的注解转换成 LogRecordOps 的信息（使用集合是可以让方法支持多个 LogRecordAnnotation 的注解）
        Collection<LogRecordOps> operations = new ArrayList<>();
        // 这个是用于存储前缀函数计算的返回值
        Map<String, String> functionNameAndReturnMap = new HashMap<>();
        try {
            // 解析方法上的 LogRecordAnnotation 注解并且将信息存储到 Collection<LogRecordOps> 集合中
            operations = logRecordOperationSource.computeLogRecordOperations(method, targetClass);
            // 获取当前拦截方法所有的日志成功模板
            List<String> spElTemplates = getBeforeExecuteFunctionTemplate(operations);
            // 进行方法前置函数解析，并返回（返回key=函数名，value=计算结果）。这里估计有一个BUG，如果我使用的函数是一个，但是返回值不一样，好像会被覆盖掉。
            functionNameAndReturnMap = processBeforeExecuteFunctionTemplate(spElTemplates, targetClass, method, args);
        } catch (Exception e) {
            log.error("log record parse before function exception", e);
        }
        try {
            ret = invoker.proceed();
        } catch (Exception e) {
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }
        try {
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(ret, method, args, operations, targetClass,
                        methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
            }
        } catch (Exception t) {
            //记录日志错误不要影响业务
            log.error("log record parse exception", t);
        } finally {
            LogRecordContext.clear();
        }
        if (methodExecuteResult.getThrowable() != null) {
            throw methodExecuteResult.getThrowable();
        }
        return ret;
    }

    /**
     * 从对象中获取所有的成功的模板，并存储到集合中
     * @param operations 注解映射的Java对象
     * @return 返回 LogRecordAnnotation 注解的所有成功的模板
     */
    private List<String> getBeforeExecuteFunctionTemplate(Collection<LogRecordOps> operations) {
        List<String> spElTemplates = new ArrayList<>();
        for (LogRecordOps operation : operations) {
            //执行之前的函数，失败模版不解析
            List<String> templates = getSpElTemplates(operation, operation.getSuccessLogTemplate());
            if(!CollectionUtils.isEmpty(templates)){
                spElTemplates.addAll(templates);
            }
        }
        return spElTemplates;
    }

    private void recordExecute(Object ret, Method method, Object[] args, Collection<LogRecordOps> operations,
                               Class<?> targetClass, boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {
        for (LogRecordOps operation : operations) {
            try {
                String action = getActionContent(success, operation);
                if (StringUtils.isEmpty(action)) {
                    //没有日志内容则忽略
                    continue;
                }
                //获取需要解析的表达式
                List<String> spElTemplates = getSpElTemplates(operation, action);
                String operatorIdFromService = getOperatorIdFromServiceAndPutTemplate(operation, spElTemplates);

                Map<String, String> expressionValues = processTemplate(spElTemplates, ret, targetClass, method, args, errorMsg, functionNameAndReturnMap);
                if (logConditionPassed(operation.getCondition(), expressionValues)) {
                    LogRecord logRecord = LogRecord.builder()
                            .bizKey(expressionValues.get(operation.getBizKey()))
                            .bizNo(expressionValues.get(operation.getBizNo()))
                            .operator(getRealOperatorId(operation, operatorIdFromService, expressionValues))
                            .category(operation.getCategory())
                            .detail(expressionValues.get(operation.getDetail()))
                            .action(expressionValues.get(action))
                            .createTime(new Date())
                            .build();

                    //如果 action 为空，不记录日志
                    if (StringUtils.isEmpty(logRecord.getAction())) {
                        continue;
                    }
                    //save log 需要新开事务，失败日志不能因为事务回滚而丢失
                    Preconditions.checkNotNull(bizLogService, "bizLogService not init!!");
                    bizLogService.record(logRecord);
                }
            } catch (Exception t) {
                log.error("log record execute exception", t);
            }
        }
    }

    private List<String> getSpElTemplates(LogRecordOps operation, String action) {
        List<String> spElTemplates = Lists.newArrayList(operation.getBizKey(), operation.getBizNo(), action, operation.getDetail());
        if (!StringUtils.isEmpty(operation.getCondition())) {
            spElTemplates.add(operation.getCondition());
        }
        return spElTemplates;
    }

    private boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
        return StringUtils.isEmpty(condition) || StringUtils.endsWithIgnoreCase(expressionValues.get(condition), "true");
    }

    private String getRealOperatorId(LogRecordOps operation, String operatorIdFromService, Map<String, String> expressionValues) {
        return !StringUtils.isEmpty(operatorIdFromService) ? operatorIdFromService : expressionValues.get(operation.getOperatorId());
    }

    private String getOperatorIdFromServiceAndPutTemplate(LogRecordOps operation, List<String> spElTemplates) {
        String realOperatorId = "";
        if (StringUtils.isEmpty(operation.getOperatorId())) {
            realOperatorId = operatorGetService.getUser().getOperatorId();
            if (StringUtils.isEmpty(realOperatorId)) {
                throw new IllegalArgumentException("[LogRecord] operator is null");
            }
        } else {
            spElTemplates.add(operation.getOperatorId());
        }
        return realOperatorId;
    }

    private String getActionContent(boolean success, LogRecordOps operation) {
        String action = "";
        if (success) {
            action = operation.getSuccessLogTemplate();
        } else {
            action = operation.getFailLogTemplate();
        }
        return action;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        bizLogService = beanFactory.getBean(ILogRecordService.class);
        operatorGetService = beanFactory.getBean(IOperatorGetService.class);
        Preconditions.checkNotNull(bizLogService, "bizLogService not null");
    }

    /**
     * 获取aop代理真实的对象类型
     * @param target Object
     * @return Class<?>
     */
    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    public void setOperatorGetService(IOperatorGetService operatorGetService) {
        this.operatorGetService = operatorGetService;
    }

    public void setLogRecordService(ILogRecordService bizLogService) {
        this.bizLogService = bizLogService;
    }

    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

}
