package com.atguigu.springcloud.wx;

public class WxErrorException extends Exception {

    private static final long serialVersionUID = 2950080679395731597L;

    /**
     * Constructs a <code>WxErrorException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public WxErrorException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>WxErrorException</code> with the specified message and
     * root cause.
     *
     * @param msg the detail message
     * @param t   root cause
     */
    public WxErrorException(String msg, Throwable t) {
        super(msg, t);
    }

}
