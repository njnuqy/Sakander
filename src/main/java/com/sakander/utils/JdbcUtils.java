package com.sakander.utils;

import com.sakander.config.DbSource;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class JdbcUtils {
    public static Connection getConn(){
        Connection conn = null;
        try {
            conn = DbSource.getConnection();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return conn;
    }
    public static int excuteUpdate(String sql,Object ...params){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = -1;
        try {
            conn = getConn();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            result = pstmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            release(pstmt,conn);
        }
        return result;
    }
    public static <T> T executeSelectOne(String sql, Class<T> clazz, Object ...params){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        T result = null;
        try {
            conn = getConn();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                result = resultSetToObject(resultSet, clazz);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt,conn,resultSet);
        }
        return result;
    }
    public static <T> List<T> executeSelectInBatch(String sql, Class<T> clazz, Object ...params){
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<T> result = new ArrayList<>();
        try {
            conn = getConn();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                result.add(resultSetToObject(resultSet, clazz));
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException |
                 NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt,conn,resultSet);
        }
        return result;
    }
    public static List<Map<String,Object>> executeAggregate(String sql, Object ...params){
        List<Map<String, Object>> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            conn = getConn();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String aliasName = metaData.getColumnLabel(i); // 如果JDBC驱动支持，这将返回别名
                    if (aliasName == null || aliasName.isEmpty()) {
                        aliasName = columnName; // 如果别名不存在或为空，则使用原始列名
                    }
                    Object value = resultSet.getObject(i); // 使用索引来获取值
                    row.put(aliasName, value); // 使用别名作为Map的键
                }
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt,conn,resultSet);
        }
        return result;
    }
    public static List<Map<String,Object>> executeSelectWithColumns(String sql,String []columns,Object ...params){
        List<Map<String,Object>> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            conn = getConn();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                result.add(resultSetToMap(resultSet, columnCount, metaData));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt,conn,resultSet);
        }
        return result;
    }
    public static void release(Statement stmt,Connection conn,ResultSet rs){
        release(stmt, conn);
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.warn("resultSet close error,{}",e.getMessage());
            }
        }
    }
    public static void release(Statement stmt,Connection conn){
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("stmt close error,{}",e.getMessage());
            }
        }
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.warn("conn close error,{}",e.getMessage());
            }
        }
    }
    private static <T> T resultSetToObject(ResultSet rs, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        T obj = clazz.getDeclaredConstructor().newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = Utlis.getColumnName(field);
            Object value = rs.getObject(columnName);
            field.set(obj, value);
        }
        return obj;
    }
    private static Map<String,Object> resultSetToMap(ResultSet resultSet,int columnCount,ResultSetMetaData metaData) throws SQLException{
        Map<String, Object> row = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String aliasName = metaData.getColumnLabel(i); // 如果JDBC驱动支持，这将返回别名
            if (aliasName == null || aliasName.isEmpty()) {
                aliasName = columnName; // 如果别名不存在或为空，则使用原始列名
            }
            Object value = resultSet.getObject(i); // 使用索引来获取值
            row.put(aliasName, value); // 使用别名作为Map的键
        }
        return row;
    }
}
