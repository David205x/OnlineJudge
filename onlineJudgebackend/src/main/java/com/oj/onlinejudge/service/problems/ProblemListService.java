package com.oj.onlinejudge.service.problems;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public interface ProblemListService {
    public JSONObject masterProblemListGetter(String problemKey,
                                              String userKey,
                                              String problemName,
                                              String tags,
                                              boolean isUnion,
                                              String state,
                                              Integer page);
    public JSONObject problemListGetter(Integer page);
    public Set<Integer> getFullProblemList();
    public Set<Integer> getProblemListByKey(String key, Integer page);
    public Set<Integer> getProblemListByName(String name, Integer page);
    public Set<Integer> getProblemListByTags(String tags, Boolean isUnion, Integer page);
    public Set<Integer> getProblemListByState(String userKey, String problemState, Integer page);
}
