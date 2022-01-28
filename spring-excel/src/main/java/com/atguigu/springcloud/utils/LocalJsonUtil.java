package com.atguigu.springcloud.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

import java.nio.charset.Charset;
import java.util.List;

public class LocalJsonUtil {

    public static <T> List<T> getListFromJson(String path, Class<T> elementType){
        ClassPathResource resource = new ClassPathResource(path);
        String str = IoUtil.read(resource.getStream(), Charset.forName("UTF-8"));
        JSONArray jsonArray = new JSONArray(str);
        return JSONUtil.toList(jsonArray,elementType);
    }

}
