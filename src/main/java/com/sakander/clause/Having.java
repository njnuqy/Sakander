package com.sakander.clause;

import lombok.Data;

@Data
public class Having {
    private String query;
    private Object[] params;
}
