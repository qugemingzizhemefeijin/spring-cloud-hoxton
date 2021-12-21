package com.atguigu.springcloud.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomAppenderLoggerService {

    public void printLog(String message) {
        log.error(message);
    }

}
