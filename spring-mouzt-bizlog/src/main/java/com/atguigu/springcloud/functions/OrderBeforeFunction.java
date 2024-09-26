package com.atguigu.springcloud.functions;

import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OrderBeforeFunction implements IParseFunction {

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "B_ORDER";
    }

    @Override
    public String apply(Object value) {
        if(StringUtils.isEmpty(value)){
            return null;
        }
        return "我记录的是原始值：" + value;
    }

}
