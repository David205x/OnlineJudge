package com.oj.onlinejudge.service.impl.user.submission;

import com.oj.onlinejudge.service.checker.cpp.CppCheckerCore;
import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
@Service
public class GetSubmissionServiceImpl implements GetSubmissionService {

    //  TODO: Unique file/process name for each submission
    @Override
    public Map<String, String> GetSubmission(String userKey, String code, String language) throws IOException {

        String root = System.getenv("BJUT_OJ_HOME");
        StringBuilder fileNameBuilder = new StringBuilder(root + "\\files\\");

        Date date = new Date();
        String timestamp = Long.toString(date.getTime());
        String submissionUUID = userKey + "_" + timestamp;

        fileNameBuilder.append(submissionUUID).append("_").append("main.cpp");
        String fileName = fileNameBuilder.toString();

        Map<String, String> ret;

        FileHelper fileHelper = new FileHelper(fileName);
        if (!fileHelper.writeAll(code)) {
            return null;
        }

        CppCheckerCore c = new CppCheckerCore(submissionUUID);
        try {
            ret = c.checkSubmission();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return ret;
    }
}