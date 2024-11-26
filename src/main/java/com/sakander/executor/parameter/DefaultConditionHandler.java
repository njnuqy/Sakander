package com.sakander.executor.parameter;

import com.sakander.clause.Insert;
import com.sakander.clause.Where;
import com.sakander.statement.Condition;
import com.sakander.statement.QueryCondition;
import com.sakander.statement.Statement;
import com.sakander.statement.UpdateCondition;

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
}
