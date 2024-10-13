package com.sakander.executor.resultset;

import com.sakander.mapping.ResultMap;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface ResultSetHandler {

    <E> List<E> handleResultSets(PreparedStatement pstmt, ResultMap resultMap) throws SQLException;


}
