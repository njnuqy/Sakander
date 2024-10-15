package com.sakander.executor.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {
    ARRAY(Types.ARRAY),

    BIT(Types.BIT),

    TINYINT(Types.TINYINT),

    SMALLINT(Types.SMALLINT),

    INTEGER(Types.INTEGER),

    BIGINT(Types.BIGINT),

    FLOAT(Types.FLOAT),

    REAL(Types.REAL),

    DOUBLE(Types.DOUBLE),

    NUMERIC(Types.NUMERIC),

    DECIMAL(Types.DECIMAL),

    CHAR(Types.CHAR),

    VARCHAR(Types.VARCHAR),

    LONGVARCHAR(Types.LONGVARCHAR),

    DATE(Types.DATE),

    TIME(Types.TIME),

    TIMESTAMP(Types.TIMESTAMP),

    BINARY(Types.BINARY),

    VARBINARY(Types.VARBINARY),

    LONGVARBINARY(Types.LONGVARBINARY),

    NULL(Types.NULL),

    OTHER(Types.OTHER),

    BLOB(Types.BLOB),

    CLOB(Types.CLOB),

    BOOLEAN(Types.BOOLEAN),

    CURSOR(-10), // oracle

    UNDEFINED(Integer.MIN_VALUE + 1000),

    NVARCHAR(Types.NVARCHAR),

    NCHAR(Types.NCHAR),

    NCLOB(Types.NCLOB),

    STRUCT(Types.STRUCT),

    JAVA_OBJECT(Types.JAVA_OBJECT),

    DISTINCT(Types.DISTINCT),

    REF(Types.REF),

    DATALINK(Types.DATALINK),

    ROWID(Types.ROWID),

    LONGNVARCHAR(Types.LONGNVARCHAR),

    SDLXML(Types.SQLXML),

    DATATIMEOFFSET(-155), // SQL Server 2008

    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE), //JDBC 4.2 JDK8

    TIMESTAMP_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE);// JDBC 4.2 JDK8

    public final int TYPE_CODE;
    public static final Map<Integer,JdbcType> codeLookup = new HashMap<>();
    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    public static JdbcType forCode(int code){
        return codeLookup.get(code);
    }

}
