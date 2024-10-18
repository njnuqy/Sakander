package com.sakander.model;

import com.sakander.statement.Statement;

import java.sql.SQLException;
import java.util.List;

public interface DbPipe {
    <T> T selectOne(Class<?> type);

    <T> List<T> selectList(Class<?> type);

    int insert(Object object) throws SQLException;

    int update(Object object) throws SQLException;
}
