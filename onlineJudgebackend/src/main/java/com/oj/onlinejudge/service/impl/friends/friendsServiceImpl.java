package com.oj.onlinejudge.service.impl.friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.mapper.FriendsMapper;
import com.oj.onlinejudge.pojo.Friends;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.friends.friendsService;
import com.oj.onlinejudge.utils.StrategyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.min;


@Service
public class friendsServiceImpl implements friendsService {

    @Autowired
    private FriendsMapper friendsMapper;

    /**
     * @author Luo`Xing`yue
     * @CreateTime 2022-10-29
     * @param JsonFriendsInfo json : {"userKey" : "xxx", "userName" : "xxx", "Friends" : "[{}, {}, ...]"}
     * @param userKey 发送者的用户编号
     * 用于更新发送者的好友信息
     * @return
     */
    @Override
    public boolean updateFriends(String JsonFriendsInfo, String userKey) {
        try{
            JSONObject json = JSONObject.parseObject(JsonFriendsInfo);
            int f = friendsMapper.updateById(
                    new Friends(
                            Integer.parseInt(userKey),
                            (String)json.get("userName"),
                            (String)json.get("Friends")
                    )
            );
            if(f == 0){
                friendsMapper.insert(
                        new Friends(
                                Integer.parseInt(userKey),
                                (String)json.get("userName"),
                                (String)json.get("Friends")
                        )
                );
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @author Luo`Xing`yue
     * @CreateTime 2022-10-29
     * @param senderKey 接收者好友中需要增加、更新的一个新好友
     * @param senderName
     * @param userKey 接收者编号
     * @return 最新的好友放在最后面
     */
    @Override
    public Map<String, String> addFriend(String senderKey, String senderName, String userKey) {
        JSONObject json = new JSONObject();
        json.put("userKey", senderKey);
        json.put("userName", senderName);
        JSONArray JsonArray;
        Map<String, String> resp = new HashMap<>();
        try{
            Friends friend = friendsMapper.selectById(userKey);
            Logger.basicLogger("Logger Event friends", friend.getFriends());
            JsonArray = JSONArray.parseArray(friend.getFriends());
            int index = JsonArray.size();
            for(int i = 0; i < JsonArray.size(); i++){
                JSONObject jsonObject = (JSONObject) JsonArray.get(i);
                if(senderKey.equals(jsonObject.get("userKey"))){
                    JsonArray.remove(i);
                    index = i;
                }
            }
            for(int i = min(min(index, JsonArray.size()), StrategyUtil.MAX_FRIENDS_NUM); i >= 1; i--) {
                JsonArray.set(i, JsonArray.get(i - 1));
            }
            JsonArray.set(0, json);
            if(JsonArray.size() == StrategyUtil.MAX_FRIENDS_NUM) {
                JsonArray.remove(StrategyUtil.MAX_FRIENDS_NUM);
            }
            friendsMapper.updateById(new Friends(Integer.parseInt(userKey), friend.getUsername(), JsonArray.toString()));
            resp.put("error_message", "success");
        }catch (NullPointerException e){
            e.printStackTrace();
            resp.put("error_message", "error");
        }
        return resp;
    }

    @Override
    public String getFriends(String userKey) {

        JSONArray JsonArray;
        Friends friend = friendsMapper.selectById(userKey);

        if(friend == null){
            JsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("error_message", "error");
            JsonArray.add(0, json);
            return JsonArray.toString();
        }
        String str = friend.getFriends();
        if(str == null || str.length() == 0 || "[]".equals(str)){
            JsonArray = new JSONArray();
            JSONObject json = new JSONObject();
            json.put("error_message", "error");
            JsonArray.add(0, json);
            return JsonArray.toString();
        }
        JsonArray = JSONObject.parseArray(str);
        return JsonArray.toString();
    }
}
