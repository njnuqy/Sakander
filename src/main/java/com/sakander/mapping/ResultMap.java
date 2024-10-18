package com.sakander.mapping;

import com.sakander.executor.type.TypeHandler;
import com.sakander.executor.type.TypeHandlerRegistry;
import com.sakander.utils.Utils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
    public ResultMap(Class<?> type,List<ResultMapping> constructorResultMappings) {
        this.type = type;
        this.constructorResultMappings = constructorResultMappings;
    }


}
