package com.sakander.executor.type;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobInputStreamTypeHandler extends BaseTypeHandler<InputStream> {
    @Override
    public InputStream getNullableResult(ResultSet rs, String column) throws SQLException {
        return toInputStream(rs.getBlob(column));
    }

    private InputStream toInputStream(Blob blob) throws SQLException {
        if(blob == null){
            return null;
        }
        return blob.getBinaryStream();
    }
}
