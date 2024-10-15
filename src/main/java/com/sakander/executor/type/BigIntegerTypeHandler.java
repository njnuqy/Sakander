package com.sakander.executor.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BigIntegerTypeHandler extends BaseTypeHandler<BigInteger> {
    @Override
    public BigInteger getNullableResult(ResultSet rs, String column) throws SQLException {
        BigDecimal bigDecimal = rs.getBigDecimal(column);
        return bigDecimal == null ? null : bigDecimal.toBigInteger();
    }
}
