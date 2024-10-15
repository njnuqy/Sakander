package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class DateTypeHandler extends BaseTypeHandler<Date>{

    @Override
    public Date getNullableResult(ResultSet rs, String column) throws SQLException {
        return toDate(rs.getTimestamp(column));
    }

    private Date toDate(Timestamp timestamp){
        return timestamp == null ? null : new Date(timestamp.getTime());
    }
}
