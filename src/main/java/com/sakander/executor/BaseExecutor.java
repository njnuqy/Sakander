package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.cache.impl.PerpetualCache;
import com.sakander.session.ResultHandler;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseExecutor implements Executor{
    private static final Logger log = LoggerFactory.getLogger(BaseExecutor.class);
    protected PerpetualCache localCache;

    protected BaseExecutor(){
        this.localCache = new PerpetualCache("LocalCache");
    }

    @Override
    public int update(UpdateCondition condition, Class<?> type) throws SQLException {
        localCache.clear();
        return doUpdate(condition,type);
    }

    @Override
    public CacheKey createCacheKey(QueryCondition condition) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(condition.getSQL());
        cacheKey.update(condition);
        return cacheKey;
    }

    @Override
    public <E> List<E> query(QueryCondition condition, Class<?> type) throws SQLException {
        CacheKey cacheKey = createCacheKey(condition);
        List<E> list = (List<E>) localCache.getObejct(cacheKey);
        if(list != null){
            return list;
        }
        list = queryFromDatabase(condition,cacheKey,type);
        return list;
    }

    private <E> List<E> queryFromDatabase(QueryCondition condition, CacheKey cacheKey, Class<?> type) throws SQLException {
        List<E> list;
        try {
            list = doQuery(condition,type);
        }finally {
            localCache.removeObejct(cacheKey);
        }
        localCache.putObject(cacheKey,list);
        return list;
    }

    protected abstract int doUpdate(UpdateCondition condition,Class<?> type) throws SQLException;

    protected abstract <E> List<E> doQuery(QueryCondition condition,Class<?> type) throws SQLException;
}
