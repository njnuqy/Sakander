package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.statement.Statement;
import com.sakander.session.ResultHandler;

public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    int update(Statement statement);

    CacheKey createCacheKey(Statement statement,String sql);


}
