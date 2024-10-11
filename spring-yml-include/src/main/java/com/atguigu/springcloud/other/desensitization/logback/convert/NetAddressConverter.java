package com.atguigu.springcloud.other.desensitization.logback.convert;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.net.NetUtil;

public class NetAddressConverter extends ClassicConverter {

    public String convert(ILoggingEvent event) {
        return NetUtil.getLocalhost().getHostAddress();
    }
}
