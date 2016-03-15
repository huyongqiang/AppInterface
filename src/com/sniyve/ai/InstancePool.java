package com.sniyve.ai;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yanglang on 16/3/14.
 * 对象缓存池
 * 缓存反射出来的对象，节省内存
 */
public class InstancePool {

    private static Map<String,Object> instance = new HashMap<String,Object>();

    public static Object getInstance(String key){
        return instance.get(key);
    }

    public static void setInstance(String key,Object obj){
        instance.put(key,obj);
    }

    public static boolean has(String key){
        return instance.containsKey(key);
    }
}
