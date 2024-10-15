package com.sakander.executor.type;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigDecimalTypeHandler extends BaseTypeHandler<BigDecimal> {
    @Override
    public BigDecimal getNullableResult(ResultSet rs, String column) throws SQLException {
        return rs.getBigDecimal(column);
    }
}
