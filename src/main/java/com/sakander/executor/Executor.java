package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.model.Statement;
import com.sakander.session.ResultHandler;

public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    CacheKey createCacheKey(Statement statement,String sql);
}
