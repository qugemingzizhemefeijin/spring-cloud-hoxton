package com.mzt.logapi.service;

/**
 * 函数接口
 */
public interface IParseFunction {

    /**
     * 是否在被拦截的函数调用前就计算好值，常用于打印前后值来使用
     * @return boolean
     */
    default boolean executeBefore(){
        return false;
    }

    String functionName();

    String apply(String value);

}
