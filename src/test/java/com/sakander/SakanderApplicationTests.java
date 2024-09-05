package com.sakander;

import com.sakander.model.Student;
import com.sakander.model.DbPipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(classes = Sakander.class)
class SakanderApplicationTests {
    private static final DbPipe<Student> dbPipe = DbPipe.create(new Student());
    @Test
    void addTest(){
        Student student = null;
        for(int i = 11 ; i < 13 ; i ++){
            student = new Student(i,"qy",i * 2);
            dbPipe.add(student);
        }
    }

    @Test
    void updateByParamsTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","54321");
        map.put("age",144);
        dbPipe.set(map).where("student_id = ?",12).updateByParams(new Student());
    }
    @Test
    void deleteTest(){
        for(int i = 0 ; i < 10 ; i ++){
            dbPipe.where("student_id = ?",i).delete(new Student());
        }
    }
    @Test
    void selectTest(){
        Student student = null;
        student =  (Student) dbPipe.where("student_id = ?",12).select();
        System.out.println(student);
        student = (Student) dbPipe.where("student_id = ? and name = ?",2,"qy2").select();
        System.out.println(student);
    }
    @Test
    void selectInBatchTest(){
        List<Student> students = dbPipe.where("name = ?","qy").selectInBatch(new Student());
        students.forEach(System.out::println);
    }

    @Test
    void updateTest(){
        Student student = new Student(11,"qyy",123);
        dbPipe.update(student);
    }
}
