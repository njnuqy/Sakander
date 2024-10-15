package com.sakander.executor.type;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {
    @Override
    public E getNullableResult(ResultSet rs, String column) throws SQLException {
        return null;
    }
}
