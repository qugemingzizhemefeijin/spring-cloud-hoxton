package com.atguigu.springcloud.other.bytekit;

import java.util.HashMap;
import java.util.Map;

public class Statement {

    public Result executeQuery(String sql) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("ccc", "x1");
        dataMap.put("ddd", "x2");
        return new Result(dataMap);
    }

    public Result execute(String sql) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("aaa", "123");
        dataMap.put("bbb", "xxx");
        return new Result(dataMap);
    }

}
