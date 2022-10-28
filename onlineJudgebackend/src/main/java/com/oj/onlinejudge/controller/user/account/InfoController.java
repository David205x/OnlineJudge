package com.oj.onlinejudge.controller.user.account;

import com.oj.onlinejudge.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String, String> getInfo(){
        return infoService.getInfo();
    }

    @GetMapping("/user/account/visit/")
    public Map<String, String> getOthers(@RequestParam String userKey){
        return infoService.getOthers(userKey);
    }
}
