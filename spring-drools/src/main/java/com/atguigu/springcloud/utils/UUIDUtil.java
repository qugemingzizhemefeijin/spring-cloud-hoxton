package com.atguigu.springcloud.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import java.util.Date;
import java.util.UUID;

import static com.atguigu.springcloud.utils.LossMoneyTemplate.workMoneyST;

@Slf4j
public final class UUIDUtil {

    /**
     * 生成原始UUID
     *
     * @return
     */
    private static String UUIDString() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成格式化UUID
     *
     * @return
     */
    public static String UUIDFormatString(String replace) {
        return UUID.randomUUID().toString().replaceAll(replace, "");
    }

    /**
     * 值加密
     *
     * @return
     */
    public static String MD5AndUUID() {
        //时间戳
        String timeJab = String.valueOf(System.currentTimeMillis());
        //UUID+时间戳
        String concat = UUIDString().concat(timeJab);
        return DigestUtils.md5Hex(concat);
    }

    /**
     * 生成不重复促销编码
     *
     * @return
     */
    public static String typeJoinTime() {
        String dateNowStr = StringJointUtil.dateToStringThree(new Date());
        int math = (int) ((Math.random() * 9 + 1) * 1000000);
        return dateNowStr.concat(String.valueOf(math));

    }

    public static String rule(String json) {
        log.info("\n" + workMoneyST);

        String rule = ruleWordExchangsST(json);
        log.info("\n" + rule);
        return rule;
    }

    /**
     * 规则业务生成
     */
    public static String ruleWordExchangsST(String json) {
        STGroup group = new STGroupString(workMoneyST);
        ST stFile = group.getInstanceOf("wordImport");
        ST stRule = group.getInstanceOf("ruleValue");
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONObject condition = jsonObject.getJSONObject("condition");
        JSONObject action = jsonObject.getJSONObject("action");
        JSONObject rule = jsonObject.getJSONObject("rule");
        stRule.add("condition", condition);
        stRule.add("action", action);
        stRule.add("rule", rule);
        stFile.add("rules", stRule);

        return stFile.render();
    }

}
