package com.sakander.utils;

import com.sakander.annotations.Column;
import com.sakander.annotations.Table;

import java.lang.reflect.Field;
import java.util.List;

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
    public static String getColumnName(Field field){
        boolean isPresent = field.isAnnotationPresent(Column.class);
        if(isPresent){
            return field.getAnnotation(Column.class).name();
        }
        return Utlis.toSnakeCase(field.getName());
    }
    public static <E> String getTableName(Class<E> clazz){
        boolean existTableAnno = clazz.isAnnotationPresent(Table.class);
        if(existTableAnno){
            Table tableAnno = clazz.getAnnotation(Table.class);
            return tableAnno.name();
        }
        System.out.println(Utlis.toSnakeCase(clazz.getSimpleName()));
        return Utlis.toSnakeCase(clazz.getSimpleName());
    }
    public static<E> Object[] getListParams(List<E> elements,int length){
        Object[] params = new Object[length];
        for(int i = 0 ; i < elements.size() ; i++){
            Field[] declaredFields = elements.get(i).getClass().getDeclaredFields();
            for(int j = 0 ; j < declaredFields.length ;j++){
                declaredFields[j].setAccessible(true);
                try {
                    params[i * declaredFields.length + j] = declaredFields[j].get(elements.get(i));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return params;
    }
    public static<E> Object[] getSqlParams(E element,Field[] fields){
        Object[] params = new Object[fields.length];
        for(int i = 0 ; i < fields.length ; i++){
            fields[i].setAccessible(true);
            try {
                params[i] = fields[i].get(element);
            }catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println("获取" + element + "的属性值失败");
                e.printStackTrace();
            }
        }
        return params;
    }
    public static<E> void judgeIfNull(E element){
        if(element == null){
            throw new IllegalArgumentException("插入的元素为空");
        }
    }
    public static<E> void judgeIfHasFields(E element,Field[] fields){
        if(fields == null || fields.length == 0){
            throw new RuntimeException(element + "没有属性");
        }
    }
}
