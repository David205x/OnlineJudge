package com.oj.onlinejudge.controller.user.submission;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.user.self.UserHeatmapService;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class UserSubmissionController {
    @Autowired
    private GetSubmissionService getSubmissionService;

    @Autowired
    private UserHeatmapService userHeatmapService;

    @PostMapping("/user/submission/getinfo/")
    public Map<String, String> submission(@RequestParam String userKey, @RequestParam String code,
            @RequestParam String language, @RequestParam String debugInfo, @RequestParam String targetProblem)
            throws IOException {
        if (debugInfo.length() == 0) {
            debugInfo = null;
        }
        return getSubmissionService.GetSubmission(userKey, code, language, debugInfo, targetProblem);
    }

    @PostMapping("/user/submission/heatmap/")
    public JSONObject heatmap(@RequestParam String userKey) {
        return userHeatmapService.getHeatmapInfo(userKey);
    }
}
