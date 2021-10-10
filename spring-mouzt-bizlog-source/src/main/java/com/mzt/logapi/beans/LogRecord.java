package com.mzt.logapi.beans;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LogRecord {

    private Integer id;
    private String bizKey;
    private String bizNo;
    private String operator;
    private String action;
    private String category;
    private Date createTime;
    private String detail;

}
