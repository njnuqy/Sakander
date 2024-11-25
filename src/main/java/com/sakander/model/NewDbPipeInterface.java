package com.sakander.model;

import com.sakander.statement.Statement;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface NewDbPipeInterface {
    <T> T selectOne(Statement statement);

    <T> List<T> selectList(Statement statement);

//    List<Map<String,Object>> selectMapList(Class<?> type,String ...columns);
//
//    List<Map<String,Object>> querySql(String sql,String ...columns);
//
//    int insert(Object object) throws SQLException;
//
//    int update(Object object) throws SQLException;
//
//    int delete(Object object);
}
