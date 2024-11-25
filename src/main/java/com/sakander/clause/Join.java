package com.sakander.clause;

import lombok.Data;

import java.lang.reflect.Type;

@Data
public class Join {
    public static final String NO_DIRECTION = "";
    public static final String NO_TABLE = "";
    public static final Join DEFAULT_JOIN = new Join();
    private String direction;
    private String table;
    public Join(){
        this.direction = NO_DIRECTION;
        this.table = NO_TABLE;
    }
    public Join(String direction, String table){
        this.direction = direction;
        this.table = table;
    }
}
