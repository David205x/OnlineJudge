package com.oj.onlinejudge.controller.index;


import com.oj.onlinejudge.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class IndexController {
    @Autowired
    private InfoService infoService;

    @PostMapping("/index/info/")
    public Map<String, String> getInfo(){
        return infoService.getInfo();
    }
}
