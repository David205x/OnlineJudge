package com.oj.onlinejudge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class onlineJudgeApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));
        SpringApplication.run(onlineJudgeApplication.class, args);
    }

}
