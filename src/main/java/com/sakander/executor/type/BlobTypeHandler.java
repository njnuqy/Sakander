package com.sakander.executor.type;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobTypeHandler extends BaseTypeHandler<byte[]>{

    @Override
    public byte[] getNullableResult(ResultSet rs, String column) throws SQLException {
        return toPrimitiveBytes(rs.getBlob(column));
    }

    private byte[] toPrimitiveBytes(Blob blob) throws SQLException {
        return blob == null ? null : blob.getBytes(1,(int) blob.length());
    }
}
