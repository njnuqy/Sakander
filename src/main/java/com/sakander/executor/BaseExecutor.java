package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.statement.Statement;

import java.sql.SQLException;

public abstract class BaseExecutor implements Executor{
    @Override
    public int update(Statement statement) {
        return doUpdate(statement);
    }

    @Override
    public CacheKey createCacheKey(Statement statement,String sql) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement.getRowRestriction().getOffset());
        cacheKey.update(statement.getRowRestriction().getLimit());
        cacheKey.update(sql);
        cacheKey.update(statement);
        return cacheKey;
    }

    protected abstract int doUpdate(Statement statement) throws SQLException;
}
