package com.sakander.mapping;

import com.sakander.condition.QueryCondition;
import com.sakander.constants.GlobalDbPipe;
import com.sakander.executor.type.TypeHandler;
import com.sakander.executor.type.TypeHandlerRegistry;
import com.sakander.utils.Utils;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
public class ResultMap {
    private final List<ResultMapping> constructorResultMappings;
    private final Class<?> type;

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

    public ResultMap(Class<?> type, QueryCondition condition){
        this.type = HashMap.class;
        this.constructorResultMappings = new ArrayList<>();
        Class<?> joinClass = GlobalDbPipe.getClassFromMap(condition.getJoin().getTable());
        HashMap<String, String> alias = condition.getAlias().getAlias();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            String column = Utils.getColumnName(field);
            if(alias.get("a."+column) != null){
                column = alias.get("a."+column);
            }
            Class<?> javaType = field.getType();
            TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
            TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
            ResultMapping resultMapping = new ResultMapping(column,javaType,typeHandler);
            constructorResultMappings.add(resultMapping);
        }
        fields = joinClass.getDeclaredFields();
        for (Field field : fields) {
            String column = Utils.getColumnName(field);
            if(alias.get("b."+column) != null){
                column = alias.get("b."+column);
            }
            Class<?> javaType = field.getType();
            TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();
            TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(javaType);
            ResultMapping resultMapping = new ResultMapping(column,javaType,typeHandler);
            constructorResultMappings.add(resultMapping);
        }
    }
}
