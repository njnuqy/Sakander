package com.sakander.executor.type;

import com.sakander.mapping.ResultMapException;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> extends TypeReference<T> implements TypeHandler<T>{
    @Override
    public T getResult(ResultSet rs, String columnName) {
        try {
            return getNullableResult(rs,columnName);
        }catch (Exception e){
            throw new ResultMapException("Error attempting to get column " + columnName + "from result set. Cause" + e,e);
        }
    }

    public abstract T getNullableResult(ResultSet rs,String column) throws SQLException;
}
