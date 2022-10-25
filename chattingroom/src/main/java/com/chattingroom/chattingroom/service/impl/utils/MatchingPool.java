package com.chattingroom.chattingroom.service.impl.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MatchingPool extends Thread{

    private static RestTemplate restTemplate;

    private static List<Participant> participants = new ArrayList<>();

    private ReentrantLock lock = new ReentrantLock();

    private final static String startMatchingUrl = "http://127.0.0.1:3000/chatting/start/";
    public void addParticipant(Integer userId, String userName, String avatarUrl){
        lock.lock();
        try{
            participants.add(new Participant(userId, userName, avatarUrl));
        }finally {
            lock.unlock();
        }
    }

    public void removeParticipant(Integer userId){
        lock.lock();
        try{
            List<Participant> newParticipants = new ArrayList<>();
            for(Participant participant : participants){
                if(!participant.getId().equals(userId)){
                    newParticipants.add(participant);
                }
            }
            participants = newParticipants;
        }finally {
            lock.unlock();
        }
    }


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        MatchingPool.restTemplate = restTemplate;
    }


    private void sendResult(Participant a, Participant b){
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("test", "test");
        restTemplate.postForObject(startMatchingUrl, data, String.class);
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
                lock.lock();
                try{
                }finally {
                    lock.unlock();
                }

            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
