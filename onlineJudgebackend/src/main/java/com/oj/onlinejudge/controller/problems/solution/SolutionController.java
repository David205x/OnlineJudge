package com.oj.onlinejudge.controller.problems.solution;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.problems.solution.SolutionDetailService;
import com.oj.onlinejudge.service.problems.solution.SolutionListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SolutionController {

    @Autowired
    private SolutionListService solutionListService;

    @Autowired
    private SolutionDetailService solutionDetailService;

    @PostMapping("problem/details/{userKey}/ones/solutionlist/")
    private JSONObject solutionListForOnes(@PathVariable String userKey, @RequestParam Integer page) {
        return solutionListService.solutionListGetterForOnes(userKey, page);
    }
    @PostMapping("problem/details/{problemId}/solutionlist")
    private JSONObject solutionList(@PathVariable String problemId, @RequestParam Integer page) {
        return solutionListService.solutionListGetter(problemId, page);
    }

    @PostMapping("problem/details/{problemId}/addsolution")
    private boolean addSolution(@PathVariable String problemId, @RequestParam String language, @RequestParam String userKey, @RequestParam String content) {
        return solutionListService.addSolution(language, problemId, userKey, content);
    }

    @PostMapping("problem/details/{problemId}/{solutionId}")
    private JSONObject getSolution(@PathVariable String problemId, @PathVariable String solutionId) {
        return solutionDetailService.getSolutionDetails(solutionId);
    }
}
