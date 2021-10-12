package com.mzt.logapi.starter.support.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 方法执行的结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
class MethodExecuteResult {

    // 是否成功
    private boolean success;

    // 异常对象
    private Throwable throwable;

    // 错误信息
    private String errorMsg;

}
