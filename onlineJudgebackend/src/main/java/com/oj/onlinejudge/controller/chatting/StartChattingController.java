package com.oj.onlinejudge.controller.chatting;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.mapper.ChattingMapper;
import com.oj.onlinejudge.mapper.FriendsMapper;
import com.oj.onlinejudge.pojo.Chatting;
import com.oj.onlinejudge.pojo.Friends;
import com.oj.onlinejudge.pojo.Problem;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.chatting.ChattingService;
import com.oj.onlinejudge.service.friends.friendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class StartChattingController {

    @Autowired
    private ChattingService chattingService;

    @Autowired
    private friendsService friendsService;


    @Autowired
    private ChattingMapper chattingMapper;

    private final Map<Integer, Integer> queuedUpdateRequests = new HashMap<>();

    @PostMapping("/chatting/start/")
    public String startChatting(@RequestParam MultiValueMap<String, String> data) {

        int senderKey, receiverKey;
        try {
            senderKey = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id"))); // change these later
            receiverKey = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        } catch (Exception e) {
            e.printStackTrace();
            return "Chat session failed to start";
        }
        return chattingService.startChatting(senderKey, receiverKey, queuedUpdateRequests);
    }

    @PostMapping("/chatting/update/friends/")
    public String getFriends(@RequestParam String userKey){
        return friendsService.getFriends(userKey);
    }

    @PostMapping("/chatting/final/friends/")
    public JSONObject finalFriends(@RequestParam Map<String, String> data){
        JSONObject json = new JSONObject();

        if (friendsService.updateFriends(data.get("FriendsInfo"), data.get("userKey"))) {
            json.put("error_message", "success");
        } else {
            json.put("error_message", "error");
        }
        return json;
    }

    @PostMapping("/chatting/add/friend/")
    public Map<String, String> addFriend(@RequestParam Map<String, String> data){
        System.out.println(data);
        return friendsService.addFriend(data.get("senderKey"), data.get("senderName"), data.get("receiverKey"));
    }

    @PostMapping("/chatting/chattingroom/chathistory/")
    public JSONObject getChattingHistory(@RequestParam Integer senderKey,
                                         @RequestParam Integer receiverKey,
                                         @RequestParam Integer page) throws InterruptedException {

//        Thread waitForUpdate = new Thread() {
//            @Override
//            public void run() {
//                while(true) {
//                    try {
//                        Thread.sleep(500); // chat updates every 500ms for both participants.
//                    } catch (Exception e) {
//                        return;
//                    }
//                    if (queuedUpdateRequests.get(senderKey) != null) {
//                        return;
//                    }
//                }
//            }
//        };
//        waitForUpdate.start();
//        waitForUpdate.wait();
//
//        Logger.basicLogger("Updater", senderKey + ": " + queuedUpdateRequests.get(senderKey) + " updates.");
//        queuedUpdateRequests.remove(senderKey);

        // consider moving this function out of chattingService later.
        return chattingService.getChattingHistory(senderKey, receiverKey, page, true);
    }

}
