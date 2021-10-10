package com.mzt.logapi.service;

public interface IFunctionService {

    String apply(String functionName, String value);

    default boolean beforeFunction(String functionName) {
        return true;
    }

}
