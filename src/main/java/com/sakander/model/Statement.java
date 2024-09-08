package com.sakander.model;

import com.sakander.clause.*;
import lombok.Data;

@Data
public class Statement {
    private Where where;
    private Set set;
    private String tableName;
    private Table table;
    private Limit limit;
    private GroupBy groupBy;
    private Having having;
    private Count count;
    public Statement(){
        this.where = new Where();
        this.set = new Set();
        this.table = new Table();
        this.limit = new Limit();
        this.groupBy = new GroupBy();
        this.having = new Having();
        this.count = new Count();
    }
}
