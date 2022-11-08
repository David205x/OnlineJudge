package com.oj.onlinejudge.service.user;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface RankService {
    JSONObject getRanking(Integer count);
}
