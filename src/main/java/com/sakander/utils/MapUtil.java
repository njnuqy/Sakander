package com.sakander.utils;

import java.util.Map;
import java.util.function.Function;

public class MapUtil {
    public static <K,V> V computeIfAbsent(Map<K,V> map, K key, Function<K,V> mappingFunction){
        V value = map.get(key);
        if(value != null){
            return value;
        }
        return map.computeIfAbsent(key,mappingFunction);
    }
}
