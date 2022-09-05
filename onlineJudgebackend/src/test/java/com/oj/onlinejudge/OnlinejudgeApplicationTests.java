package com.oj.onlinejudge;

import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.user.account.RegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class OnlinejudgeApplicationTests {
    @Autowired
    private RegisterService registerService;
    @Test
    void contextLoads() {
        registerService.register("test3", "123456", "123456");
    }

}
