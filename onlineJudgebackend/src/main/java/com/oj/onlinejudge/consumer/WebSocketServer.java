package com.oj.onlinejudge.consumer;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.consumer.util.JwtAuthentication;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    final public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();

    private User user;

    private Session session = null;

    public static UserMapper userMapper;

    public static RestTemplate restTemplate;



    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }


    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){ WebSocketServer.restTemplate = restTemplate; }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session;

        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);
            Logger.basicLogger(String.valueOf(userId));
            Logger.basicLogger("connected");
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
        // 关闭链接
        Logger.basicLogger("disconnected");
        if (this.user != null) {
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }
    public static void startChatting(Integer aId, Integer bId){
        users.get(aId).sendMessage("b");
        users.get(bId).sendMessage("a");
    }


    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        Logger.basicLogger("receive message");
        JSONObject data = JSONObject.parseObject(message);
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

