package com.chattingroom.chattingroom.service;

public interface ChattingService {
    String addUser(Integer userId, Integer rating, Integer botId);
    String removeUser(Integer userId);
}
