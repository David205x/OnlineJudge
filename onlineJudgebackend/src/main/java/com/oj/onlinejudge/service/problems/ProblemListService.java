package com.oj.onlinejudge.service.problems;


import com.alibaba.fastjson.JSONObject;

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
    public Set<Integer> getProblemListByKey(String key);
    public Set<Integer> getProblemListByName(String name);
    public Set<Integer> getProblemListByTags(String tags, Boolean isUnion);
    public Set<Integer> getProblemListByState(String userKey, String problemState);
}
