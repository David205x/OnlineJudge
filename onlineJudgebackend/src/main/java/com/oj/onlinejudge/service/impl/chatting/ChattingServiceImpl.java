package com.oj.onlinejudge.service.impl.chatting;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oj.onlinejudge.consumer.WebSocketServer;
import com.oj.onlinejudge.mapper.ChattingMapper;
import com.oj.onlinejudge.pojo.Chatting;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.chatting.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ChattingServiceImpl implements ChattingService {

    @Autowired
    private ChattingMapper chattingMapper;

    @Override
    public String updateChattingState(String ChattingInfo) {
        try{
            JSONObject jsonObject = JSONObject.parseObject(ChattingInfo);
            int f = chattingMapper.updateById(
                    new Chatting(
                            jsonObject.getInteger("chatkey"),
                            jsonObject.getInteger("senderkey"),
                            jsonObject.getString("sendername"),
                            jsonObject.getInteger("receiverkey"),
                            jsonObject.getString("receivername"),
                            jsonObject.getString("content"),
                            jsonObject.getTimestamp("time"),
                            jsonObject.getString("state"))
            );

        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }

        return "success";
    }

    @Override
    public String startChatting(Integer senderKey, Integer receiverKey, Map<Integer, Integer> monitor) {
        Logger.basicLogger("Chatting", "Starting chat between user#" + senderKey + " and user#" + receiverKey + ".");
        WebSocketServer.startChatting(senderKey, receiverKey, monitor);
        return "Chat session started";
    }

    @Override
    public JSONObject getChattingHistory(int senderKey, int receiverKey, int page, boolean mutual) {

        int chatEntriesPerPage = 10;

        IPage<Chatting> chattingIPage = new Page<>(page, chatEntriesPerPage);
        QueryWrapper<Chatting> chattingWrapper = new QueryWrapper<>();

        JSONObject ret = new JSONObject();

        if (mutual) {
            ArrayList<Integer> users = new ArrayList<>();
            users.add(senderKey);
            users.add(receiverKey);
            chattingWrapper
                    .in("senderKey", users)
                    .in("receiverKey", users)
                    .orderByDesc("time");
        } else {
            chattingWrapper
                    .eq("senderKey", senderKey)
                    .eq("receiverKey", receiverKey)
                    .or()
                    .eq("senderKey", receiverKey)
                    .eq("receiverKey", senderKey)
                    .orderByDesc("time");
        }

        List<Chatting> chattings = chattingMapper.selectPage(chattingIPage, chattingWrapper).getRecords();
        Collections.reverse(chattings);

        if (chattings.isEmpty()) {
            ret.put("chattingCount", -1);
            ret.put("totalPages", 0);
            ret.put("perPage", chatEntriesPerPage);
            ret.put("unreadNum", 0);
            ret.put("chattingList", null);
        }
        int cnt = 0;
        for(Chatting c : chattings){
            if(c.getState().equals("unread")){
                cnt++;
            }
        }
        ret.put("chattingsCount", chattings.size());
        ret.put("totalPages", chattingMapper.selectCount(chattingWrapper));
        ret.put("perPage", chatEntriesPerPage);
        ret.put("unreadNum", cnt);
        ret.put("chattingList", chattings);

        return ret;
    }

}
