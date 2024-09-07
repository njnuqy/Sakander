package com.sakander;

import com.sakander.model.Student;
import com.sakander.model.DbPipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.util.*;

@SpringBootTest(classes = Sakander.class)
class SakanderApplicationTests {
    private static final DbPipe<Student> dbPipe = DbPipe.create(new Student());
    @Test
    void addTest(){
        Student student = null;
        student = new Student(10,"qy",12 * 2);
        dbPipe.add(student);
    }
    @Test
    void updateByParamsTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","33333");
        map.put("age",144);
        dbPipe.set(map).where("student_id = ?",12).updateByParams(new Student());
        Student student =  (Student) dbPipe.where("student_id = ?",12).select();
        System.out.println(student);
    }
    @Test
    void deleteTest(){
        for(int i = 0 ; i < 15 ; i ++){
            dbPipe.where("student_id = ?",i).delete(new Student());
        }
    }
    @Test
    void selectTest(){
        Student student = null;
        student =  (Student) dbPipe.where("student_id = ?",15).select();
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
    @Test
    void addInBatchTest(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(12,"123",200));
        students.add(new Student(13,"321",123));
        dbPipe.addInBatch(students);
    }
}
