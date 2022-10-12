package com.oj.onlinejudge.service.problems.solution;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

public interface SolutionListService {

    public boolean addSolution(String problemKey, String userKey, String content);
    public JSONObject solutionListGetter(String problemKey, Integer page);
    public Set<Integer> getFullSolutionList();

}
