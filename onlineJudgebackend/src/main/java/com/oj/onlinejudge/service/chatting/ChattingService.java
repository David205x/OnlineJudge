package com.oj.onlinejudge.service.chatting;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ChattingService {
    String startChatting(Integer senderKey, Integer receiverKey, Map<Integer, Integer> monitor);
    JSONObject getChattingHistory(int senderKey, int receiverKey, int page, boolean mutual);

}
