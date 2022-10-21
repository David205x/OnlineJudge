package com.oj.onlinejudge.service.user.submission;


import com.oj.onlinejudge.service.impl.user.submission.GetSubmissionServiceImpl;

import java.io.IOException;
import java.util.Map;


public interface GetSubmissionService {

    GetSubmissionServiceImpl getSubmission(String userKey,
                                           String code,
                                           String language,
                                           String debugInfo,
                                           String targetProblem,
                                           String SID);

    String getSUUID();

    Map<String, String> callChecker() throws IOException;
}
