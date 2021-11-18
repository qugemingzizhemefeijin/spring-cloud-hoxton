package com.atguigu.springcloud.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.jmx.RingBufferAdminMBean;
import org.springframework.util.StringUtils;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Slf4j
public class Log4j2RingBufferCapacityMonitor {

    private final String cfgName;

    private final String mbeanName;

    public Log4j2RingBufferCapacityMonitor(String ctxName, String cfgName) {
        // 针对 RootLogger，它的 cfgName 是空字符串，为了显示好看，我们将它命名为 root
        this.cfgName = StringUtils.isEmpty(cfgName) ? "root" : cfgName;
        //按照 Log4j2 源码中的命名方式组装名称
        this.mbeanName = String.format(RingBufferAdminMBean.PATTERN_ASYNC_LOGGER_CONFIG, ctxName, cfgName);
    }

    public void monitor() {
        try {
            // 获取剩余大小，注意这个是严格区分大小写的
            Number v = (Number) ManagementFactory.getPlatformMBeanServer()
                .getAttribute(new ObjectName(mbeanName), "RemainingCapacity");

            log.info("{}_logger_ring_buffer_remaining_capacity {}", cfgName, v.longValue());
        } catch (Exception e) {
            log.error("get {} ring buffer remaining size error", cfgName, e);
        }
    }

}
