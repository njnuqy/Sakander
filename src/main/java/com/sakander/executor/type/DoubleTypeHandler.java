package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DoubleTypeHandler extends BaseTypeHandler<Double>{
    @Override
    public Double getNullableResult(ResultSet rs, String column) throws SQLException {
        double result = rs.getDouble(column);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
