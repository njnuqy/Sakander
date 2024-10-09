package com.sakander.utils;

import java.util.Arrays;

public class ArrayUtil {
    public static int hashcode(Object obj){
        if(obj == null){
            return 0;
        }
        final Class<?> clazz = obj.getClass();
        if(!clazz.isArray()){
            return obj.hashCode();
        }
        final Class<?> componentType = clazz.getComponentType();
        if(long.class.equals(componentType)){
            return Arrays.hashCode((long[]) obj);
        }
        if (int.class.equals(componentType)) {
            return Arrays.hashCode((int[]) obj);
        } else if (short.class.equals(componentType)) {
            return Arrays.hashCode((short[]) obj);
        } else if (char.class.equals(componentType)) {
            return Arrays.hashCode((char[]) obj);
        } else if (byte.class.equals(componentType)) {
            return Arrays.hashCode((byte[]) obj);
        } else if (boolean.class.equals(componentType)) {
            return Arrays.hashCode((boolean[]) obj);
        } else if (float.class.equals(componentType)) {
            return Arrays.hashCode((float[]) obj);
        } else if (double.class.equals(componentType)) {
            return Arrays.hashCode((double[]) obj);
        } else {
            return Arrays.hashCode((Object[]) obj);
        }
    }

    public static boolean equals(Object thisObj,Object thatObj){
        if(thisObj == null){
            return thatObj == null;
        }
        if(thatObj == null){
            return false;
        }
        final Class<?> clazz = thisObj.getClass();
        if(!clazz.equals(thatObj.getClass())){
            return false;
        }
        if(!clazz.isArray()){
            return thisObj.equals(thatObj);
        }
        final Class<?> componentType = clazz.getComponentType();
        if (long.class.equals(componentType)) {
            return Arrays.equals((long[]) thisObj, (long[]) thatObj);
        }
        if (int.class.equals(componentType)) {
            return Arrays.equals((int[]) thisObj, (int[]) thatObj);
        } else if (short.class.equals(componentType)) {
            return Arrays.equals((short[]) thisObj, (short[]) thatObj);
        } else if (char.class.equals(componentType)) {
            return Arrays.equals((char[]) thisObj, (char[]) thatObj);
        } else if (byte.class.equals(componentType)) {
            return Arrays.equals((byte[]) thisObj, (byte[]) thatObj);
        } else if (boolean.class.equals(componentType)) {
            return Arrays.equals((boolean[]) thisObj, (boolean[]) thatObj);
        } else if (float.class.equals(componentType)) {
            return Arrays.equals((float[]) thisObj, (float[]) thatObj);
        } else if (double.class.equals(componentType)) {
            return Arrays.equals((double[]) thisObj, (double[]) thatObj);
        } else {
            return Arrays.equals((Object[]) thisObj, (Object[]) thatObj);
        }
    }

    public static String toString(Object obj){
        if(obj == null) {
            return "null";
        }
        final Class<?> clazz = obj.getClass();
        if(!clazz.isArray()){
            return obj.toString();
        }
        final Class<?> componentType = clazz.getComponentType();
        if (long.class.equals(componentType)) {
            return Arrays.toString((long[]) obj);
        }
        if (int.class.equals(componentType)) {
            return Arrays.toString((int[]) obj);
        } else if (short.class.equals(componentType)) {
            return Arrays.toString((short[]) obj);
        } else if (char.class.equals(componentType)) {
            return Arrays.toString((char[]) obj);
        } else if (byte.class.equals(componentType)) {
            return Arrays.toString((byte[]) obj);
        } else if (boolean.class.equals(componentType)) {
            return Arrays.toString((boolean[]) obj);
        } else if (float.class.equals(componentType)) {
            return Arrays.toString((float[]) obj);
        } else if (double.class.equals(componentType)) {
            return Arrays.toString((double[]) obj);
        } else {
            return Arrays.toString((Object[]) obj);
        }
    }

    private ArrayUtil(){

    }
}
