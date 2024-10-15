package com.sakander.model;

import com.sakander.statement.Statement;

import java.util.List;

public interface DbPipe {
    <T> T selectOne(Class<?> type);

    <T> List<T> selectList(Class<?> type);
}
