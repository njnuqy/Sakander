package com.sakander.executor.type;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobTypeHandler extends BaseTypeHandler<String>{
    @Override
    public String getNullableResult(ResultSet rs, String column) throws SQLException {
        Clob clob = rs.getClob(column);
        return toString(clob);
    }

    private String toString(Clob clob) throws SQLException {
        return clob == null ? null : clob.getSubString(1,(int) clob.length());
    }
}
