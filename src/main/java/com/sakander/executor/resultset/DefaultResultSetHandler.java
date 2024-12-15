package com.sakander.executor.resultset;

import com.sakander.executor.result.DefaultResultContext;
import com.sakander.executor.result.DefaultResultHandler;
import com.sakander.executor.type.TypeHandler;
import com.sakander.mapping.ResultMap;
import com.sakander.mapping.ResultMapping;
import com.sakander.reflection.factory.DefaultObejctFactory;
import com.sakander.reflection.factory.ObjectFactory;
import com.sakander.session.ResultHandler;
import sun.misc.Unsafe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetHandler implements ResultSetHandler{

    private final ObjectFactory objectFactory;

    public DefaultResultSetHandler() {
        this.objectFactory = new DefaultObejctFactory();
    }

    // 第一步调用
    @Override
    public List<Object> handleResultSets(PreparedStatement pstmt, ResultMap resultMap) throws SQLException {
        final List<Object> multipleResults = new ArrayList<>();
        ResultSetWrapper rsw = getFirstResultSet(pstmt);
        handleResultSet(rsw, resultMap,multipleResults);
        return collapseSingleResultList(multipleResults);
    }

    // 第二步调用
    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap,List<Object> multipleResults) throws SQLException {
        DefaultResultHandler defaultResultHandler = new DefaultResultHandler(objectFactory);
        handleRowValues(rsw, resultMap, defaultResultHandler);
        multipleResults.add(defaultResultHandler.getResultList());
    }

    // 第三步调用
    public void handleRowValues(ResultSetWrapper rsw,ResultMap resultMap,ResultHandler<?> resultHandler) throws SQLException {
        handleRowValuesForSimpleResultMap(rsw,resultMap,resultHandler);
        closeResultSet(rsw.getResultSet());
    }

    // 第四步调用：处理简单结果集
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

    // 第五步调用
    private Object getRowValue(ResultSetWrapper rsw,ResultMap resultMap){
        final Class<?> resultType = resultMap.getType();
        return createParameterizedResultObject(rsw,resultType,resultMap.getConstructorResultMappings());
    }

    Object createParameterizedResultObject(ResultSetWrapper rsw, Class<?> resultType, List<ResultMapping> constructorMappings){
        boolean foundValues = false;
        final List<Class<?>> constructorArgTypes = new ArrayList<>();
        final List<Object> constructorArgs = new ArrayList<>();
        final List<String> columnNames = new ArrayList<>();
        for(ResultMapping constructorMapping : constructorMappings){
            final Class<?> parameterType = constructorMapping.getJavaType();
            final String column = constructorMapping.getColumn();
            final TypeHandler<?> typeHandler = constructorMapping.getTypeHandler();
            final Object value = typeHandler.getResult(rsw.getResultSet(), column);
            constructorArgTypes.add(parameterType);
            constructorArgs.add(value);
            columnNames.add(column);
            foundValues = value != null || foundValues;
        }
        return foundValues ? objectFactory.create(resultType,columnNames,constructorArgTypes,constructorArgs) : null;
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
