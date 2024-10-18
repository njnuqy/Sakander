package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.DefaultDbPipe;
import com.sakander.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest(classes = Sakander.class)
public class DefaultDbPipeTest {
    DefaultDbPipe defaultDbPipe = new DefaultDbPipe();

    @Test
    void selectOneTest(){
        Student student = defaultDbPipe.from("student").where("student_id = ?", 15).selectOne(Student.class);
        System.out.println(student);
        student = defaultDbPipe.from("student").where("student_id = ?", 15).selectOne(Student.class);
        System.out.println(student);
    }

    @Test
    void selectListTest(){
        List<Student> students = defaultDbPipe.from("student").selectList(Student.class);
        students.forEach(System.out::println);
    }

    @Test
    void insertTest(){
        Student student = new Student(1234,"1234",1234);
        int insert = defaultDbPipe.from("student").insert(student);
        System.out.println(insert);
    }

    @Test
    void updateTest(){
        Student student = new Student(1234,"4321",4321);
        int update = defaultDbPipe.from("student").update(student);
        System.out.println(update);
    }
}
