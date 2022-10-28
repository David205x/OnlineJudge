package com.oj.onlinejudge.service.friends;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.pojo.Friends;

import java.util.Map;

public interface friendsService {
    String updateFriends(String userKey);
    Map<String, String> addFriend(Friends friend, String userKey);
}
