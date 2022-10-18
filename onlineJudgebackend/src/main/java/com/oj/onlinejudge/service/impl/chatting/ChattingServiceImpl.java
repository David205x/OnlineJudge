package com.oj.onlinejudge.service.impl.chatting;

import com.oj.onlinejudge.consumer.WebSocketServer;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.chatting.ChattingService;
import org.springframework.stereotype.Service;

@Service
public class ChattingServiceImpl implements ChattingService {

    @Override
    public String startChatting(Integer aId, Integer bId) {
        Logger.basicLogger("start game : " + aId + " " + bId);
        WebSocketServer.startChatting(aId, bId);
        return "start chatting!";
    }
}
