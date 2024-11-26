package com.sakander.executor.parameter;

import com.sakander.statement.Condition;
import com.sakander.statement.QueryCondition;
import com.sakander.statement.Statement;
import com.sakander.statement.UpdateCondition;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ConditionHandler {
    void setParameters(PreparedStatement pstmt, Object[] paramters) throws SQLException;

    void setWhere(Condition condition, String query, Object ...params);

    void setInsert(UpdateCondition condition, Object object);
}
