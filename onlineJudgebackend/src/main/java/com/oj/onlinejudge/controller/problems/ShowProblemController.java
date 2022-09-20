package com.oj.onlinejudge.controller.problems;

import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.problems.ShowProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ShowProblemController {

    @Autowired
    private ShowProblemService showProblemService;

    @PostMapping("/problems/test/")
    public Map<String, String> showProblem(String probKey) {

        return showProblemService.getProblem(Integer.toString(1));
    }
}
