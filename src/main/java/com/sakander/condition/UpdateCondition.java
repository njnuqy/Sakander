package com.sakander.condition;

import com.sakander.clause.Insert;
import lombok.Data;
import org.hibernate.sql.Update;

@Data
public class UpdateCondition extends Condition{
    private Insert insert;
    private Update update;
}
