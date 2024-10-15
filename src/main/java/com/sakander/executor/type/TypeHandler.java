package com.sakander.executor.type;

import java.sql.ResultSet;

public interface TypeHandler<T> {

    T getResult(ResultSet rs,String columnName);
}
