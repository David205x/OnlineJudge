package com.chattingroom.chattingroom.service.impl;

import com.chattingroom.chattingroom.service.ChattingService;
import com.chattingroom.chattingroom.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;


@Service
public class ChattingServiceImpl implements ChattingService {

    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addUser(Integer userId, String userName, String avatarUrl) {
        System.out.println("add player success");
        matchingPool.addParticipant(userId, userName, avatarUrl);
        return "add player success";
    }

    @Override
    public String removeUser(Integer userId) {
        System.out.println("remove player success");
        matchingPool.removeParticipant(userId);
        return "remove player success";
    }

    @Override
    public String directMessage(Integer senderId, Integer receiverId) {
        return null;
    }


}
