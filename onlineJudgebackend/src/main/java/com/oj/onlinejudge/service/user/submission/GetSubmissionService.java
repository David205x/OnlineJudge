package com.oj.onlinejudge.service.user.submission;


import java.io.IOException;
import java.util.Map;


public interface GetSubmissionService {
    Map<String, String> GetSubmission(String userKey, String code, String language, String debugInfo, String targetProblem) throws IOException;
}
