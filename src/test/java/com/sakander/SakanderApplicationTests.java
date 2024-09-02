package com.sakander;

import com.sakander.model.Student;
import com.sakander.model.dbPipe;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Sakander.class)
class SakanderApplicationTests {
    private static dbPipe<Student> dbPipe = new dbPipe<Student>();

    @Test
    void addTest(){
        Student student = null;
        for(int i = 0 ; i < 10 ; i ++){
            student = new Student(i,"qy" + i,i * 2);
            dbPipe.add(student);
        }
    }
    @Test
    void updateTest(){
        Student student = null;
        for(int i = 0 ; i < 10 ; i ++){
            student = new Student(i,"new_qy" + i,i * 2);
            dbPipe.update(student);
        }
    }
}
