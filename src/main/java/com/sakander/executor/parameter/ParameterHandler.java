package com.sakander.executor.parameter;

import com.sakander.statement.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {
    Object getParameterObject();

    void setParameters(PreparedStatement pstmt,Object[] paramters) throws SQLException;

    void setWhere(Statement statement,String query, Object ...params);

    void setJoin(Statement statement,String direction,String table);

    void setOn(Statement statement,String ...ons);
}
