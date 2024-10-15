package com.sakander.executor.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

public class ArrayTypeHandler extends BaseTypeHandler<Object>{

    private static final ConcurrentHashMap<Class<?>,String> STANDARD_MAPPING;

    static{
        STANDARD_MAPPING = new ConcurrentHashMap<>();
        STANDARD_MAPPING.put(BigDecimal.class,JdbcType.NUMERIC.name());
        STANDARD_MAPPING.put(BigInteger.class, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(boolean.class, JdbcType.BOOLEAN.name());
        STANDARD_MAPPING.put(Boolean.class, JdbcType.BOOLEAN.name());
        STANDARD_MAPPING.put(byte[].class, JdbcType.VARBINARY.name());
        STANDARD_MAPPING.put(byte.class, JdbcType.TINYINT.name());
        STANDARD_MAPPING.put(Byte.class, JdbcType.TINYINT.name());
        STANDARD_MAPPING.put(Calendar.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(java.sql.Date.class, JdbcType.DATE.name());
        STANDARD_MAPPING.put(java.util.Date.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(double.class, JdbcType.DOUBLE.name());
        STANDARD_MAPPING.put(Double.class, JdbcType.DOUBLE.name());
        STANDARD_MAPPING.put(float.class, JdbcType.REAL.name());
        STANDARD_MAPPING.put(Float.class, JdbcType.REAL.name());
        STANDARD_MAPPING.put(int.class, JdbcType.INTEGER.name());
        STANDARD_MAPPING.put(Integer.class, JdbcType.INTEGER.name());
        STANDARD_MAPPING.put(LocalDate.class, JdbcType.DATE.name());
        STANDARD_MAPPING.put(LocalDateTime.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(LocalTime.class, JdbcType.TIME.name());
        STANDARD_MAPPING.put(long.class, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(Long.class, JdbcType.BIGINT.name());
        STANDARD_MAPPING.put(OffsetDateTime.class, JdbcType.TIMESTAMP_WITH_TIMEZONE.name());
        STANDARD_MAPPING.put(OffsetTime.class, JdbcType.TIME_WITH_TIMEZONE.name());
        STANDARD_MAPPING.put(Short.class, JdbcType.SMALLINT.name());
        STANDARD_MAPPING.put(String.class, JdbcType.VARCHAR.name());
        STANDARD_MAPPING.put(Time.class, JdbcType.TIME.name());
        STANDARD_MAPPING.put(Timestamp.class, JdbcType.TIMESTAMP.name());
        STANDARD_MAPPING.put(URL.class, JdbcType.DATALINK.name());
    }

    public ArrayTypeHandler(){

    }

    @Override
    public Object getNullableResult(ResultSet rs, String column) throws SQLException {
        return extractArray(rs.getArray(column));
    }

    protected Object extractArray(Array array) throws SQLException {
        if(array == null){
            return null;
        }
        Object result = array.getArray();
        array.free();
        return result;
    }
}
