package com.sakander.utils;

public class Utlis {
    public static String toSnakeCase(String camelCase) {
        // 使用正则表达式将每个大写字母前（如果该大写字母不是字符串的第一个字符）插入一个下划线，然后转换为小写
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    public static Object[] mergeArrays(Object[] ...params) {
        int totalLength = 0;
        for (Object[] param : params) {
            totalLength += param.length;
        }

        // 创建一个新的数组来存储合并后的结果
        Object[] merged = new Object[totalLength];
        int index = 0;
        // 复制第一个数组的元素
        for (Object[] param : params) {
            System.arraycopy(param, 0, merged, index, param.length);
            index += param.length;
        }
        // 返回合并后的数组
        return merged;
    }
}
