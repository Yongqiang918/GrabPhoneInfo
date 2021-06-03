package com.xx.grabphonedata

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import java.util.*


/**
 *Created by Lyq
 *on 2020-09-29
 */
object JsonDataUtil {

    /**
     * 将json转化为对象
     */
    public fun <T> json2Object(jsonStr: String, cla: Class<T>?): T? {
        try {
            if (jsonStr != null && !(jsonStr.trim()).equals("")) {
                return JSONArray.parseObject(jsonStr.trim(), cla)
            }
        } catch (e: Exception) {

        }
        return null
    }

    /**
     * 将json转化为对象List
     */
    public fun <T> json2ObjectList(jsonStr: String?, cla: Class<T>?): MutableList<T?>? {
        try {
            if (jsonStr != null && !(jsonStr.trim()).equals("")) {
                return JSONArray.parseArray(jsonStr.trim(), cla)
            }
        } catch (e: java.lang.Exception) {
        }
        return null
    }

    /**
     * 将json转换为Map
     */
    public fun json2Map(jsonStr: String?): Map<String?, String?>? {
        try {
            return JSONObject.parseObject<Map<String?, String?>>(
                jsonStr,
                MutableMap::class.java
            )
        } catch (e: java.lang.Exception) {
        }
        return null
    }

    /**
     * 将对象转化为json
     */
    public fun <T> object2Json(obj: T): String? {
        try {
            return JSONObject.toJSONString(obj)
        } catch (e: java.lang.Exception) {
        }
        return ""
    }

    /**
     * 将List转化为json
     */
    public fun list2Json(obj: Any?): String? {
        try {
            return JSONArray.toJSONString(obj)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     *将map转化为json
     */
    public fun map2Json(map: Map<String?, String?>?): String? {
        try {
            return JSON.toJSONString(map)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     *将array转化为json
     */
    fun arr2Json(arr: Any?): String? {
        try {
            return JSONArray.toJSONString(arr)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }







}