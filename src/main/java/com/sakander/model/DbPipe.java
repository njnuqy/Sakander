package com.sakander.model;

import com.sakander.statement.Statement;

public interface DbPipe {
    <T> T selectOne(Statement statement);
}
