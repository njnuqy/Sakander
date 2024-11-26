package com.sakander.statement;

import com.sakander.clause.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCondition extends Condition{
    private final Set set;
    private final Table table;
    private final RowRestriction rowRestriction;
    private final GroupBy groupBy;
    private final Having having;
    private final Count count;
    private final Sum sum;
    private final Max max;
    private final Min min;
    private final Average average;
    private final Join join;
    private final On on;
    private String SQL;

    public QueryCondition(){
        super();
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
