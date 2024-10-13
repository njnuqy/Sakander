package com.sakander.model;

import com.sakander.executor.Executor;
import com.sakander.executor.SimpleExecutor;
import com.sakander.statement.Statement;

public class DefaultDbPipe implements DbPipe{
    private final Executor executor;

    public DefaultDbPipe(){
        this.executor = new SimpleExecutor();
    }

    @Override
    public <T> T selectOne(Statement statement) {
        return null;
    }
}
