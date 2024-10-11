package com.atguigu.springcloud.other.desensitization.logback.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class ModuleConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return event.getLoggerName().length() > 20 ? "" : event.getLoggerName();
    }

}
