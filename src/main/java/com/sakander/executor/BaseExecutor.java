package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.model.Statement;

public class BaseExecutor implements Executor{
    @Override
    public CacheKey createCacheKey(Statement statement,String sql) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement.getRowRestriction().getOffset());
        cacheKey.update(statement.getRowRestriction().getLimit());
        cacheKey.update(sql);
        return cacheKey;
    }
}
