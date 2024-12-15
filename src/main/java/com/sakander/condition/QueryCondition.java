package com.sakander.condition;

import com.sakander.clause.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryCondition extends Condition{
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
    private Alias alias;
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
