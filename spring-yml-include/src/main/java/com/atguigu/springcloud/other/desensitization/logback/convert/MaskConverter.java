package com.atguigu.springcloud.other.desensitization.logback.convert;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.atguigu.springcloud.other.desensitization.logback.MaskConfig;
import com.atguigu.springcloud.other.desensitization.logback.SpFactUtils;
import com.atguigu.springcloud.other.desensitization.logback.utils.KeywordMaskUtils;
import com.atguigu.springcloud.other.desensitization.logback.utils.RegrexMaskUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 信息转换
public class MaskConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String origLog = event.getFormattedMessage();
        try {
            // 获取脱敏配置
            MaskConfig config = SpFactUtils.getBean("maskConfig", MaskConfig.class);
            // 判断是否开启日志脱敏，以及日志路径
            if (ObjectUtil.isEmpty(config) || !config.getEnable()) {
                return origLog;
            } else if (!StrUtil.startWith(event.getLoggerName(), config.getPath())) {
                return origLog;
            } else {
                // 关键字脱敏策略以及正则的表达
                KeywordMaskUtils b1 = null;
                RegrexMaskUtils b2 = null;
                // 关键字方式
                if (config.getKeyword() && config.getKeywordMap() != null
                        && (b1 = SpFactUtils.getBean(KeywordMaskUtils.class)) != null) {
                    return b1.doMask(origLog);
                }
                // 正则方式
                if (config.getRegrex() && config.getRegexMap() != null
                        && (b2 = SpFactUtils.getBean(RegrexMaskUtils.class)) != null) {
                    return b2.doMask(origLog);
                }
            }
        } catch (Exception e) {
            //log.info("error is {}", e.getMessage(), e);
        }
        return origLog;
    }

}
