package com.oj.onlinejudge.controller.problems;

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

// TODO: THIS IS A TEST FUNCTION, MAY GET REMOVED LATER

@RestController
public class ShowProblemController {

    @Autowired
    private ShowProblemService showProblemService;

    @PostMapping("/problems/test/")
    public Map<String, String> showProblem() {
        //return showProblemService.getProblem("1", "test");
        Map<String, String> retMap = new HashMap<>();
        File file = new File("C:/Users/luo'xing'yue/Desktop/out.txt");
        char[] c = new char[100];
        int count = 0;
        String ans = "";
        try {
            FileReader fr = new FileReader(file);
            while ((count = fr.read(c)) != -1) {
                //将字符数组转换成字符串
                String s = new String(c, 0, count);
                ans += s;
            }
            fr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        retMap.put("problemKey", "1");
        retMap.put("problemBody", ans);
        return retMap;
    }
}
