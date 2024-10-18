package com.sakander.statement;

import com.sakander.executor.resultset.ResultSetHandler;
import com.sakander.session.ResultHandler;
import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface StatementHandler {
    PreparedStatement prepare(Connection connection) throws SQLException;

    void parameterize(PreparedStatement pstmt) throws SQLException;

    int update(PreparedStatement statement) throws SQLException;

    <E> List<E> query(PreparedStatement statement, ResultHandler resultHandler) throws SQLException;
}
