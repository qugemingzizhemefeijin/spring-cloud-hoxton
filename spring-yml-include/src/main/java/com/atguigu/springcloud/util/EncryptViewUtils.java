package com.atguigu.springcloud.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 字符串加密
 * @Date 2023-04-21 10:50 AM
 */
@Slf4j
public class EncryptViewUtils {

    /**
     * 字符串加密
     * 加密区间为闭区间 []
     *
     * @param strVal   字符串
     * @param spanChar 加密字符
     * @param beginIdx 开始下标
     * @param endIdx   结束下标
     * @return 加密后的字符串
     */
    public static String toEncryptView(String strVal, String spanChar, int beginIdx, int endIdx) {
        // 不为空
        if (StringUtils.isNotEmpty(strVal)) {
            // 修正开始下标位移问题
            if (beginIdx < 0) {
                beginIdx = 0;
            }
            // 修正结束下标位移问题
            if (endIdx > strVal.length()) {
                endIdx = strVal.length() - 1;
            }
            // 处理结束下标小于开始下标的问题
            if (beginIdx > endIdx) {
                endIdx = beginIdx;
            }
            StringBuilder builder = new StringBuilder();
            // 如果结束下标是0
            if (endIdx == 0) {
                builder.append(spanChar);
            } else {
                // 循环处理加密字符
                char[] chars = strVal.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (i >= beginIdx && i <= endIdx) {
                        builder.append(spanChar);
                    } else {
                        builder.append(chars[i]);
                    }
                }
            }
            return builder.toString();
        }
        return strVal;
    }


}
