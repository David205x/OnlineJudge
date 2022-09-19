package com.oj.onlinejudge;

import com.oj.onlinejudge.service.checker.CppCheckerCore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;


@SpringBootTest
class OnlinejudgeApplicationTests {
    @Test
    void contextLoads() throws IOException, InterruptedException {
        CppCheckerCore c = new CppCheckerCore();
        c.checkSubmission();
    }

}
