package com.oj.onlinejudge.service.problems;


import com.alibaba.fastjson.JSONObject;

public interface ProblemListService {
    public JSONObject getProblemListOverview(Integer page);
}
