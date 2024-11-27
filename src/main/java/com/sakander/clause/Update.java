package com.sakander.clause;

import lombok.Data;

@Data
public class Update {
    private Object object;

    public Update(Object object){
        this.object = object;
    }
}
