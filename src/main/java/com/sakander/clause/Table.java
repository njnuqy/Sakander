package com.sakander.clause;

import lombok.Data;

@Data
public class Table {
    public static final String NO_TABLE = "";
    private String tableName;

    public Table(){
        this.tableName = NO_TABLE;
    }

    public Table(String tableName){
        this.tableName = tableName;
    }
}
