package com.sakander.mapping;

import lombok.Getter;

public class ResultMap {
    private String id;
    @Getter
    private Class<?> type;

    public ResultMap() {

    }
    public ResultMap(Class<?> type) {
        this.type = type;
    }
}
