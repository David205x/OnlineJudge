package com.oj.onlinejudge.controller.user.submission;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.user.self.SelfSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelfSubmissionController {
    @Autowired
    private SelfSubmissionService selfSubmissionService;

    @PostMapping("profile/solutions")
    public JSONObject getSelfSubmissions(@RequestParam String userKey,
                                         @RequestParam String column,
                                         @RequestParam Integer order,
                                         @RequestParam Integer page) {

        return selfSubmissionService.selfSubmissionListGetter(userKey, column, order, page);
    }

}
