package com.oj.onlinejudge.service.problems;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

public interface ProblemListService {
    public JSONObject getProblemListOverview(Integer page);
    public JSONObject getProblemListByKey(String key, Integer page);
    public JSONObject getProblemListByName(String name, Integer page);
    public JSONObject getProblemListByTags(ArrayList<String> tags, boolean isUnion, Integer page);
}
