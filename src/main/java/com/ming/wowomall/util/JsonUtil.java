package com.ming.wowomall.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-6-20 下午4:07
 */
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     *json转换Object
     * @param json
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> T JsonToObject(String json,Class<T> beanClass){
        T bean = null;
        try {
            bean = MAPPER.readValue(json, beanClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     *Object转换成Json
     * @param object
     * @return
     */
    public static String ObjectToJson(Object object){
        String json = null;
        try {
            json = MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     *Json转换成ObjectList
     * @param json
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> List<T> JsonToObjectList(String json,Class<T> beanClass){
        //构造一个ListType
        JavaType listType = MAPPER.getTypeFactory().constructParametricType(List.class, beanClass);
        List<T> objectList = null;
        try {
            objectList = (List<T>)MAPPER.readValue(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objectList;
    }



}
