package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteTypeHandler extends BaseTypeHandler<Byte>{
    @Override
    public Byte getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte result = rs.getByte(columnName);
        return result == 0 && rs.wasNull() ? null : result;
    }
}
