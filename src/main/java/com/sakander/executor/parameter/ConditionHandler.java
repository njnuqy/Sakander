package com.sakander.executor.parameter;

import com.sakander.condition.Condition;
import com.sakander.condition.UpdateCondition;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConditionHandler {
    void setParameters(PreparedStatement pstmt, Object[] paramters) throws SQLException;

    void setWhere(Condition condition, String query, Object ...params);

    void setInsert(UpdateCondition condition, Object object);

    void setUpdate(UpdateCondition condition,Object object);

    void setDelete(UpdateCondition condition, Object object);
}
