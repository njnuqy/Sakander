package com.sakander.statement;

import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementHandler {
    PreparedStatement prepare(Connection connection,Integer transactionTimeout,Integer fetchSize) throws SQLException;

    int update(PreparedStatement statement);
}
