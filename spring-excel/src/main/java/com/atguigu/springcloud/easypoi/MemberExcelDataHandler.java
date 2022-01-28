package com.atguigu.springcloud.easypoi;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import cn.hutool.core.util.StrUtil;
import com.atguigu.springcloud.easypoi.domain.Member;

/**
 * 自定义字段处理
 */
public class MemberExcelDataHandler extends ExcelDataHandlerDefaultImpl<Member> {

    @Override
    public Object exportHandler(Member obj, String name, Object value) {
        if ("昵称".equals(name)) {
            String emptyValue = "暂未设置";
            if (value == null) {
                return super.exportHandler(obj, name, emptyValue);
            }
            if (value instanceof String && StrUtil.isBlank((String) value)) {
                return super.exportHandler(obj, name, emptyValue);
            }
        }
        return super.exportHandler(obj, name, value);
    }

    @Override
    public Object importHandler(Member obj, String name, Object value) {
        return super.importHandler(obj, name, value);
    }

}
