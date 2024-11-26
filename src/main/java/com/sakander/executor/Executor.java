package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.statement.Condition;
import com.sakander.statement.QueryCondition;
import com.sakander.statement.Statement;
import com.sakander.session.ResultHandler;
import com.sakander.statement.UpdateCondition;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    int update(Statement statement,Class<?> type) throws SQLException;

    int update(UpdateCondition condition, Class<?> type) throws SQLException;

    <E> List<E> query(Statement statement, ResultHandler resultHandler,CacheKey cacheKey,Class<?> type) throws SQLException;

    <E> List<E> query(Statement statement,ResultHandler resultHandler,Class<?> type) throws SQLException;

    <E> List<E> query(Statement statement,ResultHandler resultHandler,Class<?> type,String ...columns) throws SQLException;

    <E> List<E> query(Statement statement,ResultHandler resultHandler,String ...columns) throws SQLException;

    <E> List<E> query(QueryCondition condition,Class<?> type) throws SQLException;

    CacheKey createCacheKey(Statement statement);


}
