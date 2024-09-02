package com.sakander.model;

import com.sakander.annotations.Id;
import com.sakander.annotations.Table;
import com.sakander.annotations.Column;
import lombok.Data;

import java.util.Calendar;

@Data
@Table(name = "student")
public class Student {
    @Id(name = "student_id")
    private int studentNo;
    @Column(name = "name")
    private String name;
    @Column(name = "age", type = "int")
    private int age;
    @Column(name = "birthday", type = "Calendar")
    private Calendar birthday;

    public Student(){ super(); }
    public Student(int studentNo, String name, int age){
        this.studentNo = studentNo;
        this.name = name;
        this.age = age;
    }

}

