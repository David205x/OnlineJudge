package com.oj.onlinejudge.service.impl.user.submission;

import com.oj.onlinejudge.service.checker.CppCheckerCore;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class GetSubmissionServiceImpl implements GetSubmissionService {

    @Override
    public Map<String, String> GetSubmission(String userKey, String code, String timestamp, String language) {
        System.out.println(code);
        String root = System.getenv("BJUT_OJ_HOME");
        String filename = root + "\\files\\sample\\main.cpp";
        Map<String, String> ret = new HashMap<>();
        File file = new File(filename);
        try {

            if (!file.exists()) {//如果文件不存在则新建文件

                file.createNewFile();

            }else {
                file.delete();
                file.createNewFile();
            }

            FileOutputStream output = new FileOutputStream(file);

            byte[] bytes = code.getBytes();

            output.write(bytes);//将数组的信息写入文件中
            CppCheckerCore c = new CppCheckerCore();
            ret = c.checkSubmission();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

        }

        return ret;
    }
}