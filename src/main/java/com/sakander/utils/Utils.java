package com.sakander.utils;

import com.sakander.annotations.Column;
import com.sakander.annotations.Id;
import com.sakander.annotations.Table;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
@Slf4j
public class Utils {
    public static String toSnakeCase(String camelCase) {
        // 使用正则表达式将每个大写字母前（如果该大写字母不是字符串的第一个字符）插入一个下划线，然后转换为小写
        return camelCase.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }
    public static Object[] mergeArrays(Object[] ...params) {
        int totalLength = 0;
        for (Object[] param : params) {
            if (param != null) {
                totalLength += param.length;
            }
        }
        // 创建一个新的数组来存储合并后的结果
        Object[] merged = new Object[totalLength];
        int index = 0;
        // 复制第一个数组的元素
        for (Object[] param : params) {
            if(param != null){
                System.arraycopy(param, 0, merged, index, param.length);
                index += param.length;
            }
        }
        // 返回合并后的数组
        return merged;
    }
    public static String getColumnName(Field field){
        boolean isPresent = field.isAnnotationPresent(Column.class);
        if(isPresent){
            return field.getAnnotation(Column.class).name();
        }
        return Utils.toSnakeCase(field.getName());
    }
    public static <E> String getTableName(Class<E> clazz){
        boolean existTableAnno = clazz.isAnnotationPresent(Table.class);
        if(existTableAnno){
            Table tableAnno = clazz.getAnnotation(Table.class);
            return tableAnno.name();
        }
        System.out.println(Utils.toSnakeCase(clazz.getSimpleName()));
        return Utils.toSnakeCase(clazz.getSimpleName());
    }
    public static Object[] getSqlParams(Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        Object[] params = new Object[fields.length];
        for(int i = 0 ; i < fields.length ; i++){
            fields[i].setAccessible(true);
            try {
                params[i] = fields[i].get(object);
            }catch (IllegalAccessException e) {
                log.warn("获取{}的属性值失败", object);
            }
        }
        return params;
    }
}
