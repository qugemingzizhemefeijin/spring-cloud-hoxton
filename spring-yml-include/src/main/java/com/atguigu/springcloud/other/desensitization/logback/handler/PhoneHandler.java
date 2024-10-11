package com.atguigu.springcloud.other.desensitization.logback.handler;

import cn.hutool.core.util.DesensitizedUtil;
import org.springframework.stereotype.Component;

@Component
public class PhoneHandler extends AbstractMaskHandler implements MaskHandler {

    public int getStartIdx(String matcherGroupStr) {
        return 0;
    }

    public int getEndIdx(String matcherGroupStr) {
        return 0;
    }

    public String regex(String str) {
        return null;
    }

    public String keyword(String str) {
        return DesensitizedUtil.mobilePhone(str);
    }

}
