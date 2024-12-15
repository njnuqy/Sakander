package com.sakander.model;

import com.sakander.clause.Alias;
import com.sakander.constants.GlobalDbPipe;
import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.condition.Condition;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;
import com.sakander.utils.Utils;

import java.util.List;
import java.util.Map;

public class DefaultDbPipe implements DbPipe {
    private final Executor executor;
    private final Class<?> type;

    public DefaultDbPipe(Class<?> clazz){
        this.executor = new SimpleExecutor();
        this.type = clazz;
        GlobalDbPipe.addClassToMap(clazz.getSimpleName().toLowerCase(),clazz);
    }

    @Override
    public <T> T selectOne(Condition condition) {
        condition.getTable().setTableName(Utils.getTableName(type));
        QueryCondition queryCondition = (QueryCondition) condition;
        List<T> list = selectList(queryCondition,this.type);
        return list.get(0);
    }

    @Override
    public <T> List<T> selectList(Condition condition) {
        condition.getTable().setTableName(Utils.getTableName(type));
        QueryCondition queryCondition = (QueryCondition) condition;
        List<Object> objects = this.selectList(queryCondition,this.type);
        return (List<T>) objects;
    }

    @Override
    public List<Map<String, Object>> selectMap(Condition condition,Alias alias) {
        condition.getTable().setTableName(Utils.getTableName(type));
        QueryCondition queryCondition = (QueryCondition) condition;
        return this.selectListMap(queryCondition, this.type,alias);
    }

    @Override
    public int insert(Condition condition){
        condition.getTable().setTableName(Utils.getTableName(type));
        UpdateCondition updateCondition = (UpdateCondition) condition;
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
    public int update(Condition condition){
        condition.getTable().setTableName(Utils.getTableName(type));
        UpdateCondition updateCondition = (UpdateCondition) condition;
        SqlBuilder.prepareUpdate(updateCondition);
        System.out.println(updateCondition.getSQL());
        for (Object parameter : condition.getParameters()) {
            System.out.println(parameter);
        }
        try {
            return executor.update(updateCondition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    @Override
    public int delete(Condition condition) {
        condition.getTable().setTableName(Utils.getTableName(type));
        UpdateCondition updateCondition = (UpdateCondition) condition;
        SqlBuilder.prepareDelete(updateCondition);
        System.out.println(updateCondition.getSQL());
        for (Object parameter : condition.getParameters()) {
            System.out.println(parameter);
        }
        try {
            return executor.update(updateCondition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    private <E> List<E> selectList(QueryCondition condition,Class<?> type) {
        String sql = SqlBuilder.getSelectSql(condition);
        condition.setSQL(sql);
        try{
            return executor.query(condition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

    private <E> List<E> selectListMap(QueryCondition condition,Class<?> type,Alias alias) {
        String sql = SqlBuilder.getSelectMapSql(condition,alias);
        condition.setSQL(sql);
        condition.setAlias(alias);
        try{
            return executor.queryMap(condition,type);
        }catch (Exception e){
            throw new RuntimeException("Error query database . Cause: " + e,e);
        }
    }

}
