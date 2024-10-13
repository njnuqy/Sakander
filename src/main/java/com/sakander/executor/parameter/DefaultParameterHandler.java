package com.sakander.executor.parameter;

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
}
