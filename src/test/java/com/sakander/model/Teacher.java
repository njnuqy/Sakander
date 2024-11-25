package com.sakander.model;

import com.sakander.annotations.Column;
import com.sakander.annotations.Id;
import com.sakander.annotations.Table;
import lombok.Data;

@Data
@Table(name = "teacher")
public class Teacher {
    @Id(name = "id")
    @Column(name = "id")
    private int id;
    @Column(name = "student_id")
    private int studentId;
    @Column(name = "name")
    private String name;
}
