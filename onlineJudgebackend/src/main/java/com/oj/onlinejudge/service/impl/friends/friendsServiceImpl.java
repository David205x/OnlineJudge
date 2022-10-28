package com.oj.onlinejudge.service.impl.friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.mapper.FriendsMapper;
import com.oj.onlinejudge.pojo.Friends;
import com.oj.onlinejudge.service.friends.friendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class friendsServiceImpl implements friendsService {

    @Autowired
    private FriendsMapper friendsMapper;
    @Override
    public String updateFriends(String userKey) {
        JSONArray JsonArray;
        try{
            JsonArray = JSON.parseArray(friendsMapper.selectById(userKey).getFriends());

            System.out.println(JsonArray.get(0));
        }catch (NullPointerException e){

            e.printStackTrace();
            return "error";
        }
        return JsonArray.toString();
    }

    @Override
    public Map<String, String> addFriend(Friends friend, String userKey) {
        JSONObject json;
        JSONObject f = new JSONObject();
        f.put("userKey", friend.getUserkey());
        f.put("userName", friend.getUsername());
        Map<String, String> resp = new HashMap<>();
        try{
            json = JSONObject.parseObject(friendsMapper.selectById(userKey).getFriends());
            System.out.println(json);
//            for(int i = 1; i <= min(9, Integer.parseInt((String)json.get("num"))); i ++){
//                json.put("friend" + i, json.);
//            }
//            json.put("friend0", f);
            resp.put("error_message", "success");
            resp.put("friends", JSONObject.toJSONString(json));
        }catch (NullPointerException e){
            e.printStackTrace();
            resp.put("error_message", "null");
            return resp;
        }

        return resp;
    }
}
