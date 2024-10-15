package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class TimeOnlyTypeHandler extends BaseTypeHandler<Date> {
    @Override
    public Date getNullableResult(ResultSet rs, String column) throws SQLException {
        return toDate(rs.getTime(column));
    }

    private Date toDate(Time time){
        return time == null ? null : new Date(time.getTime());
    }
}
