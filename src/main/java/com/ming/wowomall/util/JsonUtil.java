package com.ming.wowomall.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.google.common.collect.Lists;
import com.ming.wowomall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author m969130721@163.com
 * @date 18-6-20 下午4:07
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        //对象的所有字段全部序列化
        MAPPER.setSerializationInclusion(Include.ALWAYS);
        //取消自动转timestamps
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //忽略　在json字符串中存在,但是java对象中不存在对应属性的情况,防止报错
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        //日期格式
        MAPPER.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
    }

    public static <T> String obj2String (T obj){
        if(obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("parse object to String error ",e);
            return null;
        }
    }

    public static <T> String obj2StringPretty (T obj){
        if(obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("parse object to String error ",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<T> calzz){
        if (StringUtils.isEmpty(str) || calzz == null) {
            return null;
        }
        try {
            return calzz.equals(String.class) ? (T)str : MAPPER.readValue(str, calzz);
        } catch (IOException e) {
            log.warn("parse String to Object error ",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str, TypeReference<T> typeReference){
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return typeReference.getType().equals(String.class) ?  (T)str : MAPPER.readValue(str,typeReference);
        } catch (Exception e) {
            log.warn("parse String to Object error ",e);
            return null;
        }
    }

    public static <T> T string2Obj(String str,Class<?> collectionClass,Class<?>... beanClass){
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(collectionClass,beanClass);
        try {
            return MAPPER.readValue(str,javaType);
        } catch (Exception e) {
            log.warn("parse String to Object error ",e);
            return null;
        }
    }


    public static void main(String[] args) {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("ming@163.com");

        User user2 = new User();
        user2.setId(1);
        user2.setEmail("ming@1631.com");

        List list = Lists.newArrayList(user1,user2);
        String json = JsonUtil.obj2StringPretty(list);
        List<User> object = JsonUtil.string2Obj(json, new TypeReference<List<User>>() {
        });


        System.out.println(object);
    }
}
