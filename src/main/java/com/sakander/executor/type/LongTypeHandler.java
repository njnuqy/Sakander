package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler extends BaseTypeHandler<Long>{
    @Override
    public Long getNullableResult(ResultSet rs, String column) throws SQLException {
        long result = rs.getLong(column);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
