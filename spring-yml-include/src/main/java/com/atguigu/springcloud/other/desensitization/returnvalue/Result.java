package com.atguigu.springcloud.other.desensitization.returnvalue;

import lombok.Data;

/**
 * @Description 统一返回对象
 * @Date 2023-05-29 2:53 PM
 */
@Data
public class Result<T> {

    public static final Integer OK = 200;
    public static final Integer ERROR = 500;

    private Integer code;

    private String msg;

    private T data;

    public Result() {
        this.code = 200;
        this.msg = "请求成功";
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> res = new Result<>();
        res.setCode(code);
        res.setMsg(msg);
        return res;
    }

    public static <T> Result<T> ok() {
        return new Result<>();
    }


    public static <T> Result<T> error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }


}
