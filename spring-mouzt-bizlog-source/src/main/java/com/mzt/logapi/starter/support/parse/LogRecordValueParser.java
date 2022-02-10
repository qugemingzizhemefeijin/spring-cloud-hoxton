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

    /**
     * 计算方法执行后的模版值
     * @param templates                      模版集合
     * @param ret                            方法执行后的返回值
     * @param targetClass                    真正方法的目标Class
     * @param method                         被拦截的方法
     * @param args                           方法传入的参数
     * @param errorMsg                       方法的异常信息描述
     * @param beforeFunctionNameAndReturnMap 前置执行函数的函数与执行结果映射关系，key=函数名，value=计算结果
     * @return Map<String, String> key=模版字符,value=解析后的模版字符
     */
    public Map<String, String> processTemplate(Collection<String> templates, Object ret,
                                               Class<?> targetClass, Method method, Object[] args, String errorMsg,
                                               Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> expressionValues = new HashMap<>();
        // 创建一个模板表达式变量保持的上下文对象
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);
        // 循环解析模版
        for (String expressionTemplate : templates) {
            // 模版必须包含 { ，否则不予解析
            if (expressionTemplate.contains("{")) {
                // 从模版中解析出 待计算的表达式
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    // 待计算的表达式
                    String expression = matcher.group(2);
                    // 方法描述，主要用于从缓存中快速获取解析过的表达式对象
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    // 获取表达式计算后的结果值
                    String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    // 返回表达式计算后的最终结果，包括前置函数替换，后置函数计算，无函数则直接返回value
                    String functionReturnValue = getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, matcher.group(1), expression);
                    // 对当前匹配到的值进行替换并将此值到上一次匹配之间的字符追加到parsedStr
                    matcher.appendReplacement(parsedStr, Strings.nullToEmpty(functionReturnValue));
                }
                // 最后调用appendTail()方法将最后一次匹配后的剩余字符串加到parsedStr
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }
        }
        return expressionValues;
    }

    /**
     * 如果是前置函数，则直接返回前置函数计算后的结果，否则函数名称为空则直接返回value，否则再经过函数计算一道
     * @param beforeFunctionNameAndReturnMap 存储前置函数计算的返回值，key=函数名，value=计算结果
     * @param value                          表达式计算后的值
     * @param functionName                   自定义函数名称
     * @param expression                     表达式字符串，用于前置函数的key匹配
     * @return String 返回表达式计算后的最终结果
     */
    private String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, String value, String functionName, String expression) {
        String functionReturnValue = "";
        if(beforeFunctionNameAndReturnMap != null){
            functionReturnValue = beforeFunctionNameAndReturnMap.get(getFunctionCallInstanceKey(functionName, expression));
        }
        if(StringUtils.isEmpty(functionReturnValue)){
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }

    /**
     * 方法执行之前换成函数的结果，此时函数调用的唯一标志：函数名+参数
     */
    public String getFunctionCallInstanceKey(String functionName, String expression) {
        return functionName + expression;
    }

    /**
     * 传入所有的模板，并对需要进行方法调用前计算的函数进行求值
     * @param templates   模板集合（都是成功的模板，失败模板不可能会被前置计算）
     * @param targetClass 注解方法所属目标对象的Class
     * @param method      被拦截的方法
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
                        String functionReturnValue = getFunctionReturnValue(null, value, functionName, expression);
                        // 这里可能不能简单的使用函数名来存储前置函数计算的结果（否则传入不同参数但是获取的值确实一个。这里应该要加上参数的名称信息来标记唯一）
                        functionNameAndReturnValueMap.put(getFunctionCallInstanceKey(functionName, expression), functionReturnValue);
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

    /**
     * 设置自定义函数的操作类（里面实际调用的函数工厂）
     * @param functionService IFunctionService
     */
    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }

}
