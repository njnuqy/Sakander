package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.cache.impl.PerpetualCache;
import com.sakander.session.ResultHandler;
import com.sakander.statement.Statement;
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
    public int update(Statement statement,Class<?> type) throws SQLException {
        // TODO 缓存相关
        return doUpdate(statement,type);
    }

    @Override
    public CacheKey createCacheKey(Statement statement) {
        CacheKey cacheKey = new CacheKey();
//        cacheKey.update(statement.getRowRestriction().getOffset());
//        cacheKey.update(statement.getRowRestriction().getLimit());
        cacheKey.update(statement.getSQL());
        cacheKey.update(statement);
        return cacheKey;
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler,Class<?> type) throws SQLException {
        CacheKey key = createCacheKey(statement);
        return query(statement,resultHandler,key,type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler, CacheKey cacheKey,Class<?> type) throws SQLException {
        List<E> list = (List<E>) localCache.getObejct(cacheKey);
        if(list != null){
            return list;
        }
        list = queryFromDatabase(statement,resultHandler,cacheKey,type);
        return list;
    }

    private <E> List<E> queryFromDatabase(Statement statement,ResultHandler resultHandler,CacheKey cacheKey,Class<?> type) throws SQLException {
        List<E> list;
        try {
            list = doQuery(statement,resultHandler,type);
        }finally {
            localCache.removeObejct(cacheKey);
        }
        localCache.putObject(cacheKey,list);
        return list;
    }

    protected abstract int doUpdate(Statement statement,Class<?> type) throws SQLException;

    protected abstract <E> List<E> doQuery(Statement statement, ResultHandler resultHandler,Class<?> type) throws SQLException;
}
