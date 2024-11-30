package com.sakander.clause;

import lombok.Data;

@Data
public class Delete {
    private Object object;

    public Delete(Object object){
        this.object = object;
    }
}
