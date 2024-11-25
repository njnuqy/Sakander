package com.sakander.executor.parameter;

import com.sakander.clause.Join;
import com.sakander.clause.On;
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
    public void setJoin(Statement statement, String direction, String table) {
        Join join = new Join(direction,table);
        statement.setJoin(join);
    }

    @Override
    public void setOn(Statement statement, String... ons) {
        On on = new On(ons);
        statement.setOn(on);
    }

}
