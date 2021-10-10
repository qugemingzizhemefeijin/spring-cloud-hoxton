package com.mzt.logapi.service.impl;

import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    @Override
    public void record(LogRecord logRecord) {
        log.info("【logRecord】log={}", logRecord);
    }

    @Override
    public List<LogRecord> queryLog(String bizKey) {
        return Collections.emptyList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo) {
        return Collections.emptyList();
    }

}
