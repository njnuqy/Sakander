package com.sakander.model;

import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.executor.parameter.DefaultParameterHandler;
import com.sakander.statement.Condition;
import com.sakander.statement.QueryCondition;
import com.sakander.statement.Statement;
import com.sakander.statement.UpdateCondition;
import com.sakander.utils.Utils;
import org.hibernate.sql.Update;

import java.sql.SQLException;
import java.util.List;

public class NewDbPipe implements NewDbPipeInterface{
    private final Executor executor;
    private final DefaultParameterHandler parameterHandler;
    private final Class<?> type;

    public NewDbPipe(Class<?> clazz){
        this.executor = new SimpleExecutor();
        this.parameterHandler = new DefaultParameterHandler();
        this.type = clazz;
    }
    @Override
    public <T> T selectOne(Condition condition) {
        QueryCondition queryCondition = (QueryCondition) condition;
        queryCondition.getTable().setTableName(Utils.getTableName(type));
        List<T> list = selectList(queryCondition,this.type);
        return list.get(0);
    }
    @Override
    public <T> List<T> selectList(Statement statement) {
        statement.getTable().setTableName(Utils.getTableName(type));
        List<Object> objects = this.selectList(statement,this.type);
        return (List<T>) objects;
    }
    @Override
    public int insert(Condition condition){
        UpdateCondition updateCondition = (UpdateCondition) condition;
        condition.getTable().setTableName(Utils.getTableName(type));
        String sql = SqlBuilder.getInsertSql(updateCondition);
        Object[] params = Utils.getSqlParams(updateCondition.getInsert().getObject());
        for (Object param : params) {
            System.out.println(param);
        }
        updateCondition.setSQL(sql);
        System.out.println(updateCondition.getSQL());
        updateCondition.setParameters(params);
        try{
            return executor.update(updateCondition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }
    @Override
    public int update(Statement statement) throws SQLException {
        return 0;
    }
    @Override
    public int delete(Statement statement) {
        return 0;
    }

    private <E> List<E> selectList(QueryCondition condition,Class<?> type) {
        String sql = SqlBuilder.getSelectSql(condition);
        Object[] params = Utils.mergeArrays(condition.getWhere().getParams());
        System.out.println(sql);
        condition.setSQL(sql);
        condition.setParameters(params);
        try{
            return executor.query(condition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    private <E> List<E> selectList(Statement statement,Class<?> type) {
        String sql = SqlBuilder.getSelectSql(statement);
        Object[] params = Utils.mergeArrays(statement.getWhere().getParams());
        System.out.println(sql);
        statement.setSQL(sql);
        statement.setParameters(params);
        try{
            return executor.query(statement,null,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }



}
