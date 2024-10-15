package com.sakander.reflection.property;

import com.sakander.reflection.ReflectionException;

import java.util.Locale;

// 处理Java Bean属性名与getter和setter方法名之间的转换
public final class PropertyNamer {
    private PropertyNamer(){
        // 防止实例化
    }

    public static String methodToProperty(String name){
        if(name.startsWith("is")){
            name = name.substring(2);
        }else if(name.startsWith("get") || name.startsWith("set")){
            name = name.substring(3);
        }else{
            throw new ReflectionException("Error parsing property name" + name + "Didn't start with 'is' 'get' or 'set'");
        }
        if(name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))){
            name = name.substring(0,1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

    public static boolean isProperty(String name){
        return isGetter(name) || isSetter(name);
    }

    public static boolean isGetter(String name){
        return name.startsWith("get") && name.length() > 3 || name.startsWith("is") && name.length() > 2;
    }

    public static boolean isSetter(String name){
        return name.startsWith("set") && name.length() > 3;
    }
}
