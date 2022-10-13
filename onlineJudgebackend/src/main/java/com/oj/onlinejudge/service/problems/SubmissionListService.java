package com.oj.onlinejudge.service.problems;

import com.alibaba.fastjson.JSONObject;

import java.util.Set;

public interface SubmissionListService {

    public JSONObject submissionListGetter(String problemKey, int page);
    public JSONObject masterSubmissionListGetter(String problemKey,
                                                 String userName,
                                                 String result,
                                                 String lang,
                                                 int page);
    public Set<Integer> getFullSubmissionList();
    public Set<Integer> getProblemListByColumn(String columnName, String criteria);

}
