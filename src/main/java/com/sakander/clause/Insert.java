package com.sakander.clause;

import lombok.Data;

@Data
public class Insert {
    private Object object;

    public Insert(Object object){
        this.object = object;
    }
}
