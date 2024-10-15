package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FloatTypeHandler extends BaseTypeHandler<Float>{

    @Override
    public Float getNullableResult(ResultSet rs, String column) throws SQLException {
        float result = rs.getFloat(column);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
