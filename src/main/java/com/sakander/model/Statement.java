package com.sakander.model;

import com.sakander.clause.Set;
import com.sakander.clause.Table;
import com.sakander.clause.Where;
import lombok.Data;

@Data
public class Statement {
    private Where where;
    private Set set;
    private String tableName;
    private Table table;
    public Statement(){
        this.where = new Where();
        this.set = new Set();
        this.table = new Table();
    }
}
