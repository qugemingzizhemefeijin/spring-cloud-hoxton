package com.mzt.logapi.test.functions;

import com.mzt.logapi.service.IParseFunction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class OrderParseFunction implements IParseFunction {

    @Override
    public String functionName() {
        //  函数名称为 ORDER
        return "ORDER";
    }

    @Override
    public String apply(String value) {
        if(StringUtils.isEmpty(value)){
            return value;
        }
        return "小马宝贝：" + value;
    }

}
