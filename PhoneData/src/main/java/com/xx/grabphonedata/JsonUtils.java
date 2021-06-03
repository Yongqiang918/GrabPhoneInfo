package com.xx.grabphonedata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    /**
     * 把json string 转化成类对象
     *
     * @param
     * @param
     * @return
     */
    public static <T> T stringJsonToObject(String str, Class<T> cla) {//agsagasgaasg
        try {
            if (str != null && !"".equals(str.trim())) {
                T res = JSONArray.parseObject(str.trim(), cla);
                return res;
            }
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * 把json string 转化成类对象List
     *
     * @param
     * @param
     * @return
     */
    public static <T> List<T> stringJsonToObjectList(String str, Class<T> cla) {//saagasg
        try {
            if (str != null && !"".equals(str.trim())) {
                List<T> res = JSONArray.parseArray(str.trim(), cla);
                return res;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把类对象转化成json string
     *
     * @param
     * @return
     */
    public static <T> String objectToStringJson(T obj) {//dsafasvf
        try {
            return JSONObject.toJSONString(obj);
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * list转json
     *
     * @param
     * @return
     */
    public static String listToJson(Object obj) {//listPro2Json
        try {
            return JSONArray.toJSONString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return
     * @Description： json字符串转换为Map
     */
    public static Map<String, String> jsonToMap(String safsaf) {//safsafasf
        try {
            return JSONObject.parseObject(safsaf, Map.class);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * @param
     * @return
     */
    public static String mapToJson(Map<String, String> asfasf) {//asfasf
        try {
            return JSON.toJSONString(asfasf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String arrToJson(ArrayList arr) {
        try {
            return JSONArray.toJSONString(arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将json转换为Map
     * @param jsonStr
     * @return
     */
    public static HashMap<Integer, Integer> json2Map_int(String jsonStr) {
        HashMap<Integer, Integer> map = JSON.parseObject(
                jsonStr, new TypeReference<HashMap<Integer, Integer>>() {
                });

        return map;
    }





}
