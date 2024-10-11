package com.atguigu.springcloud.other.desensitization.logback.handler;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BankHandler extends AbstractMaskHandler implements MaskHandler {

    private static final Pattern PATTERN = Pattern.compile("(?<!\\w)(\\d{16}|\\d{17}|\\d{19,30})(?!\\w)");

    public int getStartIdx(String matcherGroupStr) {
        return 4;
    }

    public int getEndIdx(String matcherGroupStr) {
        return 4;
    }

    public String regex(String str) {
        return this.matcher(str, PATTERN);
    }

    public String keyword(String str) {
        return DesensitizedUtil.bankCard(str);
    }

}
