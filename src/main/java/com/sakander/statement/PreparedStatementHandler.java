package com.sakander.statement;

import com.sakander.executor.Executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Executor executor, Statement statement, String sql) {
        super(executor, statement, sql);
    }

    @Override
    protected PreparedStatement instantiatePreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public int update(PreparedStatement statement) {
        return 0;
    }
}
