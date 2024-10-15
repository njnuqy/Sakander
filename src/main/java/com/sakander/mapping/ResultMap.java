package com.sakander.mapping;

import lombok.Getter;

import java.util.List;
@Getter
public class ResultMap {
    private String id;
    private List<ResultMapping> constructorResultMappings;
    private Class<?> type;

    public ResultMap() {

    }
    public ResultMap(Class<?> type,List<ResultMapping> constructorResultMappings) {
        this.type = type;
        this.constructorResultMappings = constructorResultMappings;
    }
}
