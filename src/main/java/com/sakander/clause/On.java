package com.sakander.clause;

import lombok.Data;

@Data
public class On {
    public static final String[] NO_ONS = new String[0];
    public static final On DEFAULT_ON = new On();
    private String[] ons;
    public On(){
        this.ons = NO_ONS;
    }
    public On(String[] ons){
        this.ons = ons;
    }
}
