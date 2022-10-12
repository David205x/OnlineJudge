package com.oj.onlinejudge;

import com.oj.onlinejudge.service.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class onlineJudgeApplication {

    public static void main(String[] args) {

        Logger.switchStream(System.out);

        SpringApplication.run(onlineJudgeApplication.class, args);
    }

}
