package com.sakander.statement;

import com.sakander.executor.Executor;
import com.sakander.executor.parameter.DefaultParameterHandler;
import com.sakander.executor.parameter.ParameterHandler;
import com.sakander.executor.resultset.DefaultResultSetHandler;
import com.sakander.executor.resultset.ResultSetHandler;
import com.sakander.expections.ExecutorException;
import jakarta.annotation.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseStatementHandler implements StatementHandler{
    protected final Executor executor;
    protected final Statement statement;
    protected final String sql;
    protected final ResultSetHandler resultSetHandler;
    protected final ParameterHandler parameterHandler;
    public BaseStatementHandler(Executor executor,Statement statement,String sql){
        this.executor = executor;
        this.statement = statement;
        this.sql = sql;
        this.resultSetHandler = new DefaultResultSetHandler();
        this.parameterHandler = new DefaultParameterHandler();
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

    protected void setPrepareStatementTimeout(PreparedStatement statement,Integer transactionTimeout) throws SQLException {
        if(transactionTimeout == null){
            return;
        }
        statement.setQueryTimeout(transactionTimeout);
    }

    protected void closeStatement(PreparedStatement statement){
        try {
            if(statement != null){
                statement.close();
            }
        }catch (SQLException e){
            // ignore
        }
    }
    protected void setFetchSize(PreparedStatement statement,Integer fetchSize) throws SQLException {
        if(fetchSize == null){
            return;
        }
        statement.setFetchSize(fetchSize);
    }
}
