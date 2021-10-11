package com.mzt.logapi.starter.support.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
class MethodExecuteResult {

    private boolean success;
    private Throwable throwable;
    private String errorMsg;

}
