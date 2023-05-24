package com.atguigu.springcloud.test.tale.exception;

public class TaleException extends RuntimeException {

    private static final long serialVersionUID = 3097316782371593653L;

    public TaleException(String message) {
        super(message);
    }

}
