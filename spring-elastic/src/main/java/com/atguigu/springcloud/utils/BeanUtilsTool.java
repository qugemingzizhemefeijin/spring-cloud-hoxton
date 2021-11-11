package com.atguigu.springcloud.utils;

import java.lang.reflect.Field;
import java.util.*;

public class BeanUtilsTool {

    /**
     * 把对象转成Map
     */
    public static <T> Map<String, Object> objectToMap(T t) {
        return objectToMap(t, null);
    }

    /**
     * 把对象集合转成Map集合
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> ts) {
        return objectToMap(ts, null);
    }

    /**
     * 把对象转成Map，只包含指定字段
     *
     * @param t      对象
     * @param fields 包含的字段
     * @return Map
     */
    public static <T> Map<String, Object> objectToMap(T t, String[] fields) {
        if (t == null) return null;
        List<String> fieldList = null;
        if (fields != null) {
            fieldList = Arrays.asList(fields);
        }
        Map<String, Object> map = new HashMap<>();
        Field[] fieldArray = t.getClass().getDeclaredFields();
        for (Field field : fieldArray) {
            field.setAccessible(true);
            if (fieldList == null || fieldList.contains(field.getName())) {
                try {
                    map.put(field.getName(), field.get(t));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 把对个对象集合转成Map集合，只包含指定字段
     *
     * @param ts     对象集合
     * @param fields 包含的字段
     * @return List<Map>
     */
    public static <T> List<Map<String, Object>> objectToMap(List<T> ts, String[] fields) {
        List<Map<String, Object>> rs = new ArrayList<>();
        for (T t : ts) {
            Map<String, Object> map = objectToMap(t, fields);
            if (map != null) rs.add(map);
        }
        return rs;
    }

}
