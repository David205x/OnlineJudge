package com.oj.onlinejudge.consumer;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.consumer.util.JwtAuthentication;
import com.oj.onlinejudge.mapper.ChattingMapper;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.Chatting;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;

    private Session session = null;

    private static ChattingMapper chattingMapper;
    public static UserMapper userMapper;

    public static RestTemplate restTemplate;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setChattingMapper(ChattingMapper chattingMapper) {
        WebSocketServer.chattingMapper = chattingMapper;
    }


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        this.session = session;

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
            Logger.basicLogger("Chatting", "Connection established for user#" + userId + ".");
        } else {
            try {
                this.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose() {
        if (this.user != null) {
            users.remove(this.user.getId());
            Logger.basicLogger("Chatting", "Connection terminated for user#" + this.user.getId() + ".");
            matchpool.remove(this.user);
        }
    }

    public static boolean sendSingleMessage(Integer senderKey, Integer receiverKey, String content) {

        try {
            users.get(senderKey).sendMessage(content);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public static void chatInsertion(Integer senderKey, Integer receiverKey, String content) {
        User sender = userMapper.selectById(senderKey);
        User receiver = userMapper.selectById(receiverKey);

        chattingMapper.insert(new Chatting(
                        null,
                        sender.getId(),
                        sender.getUsername(),
                        receiver.getId(),
                        receiver.getUsername(),
                        content,
                        new Timestamp(System.currentTimeMillis())
                )
        );
    }

    public static void startChatting(Integer senderKey, Integer receiverKey, Map<Integer, Integer> monitor){

        final String contentAtoB = "a sent a message to b at " + Logger.timestampGen();
        final String contentBtoA = "At " + Logger.timestampGen() + " b sent a a message.";

        if (sendSingleMessage(senderKey, receiverKey, contentAtoB)) {
            chatInsertion(senderKey, receiverKey, contentAtoB);
        }
        else {
            Logger.basicLogger("Chatting", "Message fail to send.");
        }

//        if (monitor.containsKey(senderKey)) {
//            monitor.put(senderKey, monitor.get(senderKey) + 1);
//        }

        if (sendSingleMessage(receiverKey, senderKey, contentBtoA)) {
            chatInsertion(receiverKey, senderKey, contentBtoA);
        } else {
            Logger.basicLogger("Chatting", "Message fail to send.");
        }

//        if (monitor.containsKey(receiverKey)) {
//            monitor.put(receiverKey, monitor.get(receiverKey) + 1);
//        }

    }


    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject data = JSONObject.parseObject(message);
        //JSONObject.parseObject((String)data.get("content")).put("time", new Date());
        System.out.println((String)data.get("content"));
        int senderKey, receiverKey;
        senderKey = Integer.parseInt((String) data.get("a_id")); // change these later
        receiverKey = Integer.parseInt((String) data.get("b_id"));
        System.out.println(senderKey + " " + receiverKey);
        if("singleMessage".equals(data.get("event"))){
            JSONObject json = new JSONObject();
            json.put("content", (String)data.get("content"));
            json.put("time", new Timestamp(System.currentTimeMillis()));
            json.put("receiverId", String.valueOf(receiverKey));
            sendSingleMessage(senderKey, receiverKey, JSONObject.toJSONString(json));
            sendSingleMessage(receiverKey, senderKey, JSONObject.toJSONString(json));

        }
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}

