package com.sakander.model;

import com.sakander.statement.Condition;
import com.sakander.statement.Statement;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface NewDbPipeInterface {
    <T> T selectOne(Condition condition);

    <T> List<T> selectList(Statement statement);

    int insert(Condition condition) throws SQLException;

    int update(Statement statement) throws SQLException;

    int delete(Statement statement);
}
