package com.sakander.condition;

import com.sakander.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface StatementHandler {
    PreparedStatement prepare(Connection connection) throws SQLException;

    void parameterize(PreparedStatement pstmt) throws SQLException;

    void newParameterize(PreparedStatement pstmt) throws SQLException;

    void newParameterize(PreparedStatement pstmt,Condition condition) throws SQLException;

    int update(PreparedStatement statement) throws SQLException;

    <E> List<E> query(PreparedStatement statement, ResultHandler resultHandler) throws SQLException;

    <E> List<E> query(PreparedStatement statement, ResultHandler resultHandler,String ...columns) throws SQLException;

    <E> List<E> query(PreparedStatement statement) throws SQLException;

    <E> List<E> queryMapList(PreparedStatement statement, ResultHandler resultHandler,String ...columns) throws SQLException;
}
