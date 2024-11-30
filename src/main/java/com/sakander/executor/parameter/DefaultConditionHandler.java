package com.sakander.executor.parameter;

import com.sakander.clause.Delete;
import com.sakander.clause.Insert;
import com.sakander.clause.Update;
import com.sakander.clause.Where;
import com.sakander.condition.Condition;
import com.sakander.condition.UpdateCondition;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultConditionHandler implements ConditionHandler{
    @Override
    public void setParameters(PreparedStatement pstmt, Object[] paramters) throws SQLException {
        for (int i = 0; i < paramters.length; i++){
            pstmt.setObject(i+1, paramters[i]);
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
    public void setUpdate(UpdateCondition condition, Object object) {
        Update update = new Update(object);
        condition.setUpdate(update);
    }
}
