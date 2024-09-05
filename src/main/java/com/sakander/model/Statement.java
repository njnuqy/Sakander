package com.sakander.model;

import com.sakander.clause.Set;
import com.sakander.clause.Where;
import lombok.Data;

@Data
public class Statement {
    private Where where;
    private Set set;
    private String tableName;
    public Statement(){
        this.where = new Where();
        this.set = new Set();
    }
}
