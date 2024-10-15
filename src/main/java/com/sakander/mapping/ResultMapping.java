package com.sakander.mapping;

import com.sakander.executor.type.TypeHandler;
import lombok.Getter;

import java.util.List;
@Getter
public class ResultMapping {
    private final String column;
    private final Class<?> javaType;
    private final TypeHandler<?> typeHandler;

    public ResultMapping(String column,Class<?> javaType,TypeHandler<?> typeHandler){
        this.column = column;
        this.javaType = javaType;
        this.typeHandler = typeHandler;
    }

    public List<ResultMapping> getConstructorResultMappings(Class<?> clazz){

    }
}
