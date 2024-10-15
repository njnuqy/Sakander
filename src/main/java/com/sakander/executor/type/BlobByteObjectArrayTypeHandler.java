package com.sakander.executor.type;

import com.sakander.utils.ByteArrayUtils;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobByteObjectArrayTypeHandler extends BaseTypeHandler<Byte[]>{
    @Override
    public Byte[] getNullableResult(ResultSet rs, String column) throws SQLException {
        return new Byte[0];
    }

    private Byte[] getBytes(Blob blob) throws SQLException {
        Byte[] returnValue = null;
        if(blob != null){
            returnValue = ByteArrayUtils.convertToObjectArray(blob.getBytes(1,(int) blob.length()));
        }
        return returnValue;
    }
}
