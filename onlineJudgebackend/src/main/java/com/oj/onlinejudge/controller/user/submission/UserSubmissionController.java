package com.oj.onlinejudge.controller.user.submission;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.checker.utils.queues.QueueManager;
import com.oj.onlinejudge.service.impl.user.submission.GetSubmissionServiceImpl;
import com.oj.onlinejudge.service.user.self.UserHeatmapService;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class UserSubmissionController {

    @Autowired
    private GetSubmissionService getSubmissionService;

    @Autowired
    private GetSubmissionServiceImpl getSubmissionServiceImpl;

    @Autowired
    private UserHeatmapService userHeatmapService;

    @PostMapping("/user/submission/offersub/")
    public Map<String, String> processSubmission(@RequestParam String userKey,
                                                 @RequestParam String code,
                                                 @RequestParam String language,
                                                 @RequestParam String debugInfo,
                                                 @RequestParam String targetProblem,
                                                 @RequestParam String SUUID)
            throws IOException, InterruptedException {
        if (debugInfo.length() == 0) {
            debugInfo = null;
        }
        // getSubmissionService.getSubmission(userKey, code, language, debugInfo, targetProblem, SUUID);
        QueueManager.requestCheckerTask(getSubmissionServiceImpl.getSubmission(userKey, code, language, debugInfo, targetProblem, SUUID));
        return new HashMap<>();
    }

    @PostMapping("/user/submission/pollret/")
    public Map<String, String> waitForRet(@RequestParam String SUUID)
            throws IOException, InterruptedException {

        final double timeout = 5000.0;

        final Map<String, Map<String, String>> ret = new HashMap<>();
        Logger.basicLogger("PollResult", "Submission " + SUUID + " called.");

        final long clockStart = System.currentTimeMillis();
        Thread resultPoolListener = new Thread(() -> {
            try {
                while(true) {

                    Map<String, String> tmp = QueueManager.pollCheckerTask(SUUID);
                    if (tmp == null) {
                        Thread.sleep(20);
                        continue;
                    }
                    if (!tmp.isEmpty()) {
                        ret.put(SUUID, tmp);
                        return;
                    }
                    if (System.currentTimeMillis() - clockStart > timeout) {
                        Logger.basicLogger("QueueManager", "Submission expired.");
                        return;
                    }
                    Thread.sleep(20);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        resultPoolListener.start();

        while (resultPoolListener.isAlive()) {
            Thread.sleep(20);
        }

        // TODO: Fix duplicated SUUID attribute.
        Logger.basicLogger("QueueManager", "Submission" + SUUID + " handled.");

        if (resultPoolListener.isAlive()) {
            resultPoolListener.interrupt();
        }
        return ret.get(SUUID);
    }

    @PostMapping("/user/submission/heatmap/")
    public JSONObject heatmap(@RequestParam String userKey) {
        return userHeatmapService.getHeatmapInfo(userKey);
    }
}
