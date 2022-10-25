package com.chattingroom.chattingroom.controller;

import com.chattingroom.chattingroom.service.ChattingService;
import com.chattingroom.chattingroom.service.impl.utils.MatchingPool;
import com.chattingroom.chattingroom.service.impl.utils.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
public class ChattingController {
    @Autowired
    private ChattingService chattingService;

    private static RestTemplate restTemplate;

    private final static String startMatchingUrl = "http://127.0.0.1:3000/chatting/start/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        ChattingController.restTemplate = restTemplate;
    }
    @PostMapping("/user/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data){

        System.out.println("调用算法addPlayer");

        sendResult(
                new Participant(Integer.parseInt(Objects.requireNonNull(data.getFirst("senderId"))), data.getFirst("senderName"), data.getFirst("senderUrl")),
                new Participant(Integer.parseInt(Objects.requireNonNull(data.getFirst("receiverId"))), data.getFirst("receiverId"), data.getFirst("receiverId")));
        return null;
    }
    @PostMapping("/user/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String, String> data){
        System.out.println("调用算法removePlayer");
        return null;
    }

    private void sendResult(Participant a, Participant b){
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("test", "test");
        restTemplate.postForObject(startMatchingUrl, data, String.class);
    }
}
