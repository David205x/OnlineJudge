package com.oj.onlinejudge.service.impl.friends;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.mapper.FriendsMapper;
import com.oj.onlinejudge.pojo.Friends;
import com.oj.onlinejudge.service.friends.friendsService;
import com.oj.onlinejudge.utils.StrategyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


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
    public Map<String, String> addFriend(String senderKey, String senderName ,String userKey) {
        JSONObject json = new JSONObject();
        json.put("userKey", senderKey);
        json.put("userName", senderName);
        JSONArray JsonArray;
        Map<String, String> resp = new HashMap<>();
        try{
            Friends friend = friendsMapper.selectById(userKey);
            JsonArray = JSONArray.parseArray(friend.getFriends());
            if(JsonArray.size() == StrategyUtil.MAX_FRIENDS_NUM){
                for(int i = 1; i < StrategyUtil.MAX_FRIENDS_NUM; i++) {
                    JsonArray.add(i - 1, JsonArray.get(i));
                }
                JsonArray.remove(StrategyUtil.MAX_FRIENDS_NUM - 1);
            }
            for(int i = 0; i < JsonArray.size(); i++){
                JSONObject jsonObject = (JSONObject) JsonArray.get(i);
                if(senderKey.equals(jsonObject.get("userKey"))){
                    JsonArray.remove(i);
                }
            }
            JsonArray.add(JsonArray.size(), json);
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
