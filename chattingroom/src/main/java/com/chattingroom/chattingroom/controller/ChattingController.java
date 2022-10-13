package com.chattingroom.chattingroom.controller;

import com.chattingroom.chattingroom.service.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ChattingController {
    @Autowired
    private ChattingService chattingService;

    @PostMapping("/user/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data){

        System.out.println("调用算法addPlayer");
        return null;
    }
    @PostMapping("/user/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data){
        System.out.println("调用算法removePlayer");
        return null;
    }
}
