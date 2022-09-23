package com.oj.onlinejudge.service.problems;

import java.util.ArrayList;
import java.util.Map;

public interface ProblemFilter {

    Map<String, String> getProblemByKey(Integer problemKey);
    Map<String, String> getProblemByName(String problemName);
    Map<String, String> getProblemByTag(String problemTag, Integer tagCount);

}
