package com.oj.onlinejudge.service.user.self;

import com.alibaba.fastjson.JSONObject;

public interface SelfSolutionsService {

    public JSONObject selfSolutionListGetter(String userKey, String column, int order, int page);

}
