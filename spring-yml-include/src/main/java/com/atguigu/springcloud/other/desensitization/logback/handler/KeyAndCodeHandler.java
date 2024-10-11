package com.atguigu.springcloud.other.desensitization.logback.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

@Component
public class KeyAndCodeHandler extends AbstractMaskHandler implements MaskHandler {

    public int getStartIdx(String matcherGroupStr) {
        return 0;
    }

    public int getEndIdx(String matcherGroupStr) {
        return 0;
    }

    public String regex(String str) {
        return str;
    }

    public String keyword(String str) {
        String s = StrUtil.blankToDefault(str, "");
        return StrUtil.length(s) > 7 ? this.lastReplace(s, 3, 3) : "*****";
    }

}
