package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShortTypeHandler extends BaseTypeHandler<Short>{
    @Override
    public Short getNullableResult(ResultSet rs, String column) throws SQLException {
        short result = rs.getShort(column);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
