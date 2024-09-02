package com.sakander.utils;

import com.sakander.config.DatabaseConfig;

import java.io.IOException;
import java.sql.*;

public class JdbcUtils {
    public static Connection getConn(DatabaseConfig config){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(config.getUrl(),config.getUsername(),config.getPassword());
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
            DatabaseConfig config = new DatabaseConfig();
            conn = getConn(config);
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++){
                pstmt.setObject(i+1, params[i]);
            }
            result = pstmt.executeUpdate();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            release(pstmt,conn);

        }
        return result;
    }
    public static void release(Statement stmt,Connection conn){
        if(stmt != null){
            try{
                stmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            stmt = null;
        }
        if(conn != null){
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
