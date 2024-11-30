package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.DefaultDbPipe;
import com.sakander.model.Student;
import com.sakander.condition.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = Sakander.class)
public class DefaultDbPipeTest {
    DefaultDbPipe defaultDbPipe = new DefaultDbPipe(Student.class);
    // select test
    @Test
    void selectOneTest(){
        Condition condition = new ConditionBuilder().query().where("student_id = ?",66).build();
        Student student = defaultDbPipe.selectOne(condition);
        System.out.println(student);
    }
    // select list test
    @Test
    void selectListTest(){
        Condition condition = new ConditionBuilder().query().build();
        List<Student> list = defaultDbPipe.selectList(condition);
        System.out.println(list);
    }
    // insert test
    @Test
    void insertTest(){
        Student student = new Student(66,"test",66);
        defaultDbPipe.insert(new ConditionBuilder().update().insert(student).build());
    }
    // update test
    @Test
    void updateTest(){
        Student student = new Student(66,"test23",77);
        defaultDbPipe.update(new ConditionBuilder().update().change(student).build());
    }
    // delete test
    @Test
    void deleteTest(){
        Student student = new Student(66,"test23",77);
        defaultDbPipe.delete(new ConditionBuilder().update().delete(student).build());
    }

}
