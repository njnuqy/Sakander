package com.sakander.condition;

import com.sakander.clause.Table;
import com.sakander.clause.Where;
import lombok.Data;

@Data
public abstract class Condition {
    private Where where;
    private Table table;
    private Object[] Parameters;
    private String SQL;
    public Condition(){
        this.where = new Where();
        this.table = new Table();
        this.Parameters = new Object[0];
    }

}
