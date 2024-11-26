package com.sakander.executor;

import com.sakander.config.DbSource;
import com.sakander.session.ResultHandler;
import com.sakander.statement.*;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class SimpleExecutor extends BaseExecutor{
    @Override
    protected int doUpdate(Statement statement,Class<?> type) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement,statement.getSQL(),type);
            pstmt = prepareStatement(handler);
            return handler.update(pstmt);
        } finally {
            closeStatement(pstmt);
        }
    }

    @Override
    protected int doUpdate(UpdateCondition condition, Class<?> type) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,condition,condition.getSQL(),type);
            pstmt = prepareStatement(handler);
            return handler.update(pstmt);
        } finally {
            closeStatement(pstmt);
        }
    }

    @Override
    protected <E> List<E> doQuery(Statement statement, ResultHandler resultHandler,Class<?> type) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement,statement.getSQL(),type);
            pstmt = prepareStatement(handler);
            return handler.query(pstmt,resultHandler);
        }finally {
            closeStatement(pstmt);
        }

    }

    @Override
    protected <E> List<E> doQuery(Statement statement, ResultHandler resultHandler, Class<?> type, String... columns) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement,statement.getSQL(),type);
            pstmt = prepareStatement(handler);
            return handler.queryMapList(pstmt,resultHandler,columns);
        }finally {
            closeStatement(pstmt);
        }
    }

    @Override
    protected <E> List<E> doQuery(Statement statement, ResultHandler resultHandler,String ...columns) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            StatementHandler handler = new PreparedStatementHandler(this,statement);
            pstmt = prepareStatement(handler);
            return handler.query(pstmt,resultHandler,columns);
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
        handler.newParameterize(statement);
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
