package com.sakander.model;

import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.executor.parameter.DefaultParameterHandler;
import com.sakander.session.ResultHandler;
import com.sakander.condition.Statement;
import com.sakander.utils.Utils;

import java.util.List;
import java.util.Map;

public class DefaultDbPipe implements DbPipe{
    private final Executor executor;
    private Statement statement;
    private final DefaultParameterHandler parameterHandler;
    private Class<?> type;
    public DefaultDbPipe(String tableName){
        this.executor = new SimpleExecutor();
        this.parameterHandler = new DefaultParameterHandler();
        statement = new Statement();
        statement.getTable().setTableName(tableName);
    }

    public DefaultDbPipe(Class<?> clazz){
        this.executor = new SimpleExecutor();
        this.parameterHandler = new DefaultParameterHandler();
        statement = new Statement();
        this.type = clazz;
    }

    @Override
    public <T> T selectOne(Class<?> type) {
        List<T> list = selectList(Executor.NO_RESULT_HANDLER, type);
        return list.get(0);
    }

    @Override
    public <T> List<T> selectList(Class<?> type) {
        List<Object> objects = this.selectList(Executor.NO_RESULT_HANDLER, type);
        clear();
        return (List<T>) objects;
    }

    @Override
    public List<Map<String, Object>> selectMapList(Class<?> type,String ...columns) {
        return selectMapList(Executor.NO_RESULT_HANDLER,type,columns);
    }

    @Override
    public List<Map<String, Object>> querySql(String sql,String ...columns) {
        statement.setSQL(sql);
        return selectQuerySql(Executor.NO_RESULT_HANDLER,sql,columns);
    }

    private <E> List<E> selectQuerySql(ResultHandler resultHandler,String sql,String ...columns) {
        statement.setSQL(sql);
        try{
            return executor.query(statement,resultHandler,columns);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    private <E> List<E> selectList(ResultHandler resultHandler,Class<?> type) {
        String sql = SqlBuilder.getSelectSql(this.statement);
        Object[] params = Utils.mergeArrays(this.statement.getWhere().getParams());
        System.out.println(sql);
        statement.setSQL(sql);
        statement.setParameters(params);
        try{
            return executor.query(statement,resultHandler,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }


    private <E> List<E> selectMapList(ResultHandler resultHandler,Class<?> type,String ...columns) {
        String sql = SqlBuilder.getSelectWithColumnsSql(this.statement,columns);
        Object[] params = Utils.mergeArrays(this.statement.getWhere().getParams());
        statement.setSQL(sql);
        statement.setParameters(params);
        try{
            return executor.query(statement,resultHandler,type,columns);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    @Override
    public int insert(Object object){
        String sql = SqlBuilder.getInsertSql(statement,object);
        Object[] params = Utils.getSqlParams(object);
        statement.setSQL(sql);
        for (Object param : params) {
            System.out.println(param);
        }
        try {
            return executor.update(statement,object.getClass());
        }catch (Exception e){
            throw new RuntimeException("Error update database. Cause: " + e,e);
        }
    }

    @Override
    public int update(Object object){
        String sql = SqlBuilder.getUpdateSql(statement,object);
        Object[] params = Utils.getUpdateParams(object);
        statement.setSQL(sql);
        statement.setParameters(params);
        try {
            return executor.update(statement,object.getClass());
        }catch (Exception e){
            throw new RuntimeException("Error update database. Cause: " + e,e);
        }
    }

    @Override
    public int delete(Object object) {
        String sql = SqlBuilder.getDeleteSql(statement);
        Object param = Utils.getIdParam(object);
        statement.setSQL(sql);
        statement.setParameters(new Object[]{param});
        try {
            return executor.update(statement,object.getClass());
        }catch (Exception e){
            throw new RuntimeException("Error delete database. Cause:" + e,e);
        }
    }

    public DefaultDbPipe where(String query, Object ...params){
        parameterHandler.setWhere(statement,query,params);
        return this;
    }

    private void clear(){
        statement = new Statement(statement.getTable().getTableName());
    }
}
