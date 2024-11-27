package com.sakander.model;

import com.sakander.annotations.TableAdd;
import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.condition.Condition;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;
import com.sakander.utils.Utils;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
@Component
public class NewDbPipe implements NewDbPipeInterface{
    private final Executor executor;
    private final Class<?> type;

    public NewDbPipe(Class<?> clazz){
        this.executor = new SimpleExecutor();
        this.type = clazz;
    }

    @Override
    @TableAdd
    public <T> T selectOne(Condition condition) {
//        condition.getTable().setTableName(Utils.getTableName(type));
        QueryCondition queryCondition = (QueryCondition) condition;
        List<T> list = selectList(queryCondition,this.type);
        return list.get(0);
    }

    @Override
    public <T> List<T> selectList(Condition condition) {
        condition.getTable().setTableName(Utils.getTableName(type));
        QueryCondition queryCondition = (QueryCondition) condition;
        condition.getTable().setTableName(Utils.getTableName(type));
        List<Object> objects = this.selectList(queryCondition,this.type);
        return (List<T>) objects;
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
    public int update(Condition condition) throws SQLException {
        UpdateCondition updateCondition = (UpdateCondition) condition;
        return 0;
    }

    @Override
    public int delete(Condition condition) {
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

}
