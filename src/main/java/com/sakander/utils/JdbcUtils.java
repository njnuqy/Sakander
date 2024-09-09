package com.sakander.utils;

import com.sakander.annotations.Column;
import com.sakander.config.DbSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtils {
    public static Connection getConn(){
        Connection conn = null;
        try {
            conn = DbSource.getConnection();
        }catch (Exception e){
            System.out.println("获取连接失败");
            e.printStackTrace();
        }
        return conn;
    }
    public static int excuteUpdate(String sql,Object ...params){
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = -1;
        try {
            conn = getConn();
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            release(pstmt);
        }
        return result;
    }
    public static <T> T excuteSelectOne(String sql,Class<T> clazz,Object ...params){
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
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt);
        }
        return result;
    }
    public static <T> List<T> excuteSelectInBatch(String sql, Class<T> clazz, Object ...params){
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
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt);
        }
        return result;
    }
    public static List<Map<String,Object>> excuteAggregate(String sql,Object ...params){
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
                    Object value = resultSet.getObject(columnName);
                    row.put(columnName, value);
                }
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            release(pstmt);
        }
        return result;
    }
    public static void release(Statement stmt){
        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private static <T> T resultSetToObject(ResultSet rs, Class<T> clazz) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        T obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(Column.class)){
                Column column = field.getAnnotation(Column.class);
                String columnName = column.name();
                Object value = rs.getObject(columnName);
                field.set(obj, value);
            }
        }
        return obj;
    }

}
