package com.oj.onlinejudge.service.friends;


import java.util.Map;

/**
 * @updateFriends 前端对话结束后更新发送者数据表
 * @addFriends 前端对话结束后更新接收者数据表
 * @getFrineds 前端对话开始时获取发送者信息列表
 */
public interface friendsService {
    boolean updateFriends(String JsonFriendsInfo, String userKey);
    Map<String, String> addFriend(String senderKey, String senderName ,String userKey);
    String getFriends(String userKey);
}
