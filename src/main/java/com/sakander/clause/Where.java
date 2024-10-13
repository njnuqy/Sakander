package com.sakander.clause;

import lombok.Data;

@Data
public class Where {
    public static final String NO_QUERY = "";
    public static final Object[] NO_PARAMS = new Object[0];
    public static final Where DEFAULT_WHERE = new Where();
    private String query;
    private Object[] params;

    public Where(){
        this.query = NO_QUERY;
        this.params = NO_PARAMS;
    }

    public Where(String query,Object[] params){
        this.query = query;
        this.params = params;
    }
}
