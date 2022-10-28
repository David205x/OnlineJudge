package com.oj.onlinejudge.service.user.account;

import java.util.Map;

public interface InfoService {
    Map<String, String> getInfo();
    Map<String, String> getOthers(String userKey);
}
