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
        // 这个是用于存储前置函数计算的返回值
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
            // 执行被拦截的方法调用
            ret = invoker.proceed();
        } catch (Exception e) {
            // 记录异常信息
            methodExecuteResult = new MethodExecuteResult(false, e, e.getMessage());
        }
        try {
            // 如果方法有 LogRecordAnnotation 注解，则执行日志模版解析以及处理（包括日志打印、存储等操作）
            if (!CollectionUtils.isEmpty(operations)) {
                recordExecute(ret, method, args, operations, targetClass,
                        methodExecuteResult.isSuccess(), methodExecuteResult.getErrorMsg(), functionNameAndReturnMap);
            }
        } catch (Exception t) {
            // 记录日志错误不要影响业务
            log.error("log record parse exception", t);
        } finally {
            // 销毁维持的参数变量或者销毁本次调用需要的信息
            LogRecordContext.clear();
        }
        // 如果本次调用抛出了一场，则直接调用throw抛出
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

    /**
     * 对拦截的方法进行日志模版的解析，包括已经获取到了方法执行后的返回值，前置函数执行结果等都传入进行替换等操作。
     * @param ret                      被拦截的执行后的返回值
     * @param method                   被拦截的方法
     * @param args                     方法传入的参数
     * @param operations               LogRecordAnnotation 的注解转换成 LogRecordOps 的集合信息
     * @param targetClass              真正方法的目标Class
     * @param success                  方法是否执行成功
     * @param errorMsg                 方法的异常信息描述
     * @param functionNameAndReturnMap 存储前置函数计算的返回值
     */
    private void recordExecute(Object ret, Method method, Object[] args, Collection<LogRecordOps> operations,
                               Class<?> targetClass, boolean success, String errorMsg, Map<String, String> functionNameAndReturnMap) {
        // 循环解析 LogRecordAnnotation 注解模版信息
        for (LogRecordOps operation : operations) {
            try {
                // 获取需要执行的日志模版
                String action = getActionContent(success, operation);
                if (StringUtils.isEmpty(action)) {
                    //没有日志内容则忽略
                    continue;
                }
                // 获取需要解析的表达式
                List<String> spElTemplates = getSpElTemplates(operation, action);
                // 这里获取操作人ID（如果在 LogRecordAnnotation 注解中配置了 operator 则为此值，否则是从 IOperatorGetService 接口实现类中获取）
                String operatorIdFromService = getOperatorIdFromServiceAndPutTemplate(operation, spElTemplates);

                // 解析模版并返回模版替换后的值
                Map<String, String> expressionValues = processTemplate(spElTemplates, ret, targetClass, method, args, errorMsg, functionNameAndReturnMap);
                // 计算是否符合记录日志的条件
                if (logConditionPassed(operation.getCondition(), expressionValues)) {
                    // 通过解析表达式后生成的记录日志的对象
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
                    // 日志记录操作，可以是存储数据库/只是打印等
                    bizLogService.record(logRecord);
                }
            } catch (Exception t) {
                log.error("log record execute exception", t);
            }
        }
    }

    /**
     * 获取一个日志注解需要进行模板表达式解析的信息
     * @param operation 日志注解对象
     * @param action    成功/失败日志模板
     * @return List<String> 所有的模板集合
     */
    private List<String> getSpElTemplates(LogRecordOps operation, String action) {
        // 将需要进行模版解析的封装到List中
        List<String> spElTemplates = Lists.newArrayList(operation.getBizKey(), operation.getBizNo(), action, operation.getDetail());
        // 如果有条件的话，也将入到模版中
        if (!StringUtils.isEmpty(operation.getCondition())) {
            spElTemplates.add(operation.getCondition());
        }
        return spElTemplates;
    }

    /**
     * 判断日志记录的条件表达式是否为真
     * @param condition        条件的模版
     * @param expressionValues 完成计算的表达式值和value映射关系
     * @return boolean
     */
    private boolean logConditionPassed(String condition, Map<String, String> expressionValues) {
        return StringUtils.isEmpty(condition) || StringUtils.endsWithIgnoreCase(expressionValues.get(condition), "true");
    }

    /**
     * 获取操作人ID信息
     * @param operation             LogRecordOps
     * @param operatorIdFromService 是从 IOperatorGetService 接口实现类中获取，可能是空
     * @param expressionValues      被模版解析过的操作人ID
     * @return String
     */
    private String getRealOperatorId(LogRecordOps operation, String operatorIdFromService, Map<String, String> expressionValues) {
        return !StringUtils.isEmpty(operatorIdFromService) ? operatorIdFromService : expressionValues.get(operation.getOperatorId());
    }

    /**
     * 如果注解中操作人为空，则从服务中 IOperatorGetService 的实现类中获取操作人ID，否则将操作人ID存放到模版中
     * @param operation     LogRecordOps
     * @param spElTemplates 存储的待解析的模版
     * @return String
     */
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

    /**
     * 根据方法执行的结果获取需要解析的模版
     * @param success   方法是否被执行成功
     * @param operation LogRecordOps
     * @return String
     */
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

    /**
     * 主动设置操作人获取服务
     * @param operatorGetService IOperatorGetService
     */
    public void setOperatorGetService(IOperatorGetService operatorGetService) {
        this.operatorGetService = operatorGetService;
    }

    /**
     * 主动设置日志记录服务
     * @param bizLogService ILogRecordService
     */
    public void setLogRecordService(ILogRecordService bizLogService) {
        this.bizLogService = bizLogService;
    }

    /**
     * 主动设置注解 LogRecordAnnotation 的解析
     * @param logRecordOperationSource LogRecordOperationSource
     */
    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

}
