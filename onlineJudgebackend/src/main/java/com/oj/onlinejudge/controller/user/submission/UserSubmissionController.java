package com.oj.onlinejudge.controller.user.submission;

import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserSubmissionController {
    @Autowired
    private GetSubmissionService getSubmissionService;

    @PostMapping("/user/submission/getinfo/")
    public Map<String, String> submission(@RequestParam String userKey, @RequestParam String code,
                                          @RequestParam String language, @RequestParam String timestamp){

        return getSubmissionService.GetSubmission(userKey, code, timestamp, language);
    }
}
