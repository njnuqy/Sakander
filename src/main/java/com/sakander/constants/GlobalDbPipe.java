package com.sakander.constants;

import lombok.Data;

import java.util.HashMap;
@Data
public class GlobalDbPipe {
    private static GlobalDbPipe instance;
    private HashMap<String, Class<?>> classHashMap;
    private GlobalDbPipe(){
        classHashMap = new HashMap<>();
    }
    public static synchronized GlobalDbPipe getInstance(){
        if(instance == null){
            instance = new GlobalDbPipe();
        }
        return instance;
    }

    public static void addClassToMap(String key, Class<?> clazz) {
        // 注意：这里可能需要额外的同步处理，具体取决于你的使用场景
        getInstance().getClassHashMap().put(key, clazz);
    }

    public static Class<?> getClassFromMap(String key){
        return getInstance().getClassHashMap().get(key);
    }
}
