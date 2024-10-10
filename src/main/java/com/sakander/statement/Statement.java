package com.sakander.statement;

import com.sakander.clause.*;
import lombok.Data;

@Data
public class Statement {
    private Where where;
    private Set set;
    private Table table;
    private RowRestriction rowRestriction;
    private GroupBy groupBy;
    private Having having;
    private Count count;
    private Sum sum;
    private Max max;
    private Min min;
    private Average average;
    private Join join;
    private On on;
    private String SQL;
    private Integer timeout;
    private Integer fetchSize;
    public Statement(){
        this.where = new Where();
        this.set = new Set();
        this.table = new Table();
        this.rowRestriction = new RowRestriction();
        this.groupBy = new GroupBy();
        this.having = new Having();
        this.count = new Count();
        this.sum = new Sum();
        this.max = new Max();
        this.min = new Min();
        this.average = new Average();
        this.join = new Join();
        this.on = new On();
    }
}
