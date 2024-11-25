package com.sakander.statement;

import com.sakander.executor.Executor;
import com.sakander.mapping.ResultMap;
import com.sakander.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler{


    public PreparedStatementHandler(Executor executor, Statement statement, String sql,Class type) {
        super(executor,statement,type);
    }

    public PreparedStatementHandler(Executor executor, Statement statement) {
        super(executor,statement);
    }

    @Override
    protected PreparedStatement instantiatePreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(sql);
    }
    @Override
    public void parameterize(PreparedStatement pstmt) throws SQLException {
        parameterHandler.setParameters(pstmt,statement.getParameters());
    }
    @Override
    public int update(PreparedStatement pstmt) throws SQLException {
        pstmt.execute();
        return pstmt.getUpdateCount();
    }

    @Override
    public <E> List<E> query(PreparedStatement pstmt, ResultHandler resultHandler) throws SQLException {
        pstmt.execute();
        ResultMap resultMap = new ResultMap(type);
        return resultSetHandler.handleResultSets(pstmt,resultMap);
    }

    public <E> List<E> queryMapList(PreparedStatement pstmt, ResultHandler resultHandler,String ...columns) throws SQLException {
        pstmt.execute();
        ResultMap resultMap = new ResultMap(type,columns);
        return resultSetHandler.handleResultSets(pstmt,resultMap);
    }

    public <E> List<E> query(PreparedStatement pstmt, ResultHandler resultHandler,String ...columns) throws SQLException {
        pstmt.execute();
        ResultMap resultMap = new ResultMap(columns);
        return resultSetHandler.handleResultSets(pstmt,resultMap);
    }
}
