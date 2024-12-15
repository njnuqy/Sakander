package com.sakander.executor.parameter;

import com.sakander.clause.*;
import com.sakander.condition.Condition;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultConditionHandler implements ConditionHandler{
    @Override
    public void setParameters(PreparedStatement pstmt, Object[] parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++){
            pstmt.setObject(i+1, parameters[i]);
        }
    }
    @Override
    public void setWhere(Condition condition, String query, Object... params) {
        Where where = new Where(query,params);
        condition.setWhere(where);
    }

    @Override
    public void setInsert(UpdateCondition condition, Object object) {
        Insert insert = new Insert(object);
        condition.setInsert(insert);
    }

    @Override
    public void setDelete(UpdateCondition condition,Object object){
        Delete delete = new Delete(object);
        condition.setDelete(delete);
    }

    @Override
    public void setJoin(QueryCondition condition,String direction, String table) {
        Join join = new Join(direction,table);
        condition.setJoin(join);
    }

    @Override
    public void setOn(QueryCondition condition, String... ons) {
        On on = new On(ons);
        condition.setOn(on);
    }


    @Override
    public void setUpdate(UpdateCondition condition, Object object) {
        Update update = new Update(object);
        condition.setUpdate(update);
    }
}
