package com.oj.onlinejudge.controller.problems.solution;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.problems.solution.SolutionDetailService;
import com.oj.onlinejudge.service.problems.solution.SolutionListService;
import jnr.ffi.annotations.In;
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

    @PostMapping("problem/details/randomsolution/")
    private JSONObject randomSolution() {
        return solutionListService.randomSolutionGetter();
    }

    @PostMapping("problem/details/ones/solutionlist/")
    private JSONObject solutionListForOnes(@RequestParam String userKey, @RequestParam Integer page) {
        return solutionListService.solutionListGetterForOnes(userKey, page);
    }
    @PostMapping("problem/details/{problemId}/solutionlist")
    private JSONObject solutionList(@PathVariable String problemId, @RequestParam Integer page) {
        return solutionListService.solutionListGetter(problemId, page);
    }

    @PostMapping("problem/details/{problemId}/{solutionKey}/updatesolution")
    private boolean updateSolution(@PathVariable String problemId, @PathVariable Integer solutionKey, @RequestParam String language, @RequestParam String userKey, @RequestParam String content) {
        return solutionListService.updateSolution(solutionKey, language, problemId, userKey, content);
    }
    @PostMapping("problem/details/{problemId}/addsolution")
    private boolean addSolution(@PathVariable String problemId, @RequestParam String language, @RequestParam String userKey, @RequestParam String title, @RequestParam String content) {
        return solutionListService.addSolution(language, problemId, userKey, title, content);
    }

    @PostMapping("problem/details/deletesolution")
    private boolean addSolution( @RequestParam String solutionKey) {
        return solutionListService.deleteSolution(solutionKey);
    }
    @PostMapping("problem/details/{problemId}/{solutionId}")
    private JSONObject getSolution(@PathVariable String problemId, @PathVariable String solutionId) {
        return solutionDetailService.getSolutionDetails(solutionId);
    }

}
