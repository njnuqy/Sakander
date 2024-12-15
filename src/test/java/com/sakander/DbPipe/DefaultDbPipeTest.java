package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.clause.Alias;
import com.sakander.constants.Direction;
import com.sakander.model.DefaultDbPipe;
import com.sakander.model.Student;
import com.sakander.condition.*;
import com.sakander.model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Sakander.class)
public class DefaultDbPipeTest {
    DefaultDbPipe defaultDbPipe = new DefaultDbPipe(Student.class);

    @BeforeEach
    void beforeEach(){
        DefaultDbPipe defaultDbPipe = new DefaultDbPipe(Teacher.class);
    }
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

    /*
    * author:sakura
    * time:2024/12/14
    * function:select map list test
    * */
    @Test
    void selectMapListTest(){
        Condition condition = new ConditionBuilder().query().join(Direction.NO_DIRECTION,"teacher").on("student_id = student_id").build();
        Alias alias = new Alias("a.name","studentName","b.name","teacherName");
        List<Map<String,Object>> list = defaultDbPipe.selectMap(condition,alias);
        System.out.println(list.size());
        Map<String, Object> map = list.get(0);
        for (String s : map.keySet()) {
            System.out.print(s);
            System.out.println(map.get(s));
        }
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
