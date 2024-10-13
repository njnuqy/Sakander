package com.sakander.executor.parameter;

import com.sakander.clause.Table;
import com.sakander.clause.Where;
import com.sakander.statement.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DefaultParameterHandler implements ParameterHandler{
    @Override
    public Object getParameterObject() {
        return null;
    }

    @Override
    public void setParameters(PreparedStatement pstmt,Object[] paramters) throws SQLException {
        for (int i = 0; i < paramters.length; i++){
            pstmt.setObject(i+1, paramters[i]);
        }
    }

    @Override
    public void setWhere(Statement statement,String query, Object... params) {
        Where where = new Where(query,params);
        statement.setWhere(where);
    }

    @Override
    public void setFrom(Statement statement,String from) {
        Table table = new Table(from);
        statement.setTable(table);
    }
}
