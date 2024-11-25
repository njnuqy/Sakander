package com.sakander.statement;

import com.sakander.executor.parameter.DefaultParameterHandler;

public class StatementBuilder {
    private final Statement statement;
    private final DefaultParameterHandler parameterHandler;

    public StatementBuilder() {
        this.statement = new Statement();
        this.parameterHandler = new DefaultParameterHandler();
    }

    public StatementBuilder where(String query, Object ...params){
        parameterHandler.setWhere(statement,query,params);
        return this;
    }

    public Statement build(){
        return statement;
    }

}
