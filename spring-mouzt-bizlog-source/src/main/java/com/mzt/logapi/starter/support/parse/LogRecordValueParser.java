package com.mzt.logapi.starter.support.parse;

import com.google.common.base.Strings;
import com.mzt.logapi.service.IFunctionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogRecordValueParser implements BeanFactoryAware {

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    protected BeanFactory beanFactory;

    private IFunctionService functionService;

    private final LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();

    public Map<String, String> processTemplate(Collection<String> templates, Object ret,
                                               Class<?> targetClass, Method method, Object[] args, String errorMsg,
                                               Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    String functionReturnValue = getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, matcher.group(1));
                    matcher.appendReplacement(parsedStr, Strings.nullToEmpty(functionReturnValue));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }

        }
        return expressionValues;
    }

    private String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, String value, String functionName) {
        String functionReturnValue = "";
        if(beforeFunctionNameAndReturnMap != null){
            functionReturnValue = beforeFunctionNameAndReturnMap.get(functionName);
        }
        if(StringUtils.isEmpty(functionReturnValue)){
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }

    /**
     * 传入所有的模板，并对需要进行方法调用前计算的函数进行求值
     * @param templates   模板集合（都是成功的模板，失败模板不可能会被前置计算）
     * @param targetClass 注解方法所属目标对象的Class
     * @param method      注解方法
     * @param args        注解方法传入的参数信息
     * @return Map<String, String> key=函数名，value=计算结果
     */
    public Map<String, String> processBeforeExecuteFunctionTemplate(Collection<String> templates, Class<?> targetClass, Method method, Object[] args) {
        Map<String, String> functionNameAndReturnValueMap = new HashMap<>();
        // 创建一个模板表达式变量保持的上下文对象
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, null, null, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#_errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String functionName = matcher.group(1);
                    if (functionService.beforeFunction(functionName)) {
                        String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        String functionReturnValue = getFunctionReturnValue(null, value, functionName);
                        functionNameAndReturnValueMap.put(functionName, functionReturnValue);
                    }
                }
            }
        }
        return functionNameAndReturnValueMap;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }

}
