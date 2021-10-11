package com.mzt.logapi.beans;

import lombok.Builder;
import lombok.Data;

/**
 * 映射的是 LogRecordAnnotation 注解信息
 */
@Data
@Builder
public class LogRecordOps {

    private String successLogTemplate;
    private String failLogTemplate;
    private String operatorId;
    private String bizKey;
    private String bizNo;
    private String category;
    private String detail;
    private String condition;

}
