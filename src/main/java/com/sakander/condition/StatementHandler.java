package com.sakander.condition;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface StatementHandler {
    PreparedStatement prepare(Connection connection) throws SQLException;

    void parameterize(PreparedStatement pstmt,Condition condition) throws SQLException;

    int update(PreparedStatement statement) throws SQLException;

    <E> List<E> query(PreparedStatement statement) throws SQLException;

    <E> List<E> queryMap(PreparedStatement statement,QueryCondition condition) throws SQLException;
}
