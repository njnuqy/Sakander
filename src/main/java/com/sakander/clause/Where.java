package com.sakander.clause;

import lombok.Data;

@Data
public class Where {
    private String query;
    private Object[] params;
}
