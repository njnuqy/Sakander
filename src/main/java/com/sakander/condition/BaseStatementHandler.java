package com.sakander.condition;

import com.sakander.executor.Executor;
import com.sakander.executor.parameter.ConditionHandler;
import com.sakander.executor.parameter.DefaultConditionHandler;
import com.sakander.executor.resultset.DefaultResultSetHandler;
import com.sakander.executor.resultset.ResultSetHandler;
import com.sakander.expections.ExecutorException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseStatementHandler implements StatementHandler{
    protected final Executor executor;
    protected UpdateCondition updateCondition;
    protected QueryCondition queryCondition;
    protected final String sql;
    protected final Class<?> type;
    protected final ResultSetHandler resultSetHandler;
    protected final ConditionHandler conditionHandler;

    public BaseStatementHandler(Executor executor, UpdateCondition updateCondition,Class<?> type){
        this.executor = executor;
        this.updateCondition = updateCondition;
        this.sql = updateCondition.getSQL();
        this.type = type;
        this.resultSetHandler = new DefaultResultSetHandler();
        this.conditionHandler = new DefaultConditionHandler();
    }

    public BaseStatementHandler(Executor executor, QueryCondition queryCondition,Class<?> type){
        this.executor = executor;
        this.queryCondition = queryCondition;
        this.sql = queryCondition.getSQL();
        this.type = type;
        this.resultSetHandler = new DefaultResultSetHandler();
        this.conditionHandler = new DefaultConditionHandler();
    }

    @Override
    public PreparedStatement prepare(Connection connection) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = instantiatePreparedStatement(connection);
            return statement;
        }catch (SQLException e){
            closeStatement(statement);
            throw e;
        }catch (Exception e){
            closeStatement(statement);
            throw new ExecutorException("Error preparing statement. Cause:" + e, e);
        }
    }

    protected abstract PreparedStatement instantiatePreparedStatement(Connection connection) throws SQLException;

    protected void closeStatement(PreparedStatement statement){
        try {
            if(statement != null){
                statement.close();
            }
        }catch (SQLException e){
            // ignore
        }
    }

}
