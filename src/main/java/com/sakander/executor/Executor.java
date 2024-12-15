package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    int update(UpdateCondition condition, Class<?> type) throws SQLException;

    <E> List<E> query(QueryCondition condition,Class<?> type) throws SQLException;

    <E> List<E> queryMap(QueryCondition condition,Class<?> type) throws SQLException;

    CacheKey createCacheKey(QueryCondition condition);
}
