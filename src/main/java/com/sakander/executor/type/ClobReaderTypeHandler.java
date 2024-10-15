package com.sakander.executor.type;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobReaderTypeHandler extends BaseTypeHandler<Reader>{
    @Override
    public Reader getNullableResult(ResultSet rs, String column) throws SQLException {
        return toReader(rs.getClob(column));
    }

    private Reader toReader(Clob clob) throws SQLException {
        if(clob == null){
            return null;
        }
        return clob.getCharacterStream();
    }
}
