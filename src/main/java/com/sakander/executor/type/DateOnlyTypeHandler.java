package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DateOnlyTypeHandler extends BaseTypeHandler<Date>{
    @Override
    public Date getNullableResult(ResultSet rs, String column) throws SQLException {
        return null;
    }

    private java.sql.Date toSqlDate(Date date){
        return date == null ? null : new java.sql.Date(date.getTime());
    }

}
