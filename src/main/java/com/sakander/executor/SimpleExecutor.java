package com.sakander.executor;

import com.sakander.config.DbSource;
import com.sakander.session.ResultHandler;
import com.sakander.statement.BaseStatementHandler;
import com.sakander.statement.PreparedStatementHandler;
import com.sakander.statement.Statement;
import com.sakander.statement.StatementHandler;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class SimpleExecutor extends BaseExecutor{
    @Override
    protected int doUpdate(Statement statement) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement,statement.getSQL());
            pstmt = prepareStatement(handler);
            return handler.update(pstmt);
        } finally {
            closeStatement(pstmt);
        }
    }


    @Override
    protected <E> List<E> doQuery(Statement statement,Object parameter, ResultHandler resultHandler) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement,statement.getSQL());
            pstmt = prepareStatement(handler);
            return handler.query(pstmt,resultHandler);
        }finally {
            closeStatement(pstmt);
        }

    }

    protected Connection getConnection(){
        Connection conn = null;
        try {
            conn = DbSource.getConnection();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return conn;
    }

    private PreparedStatement prepareStatement(StatementHandler handler) throws SQLException {
        PreparedStatement statement;
        Connection connection = getConnection();
        statement = handler.prepare(connection);
        handler.parameterize(statement);
        return statement;
    }
    protected void closeStatement(PreparedStatement statement){
        if(statement != null){
            try {
                statement.close();
            }catch (SQLException e){
                // ignore
            }
        }
    }

}
