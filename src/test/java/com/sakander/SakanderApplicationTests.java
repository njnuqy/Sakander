package com.sakander;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.sakander.clause.Count;
import com.sakander.clause.Sum;
import com.sakander.constants.Direction;
import com.sakander.model.Student;
import com.sakander.model.DbPipe;
import com.sakander.utils.JdbcUtils;
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
        student = new Student(101,"qy",123);
        dbPipe.add(student);
    }
    @Test
    void addInBatchTest(){
        List<Student> students = new ArrayList<>();
        students.add(new Student(12,"123",200));
        students.add(new Student(13,"321",123));
        dbPipe.addInBatch(students);
    }
    @Test
    void updateTest(){
        Student student = new Student(11,"qyy",123);
        dbPipe.update(student);
    }
    @Test
    void updateByParamsTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("name","1234567");
        map.put("age",144);
        dbPipe.set(map).where("student_id = ?",12).updateByParams(new Student());
        Student student =  (Student) dbPipe.where("student_id = ?",12).select();
        System.out.println(student);
    }
    @Test
    void deleteTest(){
        dbPipe.where("student_id = ? and name = ?",12,"qy").delete();
    }
    @Test
    void selectTest(){
        Student student = null;
        student =  (Student) dbPipe.where("student_id = ?",15).select();
        System.out.println(student);
    }
    @Test
    void selectInBatchTest(){
        List<Student> students = dbPipe.where("name = ?","qy").limit(2,1).easySelectInBatch();
        students.forEach(System.out::println);
        students = dbPipe.where("name = ?","qy").limit(1,1).complexSelectInBatch();
        students.forEach(System.out::println);
    }
    @Test
    void selectWithColumnsTest(){
        List<Map<String, Object>> list = dbPipe.where("student_id = ?", 15).selectWithColumns("name", "age");
        Map<String, Object> map = list.get(0);
        map.forEach((k,v) -> {
            System.out.println(k + "===" + v);
        });
    }
    @Test
    void limitTest(){
        List<Object> objects = dbPipe.where("`age` > ?",20).limit(2).easySelectInBatch();
        objects.forEach(System.out::println);
    }
    @Test
    void countTest(){
        List<Map<String, Object>> maps = dbPipe.group("name").count("age").selectWithAggregate("name");
        test(maps);
    }
    @Test
    void sumTest(){
        List<Map<String,Object>> maps = dbPipe.group("name").sum("age").selectWithAggregate("name");
        test(maps);
    }
    @Test
    void maxTest(){
        List<Map<String,Object>> maps = dbPipe.group("name").max("age").selectWithAggregate("name");
        test(maps);
    }
    @Test
    void minTest(){
        List<Map<String,Object>> maps = dbPipe.group("name").min("age").selectWithAggregate("name");
        test(maps);
    }
    @Test
    void averageTest(){
        List<Map<String,Object>> maps = dbPipe.group("name").average("age").selectWithAggregate("name");
        test(maps);
    }
    @Test
    void joinTest(){
        List<Map<String, Object>> maps = dbPipe.join(Direction.DIRECTION_LEFT, "teacher", "name").on("t2.student_id = t1.student_id").selectWithJoin("name as studentName");
        test(maps);
    }
    @Test
    void sqlTest(){
        List<Student> objects = dbPipe.querySQL("select * from student");
        objects.forEach(object -> {
            System.out.println(object);
        });
        dbPipe.updateSQL("update student set `name` = 'qqq' where student_id = 10");
    }
    void test(List<Map<String,Object>> maps){
        Map<String, Object> map0 = maps.get(0);
        System.out.println(map0.keySet());
        map0.entrySet().forEach(System.out::println);
    }
}
