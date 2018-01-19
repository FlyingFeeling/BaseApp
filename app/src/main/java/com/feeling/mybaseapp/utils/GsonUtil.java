package com.feeling.mybaseapp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by 123 on 2018/1/8.
 */

public class GsonUtil {
    /**
     * JSON格式字符串转为实体类
     *
     * @param jsonResult JSON格式字符串
     * @param tClass          实体类型
     * @param <T>        泛型
     * @return 泛型对象
     */
    public static <T> T json2Bean(String jsonResult, Class<T> tClass) {
        Gson gson = new Gson();
        Type type=TypeToken.get(tClass).getType();
        return gson.fromJson(jsonResult, type);
    }

    /**
     * JSON格式字符串转为实体类
     *
     * TypeToken<T> typeToken=new TypeToken<T>(){};
     * GsonUtil.json2Bean(jsonResult,typeToken)
     *
     * @param jsonResult JSON格式字符串
     * @param typeToken  实体类型
     * @param <T>        泛型
     * @return 泛型对象
     */
    public static <T> T json2Bean(String jsonResult, TypeToken<T> typeToken) {
        Type type=typeToken.getType();
        return new Gson().fromJson(jsonResult, type);
    }

    /**
     * JSON格式字符串转为List集合实体类
     *
     * @param jsonResult JSON格式字符串
     * @param tClass     实体类型
     * @param <T>        泛型
     * @return 泛型对象
     */
    public static <T> List<T> json2List(String jsonResult, Class<T> tClass) {
        Type type=new TypeToken<List<T>>(){}.getType();
        return new Gson().fromJson(jsonResult, type);
    }

    /**
     * 实体类转为JSON格式的字符串
     *
     * @param object 实体对象
     * @return JSON格式字符串
     */
    public static String bean2Json(Object object) {
        return new Gson().toJson(object);

    }
}
