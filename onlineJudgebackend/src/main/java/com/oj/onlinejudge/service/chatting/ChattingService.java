package com.oj.onlinejudge.service.chatting;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @implNote updateChattingState 在前端会话结束时更新
 */
public interface ChattingService {
    String updateChattingState(String ChattingInfo);
    String startChatting(Integer senderKey, Integer receiverKey, Map<Integer, Integer> monitor);
    JSONObject getChattingHistory(int senderKey, int receiverKey, int page, boolean mutual);
}
