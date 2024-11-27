package com.sakander.model;

import com.sakander.condition.Condition;

import java.sql.SQLException;
import java.util.List;

public interface NewDbPipeInterface {
    <T> T selectOne(Condition condition);

    <T> List<T> selectList(Condition condition);

    int insert(Condition condition) throws SQLException;

    int update(Condition condition) throws SQLException;

    int delete(Condition condition);
}
