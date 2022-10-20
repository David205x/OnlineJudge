package com.oj.onlinejudge.service.user.self;

import com.alibaba.fastjson.JSONObject;

public interface SelfSubmissionService {

    public JSONObject selfSubmissionListGetter(String userKey, String column, int order, int page);

}
