package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.NewDbPipe;
import com.sakander.model.Student;
import com.sakander.condition.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Sakander.class)
public class NewDbPipeTest {

    @Autowired
    NewDbPipe dbPipe = new NewDbPipe(Student.class);

    @Test
    void selectOneTest(){
        Condition condition = new ConditionBuilder().query().where("student_id = ?",66).build();
        Student student = dbPipe.selectOne(condition);
        System.out.println(student);
    }

    @Test
    void selectListTest(){
        Condition condition = new ConditionBuilder().query().build();
        List<Student> list = dbPipe.selectList(condition);
        System.out.println(list);
    }

    @Test
    void insertTest(){
        Student student = new Student(66,"test",66);
        dbPipe.insert(new ConditionBuilder().update().insert(student).build());
    }


}
