package com.sakander.model;

import com.sakander.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static Database instance;
    private Connection connection;
    private Database(DatabaseConfig config) throws SQLException {
        this.connection = DriverManager.getConnection(
                config.getUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }
    public static synchronized Database connect(DatabaseConfig config) throws SQLException {
        if(instance == null || instance.getConnection().isClosed()){
            instance = new Database(config);
        }
        return instance;
    }
    public static Database getInstance() throws IllegalAccessException {
        if (instance == null) {
            throw new IllegalAccessException("DB Instance is not initialized");
        }
        return instance;
    }
    // 获取当前连接
    public Connection getConnection(){
        return connection;
    }
    public void executeQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
        }
    }
    public List<String[]> executeSelectQuery(String query) throws SQLException {
        List<String[]> results = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // 获取结果集的元数据
            int columnCount = rs.getMetaData().getColumnCount();

            // 迭代结果集
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                results.add(row);
            }
        }
        return results;
    }
    // 执行查询并返回结果
    public List<String[]> executeQueryWithRowsName(String query) throws SQLException {
        List<String[]> results = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 获取列名
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }
            results.add(columnNames);

            // 迭代结果集
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getString(i);
                }
                results.add(row);
            }
        }
        return results;
    }
    // 关闭连接
    public void close() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }
}
