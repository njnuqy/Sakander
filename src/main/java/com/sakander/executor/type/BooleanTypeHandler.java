package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BooleanTypeHandler extends BaseTypeHandler<Boolean> {
    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        boolean result = rs.getBoolean(columnName);
        return !result && rs.wasNull() ? null : result;
    }
}
