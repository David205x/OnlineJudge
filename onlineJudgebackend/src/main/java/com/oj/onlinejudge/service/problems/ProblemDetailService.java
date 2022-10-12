package com.oj.onlinejudge.service.problems;

import java.util.Map;

public interface ProblemDetailService {


    Map<String, String> getProblemDetails(Integer problemKey);
    Map<String, String> getProblemByKey(Integer problemKey);
    Map<String, String> getProblemByName(String problemName);
    Map<String, String> getProblemByTag(String problemTag, Integer tagCount);

}
