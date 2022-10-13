package com.oj.onlinejudge.service.problems.solution;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface SolutionDetailService {

    public JSONObject getSolutionDetails(String solutionKey);

}
