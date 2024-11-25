package com.sakander.mapping;

import com.sakander.executor.type.TypeHandler;
import com.sakander.executor.type.TypeHandlerRegistry;
import com.sakander.utils.Utils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public class ResultMap {
    private String id;
    private List<ResultMapping> constructorResultMappings;
    private Class<?> type;

    public ResultMap() {

    }

    public ResultMap(Class<?> type){
        this.type = type;
        constructorResultMappings = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String column = Utils.getColumnName(field);
            Class<?> javaType = field.getType();
            TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
            TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
            ResultMapping resultMapping = new ResultMapping(column,javaType,typeHandler);
            constructorResultMappings.add(resultMapping);
        }
    }

    public ResultMap(Class<?> type,String ...columns){
        this.type = Map.class;
        constructorResultMappings = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        List<String> columnList = Arrays.stream(columns).toList();
        for(Field field : fields){
            String column = Utils.getColumnName(field);
            if(columnList.contains(column)){
                Class<?> javaType = field.getType();
                TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
                TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
                ResultMapping resultMapping = new ResultMapping(column,javaType,typeHandler);
                constructorResultMappings.add(resultMapping);
            }
        }
    }

    public ResultMap(String ...columns){
        this.type = Map.class;
        constructorResultMappings = new ArrayList<>();
        Field[] fields = type.getDeclaredFields();
        List<String> columnList = Arrays.stream(columns).toList();
        for(Field field : fields){
            String column = Utils.getColumnName(field);
            if(columnList.contains(column)){
                Class<?> javaType = field.getType();
                TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
                TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
                ResultMapping resultMapping = new ResultMapping(column,javaType,typeHandler);
                constructorResultMappings.add(resultMapping);
            }
        }
    }
}
