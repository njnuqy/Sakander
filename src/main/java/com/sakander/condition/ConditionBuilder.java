package com.sakander.condition;


import com.sakander.executor.parameter.DefaultConditionHandler;

public class ConditionBuilder {
    private Condition condition;
    private final DefaultConditionHandler conditionHandler;

    public ConditionBuilder(){
        this.conditionHandler = new DefaultConditionHandler();
    }
    // declare query condition
    public ConditionBuilder query(){
        this.condition = new QueryCondition();
        return this;
    }
    // declare update condition
    public ConditionBuilder update(){
        this.condition = new UpdateCondition();
        return this;
    }

    public ConditionBuilder change(Object object){
        conditionHandler.setUpdate((UpdateCondition) condition,object);
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

    public ConditionBuilder delete(Object object){
        conditionHandler.setDelete((UpdateCondition) condition,object);
        return this;
    }

    public ConditionBuilder join(String direction,String table){
        conditionHandler.setJoin((QueryCondition) condition,direction,table);
        return this;
    }

    public ConditionBuilder on(String ...ons){
        conditionHandler.setOn((QueryCondition) condition,ons);
        return this;
    }

    public Condition build(){
        return condition;
    }
}
