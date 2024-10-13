package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.DefaultDbPipe;
import com.sakander.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Sakander.class)
public class DefaultDbPipeTest {
    DefaultDbPipe defaultDbPipe = new DefaultDbPipe();

    @Test
    void selectTest(){
        Student student = defaultDbPipe.from("student").where("student_id = ?", 15).selectOne(Student.class);
        System.out.println(student);
    }
}
