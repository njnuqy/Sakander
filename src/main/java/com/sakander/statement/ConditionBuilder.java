package com.sakander.statement;


import com.sakander.executor.parameter.DefaultConditionHandler;

public class ConditionBuilder {
    private Condition condition;
    private final DefaultConditionHandler conditionHandler;

    public ConditionBuilder(){
        this.conditionHandler = new DefaultConditionHandler();
    }

    public ConditionBuilder query(){
        this.condition = new QueryCondition();
        return this;
    }

    public ConditionBuilder update(){
        this.condition = new UpdateCondition();
        return this;
    }

    public ConditionBuilder where(String query,Object ...params){
        conditionHandler.setWhere(condition,query,params);
        return this;
    }

    public ConditionBuilder insert(Object object){
        conditionHandler.setInsert((UpdateCondition)condition,object);
        return this;
    }

    public Condition build(){
        return condition;
    }
}
