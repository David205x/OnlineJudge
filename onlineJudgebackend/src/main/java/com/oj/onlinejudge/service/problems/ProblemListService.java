package com.oj.onlinejudge.service.problems;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public interface ProblemListService {
    public JSONObject masterProblemListGetter(String problemKey,
                                              String userKey,
                                              String problemName,
                                              ArrayList<String> tags,
                                              boolean isUnion,
                                              Integer state,
                                              Integer page);
    public JSONObject problemListGetter(Integer page);
    public Set<Integer> getProblemListByKey(String key, Integer page);
    public Set<Integer> getProblemListByName(String name, Integer page);
    public Set<Integer> getProblemListByTags(ArrayList<String> tags, Boolean isUnion, Integer page);
    public Set<Integer> getProblemListByState(String userKey, Integer state, Integer page);
}
