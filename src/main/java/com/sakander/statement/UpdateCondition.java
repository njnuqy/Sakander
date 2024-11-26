package com.sakander.statement;

import com.sakander.clause.Insert;
import lombok.Data;

@Data
public class UpdateCondition extends Condition{
    private Insert insert;
    private String SQL;
}
