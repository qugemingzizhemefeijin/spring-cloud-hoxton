package com.atguigu.springcloud.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class JsonUtil {

	/**
	 * 空的JSON字符串
	 */
	private static final String EMPTY_JSON_STRING = "{}";

	/**
	 * 空的JSON Array字符串
	 */
	private static final String EMPTY_JSON_ARRAY = "[]";

	/**
	 * 空的immutable JSONObject类
	 */
	public static final JSONObject EMPTY_JSON = new JSONObject(Collections.emptyMap());

	/**
	 * 空的immutable JSONArray类
	 */
	public static final JSONArray EMPTY_ARRAY = new JSONArray(Collections.emptyList());

	/**
	 * ObjectMapper，空值过滤
	 */
	private static final ObjectMapper OBJECT_MAPPER = initObjectMapper(true);

	/**
	 * ObjectMapper，空值不过滤
	 */
	private static final ObjectMapper OBJECT_NULL_MAPPER = initObjectMapper(false);

	/**
	 * 初始化ObjectMapper
	 */
	private static ObjectMapper initObjectMapper(boolean filterNull) {
		ObjectMapper objectMapper = new ObjectMapper();
		// 去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		// 设置输入:禁止把POJO中值为null的字段映射到json字符串中
		objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		if(filterNull) {
			// 空值不序列化
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		}
		// 反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		// 序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 单引号处理
		objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

		return objectMapper;
	}
	/**
	 * 将JSON字符串转换为指定类型的JAVA BEAN对象
	 * @param json - String
	 * @param clazz - Class
	 * @return T
	 */
	public static <T> T toObject(String json, Class<T> clazz) {
		if (json == null || json.length() == 0)
			return null;
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 将JAVA BEAN对象转换为JSON字符串，不包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_STRING}字符
	 * @param entity - T
	 * @return String
	 */
	public static <T> String toJson(T entity) {
		if (entity == null)
			return EMPTY_JSON_STRING;
		try {
			return OBJECT_MAPPER.writeValueAsString(entity);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}

	/**
	 * 将JAVA BEAN对象转换为JSON字符串，并且包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_STRING}字符
	 * @param entity - T
	 * @return String
	 */
	public static <T> String toJsonIncludeNull(T entity) {
		if (entity == null) {
			return EMPTY_JSON_STRING;
		}
		try {
			return OBJECT_NULL_MAPPER.writeValueAsString(entity);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}
	/**
	 * 将JSON字符串转换为Map
	 * @param json - String
	 * @return Map<K , V>
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> toMap(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyMap();
		try {
			return OBJECT_MAPPER.readValue(json, HashMap.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}

	/**
	 * 将JSON字符串转换为有序Map
	 * @param json - String
	 * @return Map<K , V>
	 */
	@SuppressWarnings("unchecked")
	public static<K , V> Map<K , V> toLinkedMap(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyMap();
		try {
			return OBJECT_MAPPER.readValue(json, LinkedHashMap.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyMap();
	}

	/**
	 * 将JSON字符串转换为List
	 * @param json - String
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T>  List<T> toList(String json) {
		if (json == null || json.length() == 0)
			return Collections.emptyList();
		try {
			return OBJECT_MAPPER.readValue(json, ArrayList.class);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	/**
	 * 将Map对象转换为JSON字符串，不包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_STRING}字符
	 * @param obj - Map<K , V>
	 * @return String
	 */
	public static <K, V> String toJson(Map<K, V> obj) {
		if (obj == null || obj.isEmpty())
			return EMPTY_JSON_STRING;
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}

	/**
	 * 将Map对象转换为JSON字符串，并且包含为空的属性，如果Map为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_STRING}字符
	 * @param obj - Map<K , V>
	 * @return String
	 */
	public static <K, V> String toJsonIncludeNull(Map<K, V> obj) {
		if (obj == null || obj.isEmpty()) {
			return EMPTY_JSON_STRING;
		}
		try {
			return OBJECT_NULL_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_STRING;
	}

	/**
	 * 将Collection对象转换为JSON字符串，不包含为空的属性，如果Collection为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_ARRAY}字符
	 * @param obj - Collection<T>
	 * @return String
	 */
	public static <T> String toJson(Collection<T> obj) {
		if (obj == null || obj.isEmpty())
			return EMPTY_JSON_ARRAY;
		try {
			return OBJECT_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_ARRAY;
	}

	/**
	 * 将Collection对象转换为JSON字符串，并且包含为空的属性，如果Collection为空或者转换失败，将返回{@link JsonUtil#EMPTY_JSON_ARRAY}字符
	 * @param obj - Collection<T>
	 * @return String
	 */
	public static <T> String toJsonIncludeNull(Collection<T> obj) {
		if (obj == null || obj.isEmpty()) {
			return EMPTY_JSON_ARRAY;
		}
		try {
			return OBJECT_NULL_MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON_ARRAY;
	}

	/**
	 * 将Map对象转换为指定的Bean对象
	 * @param map - map
	 * @param clazz - Class<T>
	 * @return T
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T toObject(Map map, Class<T> clazz) {
		if (map == null || map.isEmpty())
			return null;
		return OBJECT_MAPPER.convertValue(map, clazz);
	}

	/**
	 * 将json array 字符串 转换为指定的集合类型
	 * @param jsonArrayStr - String
	 * @param clazz - Class<T>
	 * @return List<T>
	 */
	public static <T> List<T> toList(String jsonArrayStr, Class<T> clazz) {
		if (jsonArrayStr != null && jsonArrayStr.length() > 0) {
			try {
				return OBJECT_MAPPER.readValue(jsonArrayStr, new TypeReference<List<T>>() { });
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return Collections.emptyList();
	}

	/**
	 * 将字符串转换成FastJson对象<br>
	 * 如果字符串为空或者发生异常，将返回一个不可修改的非空JSONObject
	 * @param json - String
	 * @return JSONObject
	 */
	public static JSONObject toJsonObject(String json) {
		if (json == null || json.length() == 0) {
			return EMPTY_JSON;
		}
		try {
			return JSONObject.parseObject(json);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON;
	}

	/**
	 * 将字符串转换成FastJson对象<br>
	 * 如果字符串为空或者发生异常，将返回一个不可修改的非空JSONObject
	 * @param json - String
	 * @param ordered - 是否按照文本顺序显示
	 * @return JSONObject
	 */
	public static JSONObject toJsonObject(String json , boolean ordered) {
		if (json == null || json.length() == 0) {
			return EMPTY_JSON;
		}
		try {
			if(ordered) {
				return JSONObject.parseObject(json , Feature.OrderedField);
			} else {
				return JSONObject.parseObject(json);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_JSON;
	}
	/**
	 * 将字符串转换成FastJson对象<br>
	 * 如果字符串为空或者发生异常，将返回一个不可修改的非空JSONArray
	 * @param json - String
	 * @return JSONArray，返回的对象请不要进行任何写入操作
	 */
	public static JSONArray toJsonArray(String json) {
		if (json == null || json.length() == 0) {
			return EMPTY_ARRAY;
		}
		try {
			return JSONObject.parseArray(json);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return EMPTY_ARRAY;
	}

}
