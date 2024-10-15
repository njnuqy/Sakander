package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NStringTypeHandler extends BaseTypeHandler<String>{
    @Override
    public String getNullableResult(ResultSet rs, String column) throws SQLException {
        return rs.getString(column);
    }
}
