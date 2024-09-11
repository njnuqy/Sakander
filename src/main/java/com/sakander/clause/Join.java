package com.sakander.clause;

import lombok.Data;

@Data
public class Join {
    private String direction;
    private String table;
    private String[] cols;
}
