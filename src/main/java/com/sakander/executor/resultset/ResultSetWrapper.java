package com.sakander.executor.resultset;

import java.sql.ResultSet;

public class ResultSetWrapper {
    private final ResultSet resultSet;

    public ResultSetWrapper(ResultSet resultSet){
        this.resultSet = resultSet;
    }

    public ResultSet getResultSet(){
        return resultSet;
    }
}
