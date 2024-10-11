package com.atguigu.springcloud.other.desensitization.logback.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.atguigu.springcloud.other.desensitization.logback.MaskConfig;
import com.atguigu.springcloud.other.desensitization.logback.MaskRuleEnum;
import com.atguigu.springcloud.other.desensitization.logback.handler.MaskHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KeywordMaskUtils {

    @Resource
    private Map<String, MaskHandler> handlerMap;

    @Resource
    private MaskConfig maskConfig;

    public String doMask(String maskLog) {
        TimeInterval timer = DateUtil.timer();

        // 关键字规则
        Map<String, List<String>> keywordMap = maskConfig.getKeywordMap();
        for (Map.Entry<String, List<String>> entry : keywordMap.entrySet()) {
            // 匹配的规则名称和关键字名称
            String key = entry.getKey();
            // 关键字
            List<String> values = entry.getValue();
            for (String value : values) {
                int index = -1;
                while (true) {
                    // 关键字
                    index = StringUtils.indexOfIgnoreCase(maskLog, value, index + 1);
                    if (index != -1) {
                        // 关键字的值开始和结束标记
                        int startIndex = this.getStartIndex(maskLog, index + value.length());
                        int endIndex = this.getEndIndex(maskLog, startIndex);
                        String subStr = maskLog.substring(startIndex, endIndex);
                        subStr = handlerMap.get(MaskRuleEnum.match(key)).keyword(subStr);
                        maskLog = maskLog.substring(0, startIndex) + subStr + maskLog.substring(endIndex);
                        index += endIndex;
                    } else {
                        break;
                    }
                }
            }
        }
        return maskLog;
    }

    private int getStartIndex(String msg, int valueStart) {
        while (true) {
            char ch = msg.charAt(valueStart);
            if (ch == ':' || ch == '：' || ch == '=') {
                ++valueStart;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    ++valueStart;
                }

                if (ch == ' ') {
                    valueStart += 2;
                }

                return valueStart;
            }

            ++valueStart;
        }
    }

    private int getEndIndex(String msg, int valueEnd) {
        while (true) {
            if (valueEnd != msg.length()) {
                char ch = msg.charAt(valueEnd);
                if (ch == '"') {
                    if (valueEnd + 1 != msg.length()) {
                        char nextCh = msg.charAt(valueEnd + 1);
                        if (nextCh != ';' && nextCh != ',') {
                            ++valueEnd;
                            continue;
                        }

                        while (valueEnd > 0) {
                            char preCh = msg.charAt(valueEnd - 1);
                            if (preCh != '\\') {
                                break;
                            }

                            --valueEnd;
                        }
                    }
                } else if (ch != ';' && ch != ',' && ch != '}') {
                    ++valueEnd;
                    continue;
                }
            }

            return valueEnd;
        }
    }
}
