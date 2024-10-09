package com.sakander.executor.statement;


import com.sakander.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {
    Statement prepare(Connection connection, Integer transactionTimeout) throws SQLException;

    void parameterize(Statement statement) throws SQLException;

    void batch(Statement statement) throws SQLException;

    int update(Statement statement) throws SQLException;

    <E>List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;


}
