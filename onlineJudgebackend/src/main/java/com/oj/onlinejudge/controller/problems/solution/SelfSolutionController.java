package com.oj.onlinejudge.controller.problems.solution;

import com.oj.onlinejudge.service.user.self.SelfSolutionsService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SelfSolutionController {
    @Autowired
    private SelfSolutionsService selfSolutionsService;

    @PostMapping("profile/submissions")
    public JSONObject getSelfSubmissions(@RequestParam String userKey,
                                         @RequestParam String column,
                                         @RequestParam Integer order,
                                         @RequestParam Integer page) {

        return selfSolutionsService.selfSolutionListGetter(userKey, column, order, page);
    }
}
