package com.oj.onlinejudge.controller.problems;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.problems.ProblemDetailService;
import com.oj.onlinejudge.service.problems.ProblemListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProblemFilterController {

    @Autowired
    private ProblemDetailService problemDetailService;
    @Autowired
    private ProblemListService problemListService;

    @PostMapping("/problems/show/")
    public Map<String, String> showProblem(@RequestParam String probKey) {
        return problemDetailService.getProblemDetails(Integer.parseInt(probKey));
    }

    @PostMapping("/problems/overview/")
    public JSONObject getProblemOverview(@RequestParam Map<String, String> searchProblem){
        return problemListService.masterProblemListGetter(
                searchProblem.get("searchProblemKey"),
                searchProblem.get("userKey"),
                searchProblem.get("searchProblemName"),
                searchProblem.get("searchProblemTag"),
                true,
                searchProblem.get("problemState"),
                Integer.parseInt(searchProblem.get("pageNum")));
    }


}
