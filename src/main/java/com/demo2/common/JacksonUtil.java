package com.demo2.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

public class JacksonUtil {
    private static Log logger = LogFactory.getLog(JacksonUtil.class);
    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return mapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 如果JSON字符串为Null或"null"字符串,返回Null. 如果JSON字符串为"[]",返回空集合.
     * <p/>
     * 如需读取集合如List/Map,且不是List<String>这种简单类型时使用如下语句: List<MyBean> beanList = JsonUtil.fromJson(listString, new TypeReference<List<MyBean>>() {});
     */
    @SuppressWarnings("rawtypes")
    public static <T> T fromJson(String jsonString, TypeReference valueTypeRef) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, valueTypeRef);
        } catch (Exception e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public static List fromJsonToList(String jsonString, Class clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    public static Map fromJsonToMap(String jsonString, Class key, Class value) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructMapType(Map.class, key, value));
        } catch (Exception e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 如果对象为Null,返回"null". 如果集合为空集合,返回"[]".
     */
    public static String toJson(Object object) {

        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    public static void main(String[] args) {
        int[] a = new int[8];
        System.out.print(toJson(a));
    }
}
