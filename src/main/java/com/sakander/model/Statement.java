package com.sakander.model;

import com.sakander.clause.Where;
import lombok.Data;

@Data
public class Statement {
    private Where where;
    private String tableName;
    public Statement(){
        this.where = new Where();
    }
}
