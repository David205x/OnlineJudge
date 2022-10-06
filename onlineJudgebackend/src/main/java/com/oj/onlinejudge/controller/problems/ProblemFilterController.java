package com.oj.onlinejudge.controller.problems;

import com.oj.onlinejudge.service.problems.ProblemFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProblemFilterController {

    @Autowired
    private ProblemFilter problemFilter;

    @PostMapping("/problems/test/")
    public Map<String, String> showProblem(@RequestParam String probKey) {
        return problemFilter.getProblemDetails(Integer.parseInt(probKey));
    }
    @PostMapping("/problems/overview/")
    public Map<String, String> getProblemOverview(){
        return problemFilter.getProblemByKey(1);
    }
}
