package com.sakander.executor.type;

import com.sakander.utils.ByteArrayUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ByteObjectArrayTypeHandler extends BaseTypeHandler<Byte[]>{
    @Override
    public Byte[] getNullableResult(ResultSet rs, String column) throws SQLException {
        byte[] bytes = rs.getBytes(column);
        return getBytes(bytes);
    }

    private Byte[] getBytes(byte[] bytes){
        Byte[] returnValue = null;
        if(bytes != null){
            returnValue = ByteArrayUtils.convertToObjectArray(bytes);
        }
        return returnValue;
    }
}
