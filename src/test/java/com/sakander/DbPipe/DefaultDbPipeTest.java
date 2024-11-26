package com.sakander.DbPipe;

import com.sakander.Sakander;
import com.sakander.model.DefaultDbPipe;
import com.sakander.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest(classes = Sakander.class)
public class DefaultDbPipeTest {
    DefaultDbPipe defaultDbPipe = new DefaultDbPipe("student");

    @Test
    void selectOneTest(){
        Student student = defaultDbPipe.where("student_id = ?", 15).selectOne(Student.class);
        System.out.println(student);
        List<Object> objects = defaultDbPipe.selectList(Student.class);
        System.out.println(objects.size());
    }

    @Test
    void selectMapListTest(){
        List<Map<String, Object>> maps = defaultDbPipe.selectMapList(Student.class, "student_id", "name");
        maps.forEach(map -> {
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        });
    }

    @Test
    void selectListTest(){
        List<Student> students = defaultDbPipe.selectList(Student.class);
        students.forEach(System.out::println);
    }
    @Test
    void sqlTest(){
        List<Map<String, Object>> maps = defaultDbPipe.querySql("select s.student_id,t.student_id from student s left join teacher t on t.student_id = s.student_id where s.student_id = 15","s.student_id","t.student_id");
        maps.forEach(map -> {
            for(Map.Entry<String,Object> entry : map.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
            }
        });
    }
    @Test
    void insertTest(){
        Student student = new Student(422,"1234",1234);
        int insert = defaultDbPipe.insert(student);
    }

    @Test
    void updateTest(){
        Student student = new Student(1234,"4321",4321);
        int update = defaultDbPipe.update(student);
        System.out.println(update);
    }

    @Test
    void deleteTest(){
        Student student = defaultDbPipe.where("student_id = ?", 1234).selectOne(Student.class);
        System.out.println(student);
        int delete = defaultDbPipe.delete(student);
        System.out.println(delete);
        student = defaultDbPipe.where("student_id = ?", 1234).selectOne(Student.class);
        System.out.println(student);
    }
}
