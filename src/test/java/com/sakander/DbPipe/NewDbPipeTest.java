package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.NewDbPipe;
import com.sakander.model.Student;
import com.sakander.statement.Statement;
import com.sakander.statement.StatementBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Sakander.class)
public class NewDbPipeTest {
    NewDbPipe dbPipe = new NewDbPipe(Student.class);

    @Test
    void test(){
        Statement statement = new StatementBuilder().where("student_id = ?",15).build();
        Student student = dbPipe.selectOne(statement);
        System.out.println(student);
    }
}
