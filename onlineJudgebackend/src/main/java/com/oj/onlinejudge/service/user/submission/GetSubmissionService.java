package com.oj.onlinejudge.service.user.submission;


import java.util.Map;


public interface GetSubmissionService {
    Map<String, String> GetSubmission(String userKey, String code, String timestamp, String language);
}
