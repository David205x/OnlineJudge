package com.oj.onlinejudge.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.oj.onlinejudge.service.user.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RankController {
    @Autowired
    private RankService rankService;

    @PostMapping("/user/ranking/")
    public JSONObject getRanking(@RequestParam Map<String, String> map){
        return rankService.getRanking(Integer.valueOf(map.get("count")));
    }
}
