package com.oj.onlinejudge;

import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.checker.utils.queues.QueueManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class onlineJudgeApplication {

    public static void main(String[] args) {

        Logger.switchStream(System.out);
        QueueManager.initBlockingQueue();
        QueueManager.initResultPool();

        SpringApplication.run(onlineJudgeApplication.class, args);
    }

}
