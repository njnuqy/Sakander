package com.sakander.executor.resultset;

import com.sakander.executor.result.DefaultResultContext;
import com.sakander.executor.result.DefaultResultHandler;
import com.sakander.mapping.ResultMap;
import com.sakander.refection.DefaultReflectorFactory;
import com.sakander.refection.ReflectorFactory;
import com.sakander.refection.factory.DefaultObejctFactory;
import com.sakander.refection.factory.ObjectFactory;
import com.sakander.session.ResultHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler{

    private final ResultHandler<?> resultHandler;
    private final ReflectorFactory reflectorFactory;
    private final ObjectFactory objectFactory;

    public DefaultResultSetHandler() {
        this.resultHandler = new DefaultResultHandler();
        this.reflectorFactory = new DefaultReflectorFactory();
        this.objectFactory = new DefaultObejctFactory();
    }
    // 已经执行过了 execute 方法
    @Override
    public List<Object> handleResultSets(PreparedStatement pstmt,ResultMap resultMap) throws SQLException {
        final List<Object> multipleResults = new ArrayList<>();
        ResultSetWrapper rsw = getFirstResultSet(pstmt);
        handleResultSet(rsw, resultMap,multipleResults);
        return collapseSingleResultList(multipleResults);
    }

    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap,List<Object> multipleResults) throws SQLException {
        DefaultResultHandler defaultResultHandler = new DefaultResultHandler(objectFactory);
        handleRowValues(rsw, resultMap, defaultResultHandler);
        multipleResults.add(defaultResultHandler.getResultList());
    }

    public void handleRowValues(ResultSetWrapper rsw,ResultMap resultMap,ResultHandler<?> resultHandler) throws SQLException {
        handleRowValuesForSimpleResultMap(rsw,resultMap,resultHandler);
        closeResultSet(rsw.getResultSet());
    }

    // 处理简单结果集
    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw,ResultMap resultMap,ResultHandler<?> resultHandler) throws SQLException {
        DefaultResultContext<Object> resultContext = new DefaultResultContext<>();
        ResultSet resultSet = rsw.getResultSet();
        while(resultSet.next()){
            Object rowValue = getRowValue(rsw,resultMap);
            storeObject(resultHandler,resultContext,rowValue);
        }
    }

    private void storeObject(ResultHandler<?> resultHandler,DefaultResultContext<Object> resultContext,Object rowValue){
        callResultHandler(resultHandler,resultContext,rowValue);
    }

    @SuppressWarnings("unchecked" /* because ResultHandler<?> is always ResultHandler<Object> */)
    private void callResultHandler(ResultHandler<?> resultHandler,DefaultResultContext<Object> resultContext,Object rowValue){
        resultContext.nextResultObejct(rowValue);
        ((ResultHandler<Object>) resultHandler).handleResult(resultContext);
    }


    private Object getRowValue(ResultSetWrapper rsw,ResultMap resultMap){
        Object rowValue = createResultObject(rsw,resultMap);
        return rowValue;
    }

    private Object createResultObject(ResultSetWrapper rsw,ResultMap resultMap){
        final Class<?> resultType = resultMap.getType();
        return objectFactory.create(resultType);
    }

    private ResultSetWrapper getFirstResultSet(PreparedStatement pstmt) throws SQLException {
        ResultSet rs = pstmt.getResultSet();
        while(rs == null){
            if(pstmt.getMoreResults()){
                rs = pstmt.getResultSet();
            }else if(pstmt.getUpdateCount() == -1){
                break;
            }
        }
        return rs != null ? new ResultSetWrapper(rs) : null;
    }

    private void closeResultSet(ResultSet rs){
        try{
            if(rs != null){
                rs.close();
            }
        }catch (SQLException e){
            // ignore
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> collapseSingleResultList(List<Object> multipleResults) {
        return multipleResults.size() == 1 ? (List<Object>) multipleResults.get(0) : multipleResults;
    }
}
