package com.sakander;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Sakander {
    public static void main(String[] args) {
        SpringApplication.run(Sakander.class,args);
    }
}
