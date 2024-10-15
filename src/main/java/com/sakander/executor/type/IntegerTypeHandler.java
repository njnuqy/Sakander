package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IntegerTypeHandler extends BaseTypeHandler<Integer>{
    @Override
    public Integer getNullableResult(ResultSet rs, String column) throws SQLException {
        int result = rs.getInt(column);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
