package com.sakander.condition;

import com.sakander.executor.Executor;
import com.sakander.mapping.ResultMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PreparedStatementHandler extends BaseStatementHandler{
    public PreparedStatementHandler(Executor executor, UpdateCondition condition,Class type) {
        super(executor,condition,type);
    }

    public PreparedStatementHandler(Executor executor, QueryCondition condition,Class type) {
        super(executor,condition,type);
    }
    @Override
    protected PreparedStatement instantiatePreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(PreparedStatement pstmt,Condition condition) throws SQLException {
        conditionHandler.setParameters(pstmt,condition.getParameters());
    }

    @Override
    public int update(PreparedStatement pstmt) throws SQLException {
        pstmt.execute();
        return pstmt.getUpdateCount();
    }

    @Override
    public <E> List<E> query(PreparedStatement pstmt) throws SQLException {
        pstmt.execute();
        ResultMap resultMap = new ResultMap(type);
        return resultSetHandler.handleResultSets(pstmt,resultMap);
    }

    @Override
    public <E> List<E> queryMap(PreparedStatement pstmt,QueryCondition condition) throws SQLException {
        pstmt.execute();
        ResultMap resultMap = new ResultMap(type,condition);
        return resultSetHandler.handleResultSets(pstmt,resultMap);
    }
}
