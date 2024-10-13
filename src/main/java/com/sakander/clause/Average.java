package com.sakander.clause;

import lombok.Data;

@Data
public class Average {
    public static final String[] NO_AVERAGES = new String[0];
    public Average(){
        this.averages = NO_AVERAGES;
    }
    public Average(String[] averages){
        this.averages = averages;
    }
    private String[] averages;
}
