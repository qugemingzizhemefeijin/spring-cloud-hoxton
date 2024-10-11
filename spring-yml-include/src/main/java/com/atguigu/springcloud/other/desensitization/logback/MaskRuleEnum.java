package com.atguigu.springcloud.other.desensitization.logback;

import java.util.HashMap;
import java.util.Map;

public enum MaskRuleEnum {

    PHONE("phone", "phoneHandler"),
    NAME("name", "nameHandler"),
    EMAIL("email", "emailHandler"),
    ID_CARD("idCard", "idCardHandler"),
    ADDRESS("address", "addressHandler"),
    BANK("bank", "bankHandler"),
    ;

    MaskRuleEnum(String rule, String handler) {
        this.rule = rule;
        this.handler = handler;
    }

    public final String rule;
    public final String handler;

    public final static Map<String, String> map = new HashMap<>();

    static {
        for (MaskRuleEnum rule : MaskRuleEnum.values()) {
            map.put(rule.rule, rule.handler);
        }
    }

    public static String match(String name) {
        return map.get(name);
    }
}
