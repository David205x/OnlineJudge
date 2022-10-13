package com.chattingroom.chattingroom;

import com.chattingroom.chattingroom.service.impl.ChattingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChattingroomApplication {

    public static void main(String[] args) {
        ChattingServiceImpl.matchingPool.start();
        SpringApplication.run(ChattingroomApplication.class, args);
    }

}
