package com.oj.onlinejudge;

import com.oj.onlinejudge.service.checker.CppCheckerCore;
import com.oj.onlinejudge.service.checker.FileHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java. io.*;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class OnlinejudgeApplicationTests {
    @Test
    public void testFunction() throws IOException, InterruptedException {
        // filePath should equal to getenv\\files

        FileHelper fileHelper = new FileHelper("E:\\vue\\OnlineJudge\\files\\main.cpp");
        fileHelper.readAll();
        String rawCode = fileHelper.getAll();

        Date date = new Date();
        String timestamp = Long.toString(date.getTime());
        String submissionUUID = "20080000" + "_" + timestamp;

        StringBuilder fileNameBuilder = new StringBuilder("E:\\vue\\OnlineJudge\\files\\");
        fileNameBuilder.append(submissionUUID).append("_").append("main.cpp");
        String fileName = fileNameBuilder.toString();

        FileHelper cppGen = new FileHelper(fileName);
        cppGen.writeAll(rawCode);

        CppCheckerCore c = new CppCheckerCore(submissionUUID);
        try {
            System.out.println(c.checkSubmission());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
