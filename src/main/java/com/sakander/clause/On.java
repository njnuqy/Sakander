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
    public On(String ...ons){
        for(int i = 0 ; i < ons.length ; i++){
            ons[i] = ons[i].replaceAll("\\b(\\w+)\\b\\s*=\\s*\\b\\1\\b", "a.$1 = b.$1");
        }
        this.ons = ons;
    }
}
