package com.oj.onlinejudge.service.problems.solution;

import com.alibaba.fastjson.JSONObject;
import jnr.ffi.annotations.In;

import java.util.Set;

public interface SolutionListService {

    public boolean updateSolution(Integer solutionKey, String language, String problemKey, String userKey, String content);
    public boolean addSolution(String language, String problemKey, String userKey, String content);
    public JSONObject solutionListGetterForOnes(String userKey, Integer page);
    public JSONObject solutionListGetter(String problemKey, Integer page);
    public Set<Integer> getFullSolutionList();

    public boolean deleteSolution(String solutionKey);

}
