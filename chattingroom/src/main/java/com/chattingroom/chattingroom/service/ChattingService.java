package com.chattingroom.chattingroom.service;

public interface ChattingService {
    String addUser(Integer userId, String userName, String avatarUrl);
    String removeUser(Integer userId);
    String directMessage(Integer senderId, Integer receiverId);
}
