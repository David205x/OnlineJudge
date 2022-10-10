package com.oj.onlinejudge.service.user.account;

import com.alibaba.fastjson.JSONObject;

public interface UserHeatmapService {
    public JSONObject getHeatmapInfo(String userKey);
}
