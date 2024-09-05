package com.sakander.model;

import com.sakander.Sakander;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Sakander.class)
public class DbPipeTest {
    private static final DbPipe<Student> dbPipe = new DbPipe<>();

}
