package com.oj.onlinejudge.service.problems;

import net.minidev.json.JSONObject;

public interface ProblemListService {
    public JSONObject getList(Integer page);
}
