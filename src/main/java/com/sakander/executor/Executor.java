package com.sakander.executor;

import com.sakander.cache.CacheKey;
import com.sakander.executor.resultset.ResultSetHandler;
import com.sakander.statement.Statement;
import com.sakander.session.ResultHandler;

import javax.swing.plaf.nimbus.State;
import java.sql.SQLException;
import java.util.List;

public interface Executor {
    ResultHandler NO_RESULT_HANDLER = null;

    int update(Statement statement) throws SQLException;

    <E> List<E> query(Statement statement, Object parameter, ResultHandler resultHandler,CacheKey cacheKey) throws SQLException;

    <E> List<E> query(Statement statement,Object parameter,ResultHandler resultHandler) throws SQLException;

    CacheKey createCacheKey(Statement statement);


}
