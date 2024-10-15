package com.sakander.model;

import com.sakander.clause.Where;
import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.executor.parameter.DefaultParameterHandler;
import com.sakander.session.ResultHandler;
import com.sakander.statement.Statement;
import com.sakander.utils.Utlis;

import java.sql.SQLException;
import java.util.List;

public class DefaultDbPipe implements DbPipe{
    private final Executor executor;
    private Statement statement;
    private final DefaultParameterHandler parameterHandler;

    public DefaultDbPipe(){
        this.executor = new SimpleExecutor();
        this.parameterHandler = new DefaultParameterHandler();
        statement = new Statement();
    }

    @Override
    public <T> T selectOne(Class<?> type) {
        List<T> list = selectList(Executor.NO_RESULT_HANDLER,type);
        return list.get(0);
    }

    @Override
    public <T> List<T> selectList(Class<?> type) {
        return this.selectList(Executor.NO_RESULT_HANDLER,type);
    }

    private <E> List<E> selectList(ResultHandler resultHandler,Class<?> type) {
        String sql = SqlBuilder.getEasySelectSql(this.statement);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        statement.setSQL(sql);
        statement.setParameters(params);
        try{
            return executor.query(statement,resultHandler,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    public DefaultDbPipe where(String query, Object ...params){
        parameterHandler.setWhere(statement,query,params);
        return this;
    }

    public DefaultDbPipe from(String from){
        parameterHandler.setFrom(statement,from);
        return this;
    }
}
