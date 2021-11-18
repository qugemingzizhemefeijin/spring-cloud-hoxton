package com.atguigu.springcloud.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
// proxyBeanMethods
// 当我们使用代理对象的时候，调用它的方法，他会检测容器中是不是有了这样的组件，如果有，则不再新建组件，直接将已经有的组件返回。如果说没有的话，才会新建组件。
// 这样保证了容器中的组件始终就保持单一性。不过这也有一个不好的地方，那就是每次都要检测，会降低速度。

// 当不是代理对象的时候，则不会检测，直接创建新的组件了。
@Configuration//(proxyBeanMethods = false)
public class Log4j2Configuration {

    private List<Log4j2RingBufferCapacityMonitor> log4j2RingBufferCapacityMonitorList;

    @PostConstruct
    public void init() {
        log4j2RingBufferCapacityMonitorList = new ArrayList<>();

        //通过 LogManager 获取 LoggerContext，从而获取配置
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        org.apache.logging.log4j.core.config.Configuration configuration = loggerContext.getConfiguration();
        //获取 LoggerContext 的名称，因为 Mbean 的名称包含这个
        String ctxName = loggerContext.getName();

        // 注意这里没有判断普通的logger，默认当log4j2中全部都是asyncLogger，实际使用的时候得区分一下
        configuration.getLoggers().keySet().forEach(k -> {
            try {
                // 获取AsyncLogger的名称
                String cfgName = StringUtils.isEmpty(k) ? "" : k;
                log4j2RingBufferCapacityMonitorList.add(new Log4j2RingBufferCapacityMonitor(ctxName, cfgName));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });

        if(!log4j2RingBufferCapacityMonitorList.isEmpty()) {
            // 这里启动监控线程
            new Thread(() -> {
                while(true) {
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        log4j2RingBufferCapacityMonitorList.forEach(Log4j2RingBufferCapacityMonitor::monitor);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }).start();
        }
    }

}
