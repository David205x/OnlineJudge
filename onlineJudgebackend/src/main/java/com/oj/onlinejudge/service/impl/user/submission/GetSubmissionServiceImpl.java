package com.oj.onlinejudge.service.impl.user.submission;

import com.oj.onlinejudge.service.checker.cpp.CppCheckerCore;
import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.checker.java.JavaCheckerCore;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
@Service
public class GetSubmissionServiceImpl implements GetSubmissionService {

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    @Override
    public Map<String, String> GetSubmission(String userKey, String code, String language) throws IOException {

        String root = System.getenv("BJUT_OJ_HOME");
        StringBuilder fileNameBuilder = new StringBuilder(root + "\\files\\");

        SubmissionUUIDGen gen = new SubmissionUUIDGen(userKey);
        final String submissionUUID = gen.uuidGen();
        System.out.println(tempLogger("SID: " + submissionUUID + " | Language: " + language));

        // TODO: Wire it to frontend.
        String debugInfo = null; // to disable debugger, put null here.

        switch (language) {
            case "c_cpp": fileNameBuilder.append(submissionUUID).append("_main.cpp"); break;
            case "java": fileNameBuilder.append("Main_").append(submissionUUID).append(".java"); break;
            case "python": fileNameBuilder.append("Main_").append(submissionUUID).append(".py"); break;
            default: {
                break;
            }
        }

        String fileName = fileNameBuilder.toString();

        Map<String, String> ret = null;

        FileHelper fileHelper = new FileHelper(fileName);
        if (!fileHelper.writeAll(code)) {
            return null;
        }
        if("c_cpp".equals(language)){
            CppCheckerCore c = new CppCheckerCore(submissionUUID, debugInfo);
            try {
                ret = c.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("java".equals(language)){
            JavaCheckerCore j = new JavaCheckerCore(submissionUUID);
            try {
                ret = j.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("python".equals(language)){
            return null;
        }

        return ret;
    }

    public String tempLogger(String info) {
        return "[" + format.format(new Date(System.currentTimeMillis())) + "] " + info;
    }
}