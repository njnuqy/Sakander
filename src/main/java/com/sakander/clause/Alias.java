package com.sakander.clause;

import lombok.Data;

import java.util.HashMap;

@Data
public class Alias {
    private HashMap<String,String> alias;
    public Alias(String ...alias){
        this.alias = new HashMap<>();
        for(int i = 0 ; i < alias.length ; i += 2){
            this.alias.put(alias[i],alias[i+1]);
        }
    }
}
