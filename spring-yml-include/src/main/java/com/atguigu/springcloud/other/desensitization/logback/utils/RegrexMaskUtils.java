package com.atguigu.springcloud.other.desensitization.logback.utils;

import cn.hutool.core.util.StrUtil;
import com.atguigu.springcloud.other.desensitization.logback.MaskConfig;
import com.atguigu.springcloud.other.desensitization.logback.MaskRuleEnum;
import com.atguigu.springcloud.other.desensitization.logback.handler.MaskHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RegrexMaskUtils {

    @Resource
    private Map<String, MaskHandler> handlerMap;

    @Resource
    private MaskConfig maskConfig;

    public String doMask(String maskLog) {
        String processLog = maskLog;
        Map<String, String> regexMap = maskConfig.getRegexMap();
        // 正则表达式
        for (Map.Entry<String, String> entry : regexMap.entrySet()) {
            // 规则关键字以及过滤规则是否开启
            String key = entry.getKey();
            String value = entry.getValue();
            if (StrUtil.equals(value, "1")) {
                processLog = handlerMap.get(MaskRuleEnum.match(key)).regex(processLog);
            }
        }

        return processLog;
    }
}
