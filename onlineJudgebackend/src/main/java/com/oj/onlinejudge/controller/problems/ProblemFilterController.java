package com.oj.onlinejudge.controller.problems;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.problems.ProblemFilter;
import com.oj.onlinejudge.service.problems.ProblemListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@RestController
public class ProblemFilterController {

    @Autowired
    private ProblemFilter problemFilter;
    @Autowired
    private ProblemListService problemListService;

    @PostMapping("/problems/test/")
    public Map<String, String> showProblem(@RequestParam String probKey) {
        return problemFilter.getProblemDetails(Integer.parseInt(probKey));
    }

    @PostMapping("/problems/overview/")
    public JSONObject getProblemOverview(@RequestParam Map<String, String> searchProblem){
        System.out.println(searchProblem.get("pageNum"));
        System.out.println(searchProblem.get("userKey"));
        System.out.println(searchProblem.get("searchProblemKey"));
        System.out.println(searchProblem.get("searchProblemName"));
        System.out.println(searchProblem.get("searchProblemTag"));
        System.out.println(searchProblem.get("problemState"));

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
