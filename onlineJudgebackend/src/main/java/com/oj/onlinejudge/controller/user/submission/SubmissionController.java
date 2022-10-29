package com.oj.onlinejudge.controller.user.submission;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.problems.SubmissionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubmissionController {
    @Autowired
    private SubmissionListService submissionListService;

    @PostMapping("problem/details/{problemId}/sublist")
    public JSONObject submissionList(@PathVariable String problemId,
                                     @RequestParam String userName,
                                     @RequestParam String result,
                                     @RequestParam String lang,
                                     @RequestParam Integer page) {

        return submissionListService.masterSubmissionListGetter(problemId, userName, result, lang, page);
    }
    @PostMapping("problem/details/sublist/")
    public JSONObject submissionListForOnes(@RequestParam String userKey,
                                            @RequestParam Integer page) {

        return submissionListService.masterSubmissionListGetterForOnes(userKey, page);
    }
}
