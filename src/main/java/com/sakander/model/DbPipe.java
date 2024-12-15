package com.sakander.model;

import com.sakander.clause.Alias;
import com.sakander.condition.Condition;
import jakarta.annotation.Nullable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DbPipe {
    <T> T selectOne(Condition condition);

    <T> List<T> selectList(Condition condition);

    List<Map<String,Object>> selectMap(Condition condition, @Nullable Alias alias);

    int insert(Condition condition) throws SQLException;

    int update(Condition condition) throws SQLException;

    int delete(Condition condition);
}
