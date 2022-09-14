package com.oj.onlinejudge.controller.problems;

import com.oj.onlinejudge.service.problems.ShowProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER

@RestController
public class ShowProblemController {

    @Autowired
    private ShowProblemService showProblemService;

    @PostMapping("/problems/test/")
    public Map<String, String> showProblem() {
        return showProblemService.getProblem("1", "test");
    }
}
