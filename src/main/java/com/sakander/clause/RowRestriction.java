package com.sakander.clause;

import lombok.Getter;

@Getter
public class RowRestriction {
    public static final int NO_ROW_OFFSET = 0;
    public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
    public static final RowRestriction DEFAULT = new RowRestriction();
    private final int offset;
    private final int limit;
    public RowRestriction(){
        this.offset = NO_ROW_OFFSET;
        this.limit = NO_ROW_LIMIT;
    }
    public RowRestriction(int limit, int offset){
        this.offset = offset;
        this.limit = limit;
    }

    public RowRestriction(int limit){
        this.offset = NO_ROW_OFFSET;
        this.limit = limit;
    }
}
