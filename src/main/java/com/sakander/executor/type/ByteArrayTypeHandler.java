package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteArrayTypeHandler extends BaseTypeHandler<byte[]> {
    @Override
    public byte[] getNullableResult(ResultSet rs, String column) throws SQLException {
        return rs.getBytes(column);
    }
}
