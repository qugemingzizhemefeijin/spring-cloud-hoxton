package com.mzt.logapi.service;

/**
 * 函数接口，可以在注解的模板信息中使用函数
 */
public interface IFunctionService {

    String apply(String functionName, String value);

    /**
     * 是否在被拦截的函数调用前就计算好值，常用于打印前后值来使用
     * @param functionName 函数名称
     * @return boolean
     */
    default boolean beforeFunction(String functionName) {
        return true;
    }

}
