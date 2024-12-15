package com.sakander.condition;

import com.sakander.clause.Delete;
import com.sakander.clause.Insert;
import com.sakander.clause.Update;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCondition extends Condition{
    private Insert insert;
    private Update update;
    private Delete delete;
}
