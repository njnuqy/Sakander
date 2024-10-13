package com.sakander.executor.parameter;

import com.sakander.statement.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {
    Object getParameterObject();

    void setParameters(PreparedStatement pstmt,Object[] paramters) throws SQLException;

    void setWhere(Statement statement,String query, Object ...params);

    void setFrom(Statement statement,String from);
}
