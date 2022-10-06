package com.oj.onlinejudge.service.impl.user.submission;

import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.service.checker.c.CCheckerCore;
import com.oj.onlinejudge.service.checker.cpp.CppCheckerCore;
import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.checker.java.JavaCheckerCore;
import com.oj.onlinejudge.service.checker.python.PythonCheckerCore;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class GetSubmissionServiceImpl implements GetSubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    @Override
    public Map<String, String> GetSubmission(String userKey, String code, String language, String debugInfo, String targetProblem) throws IOException {

        // TODO: Get problen from DB and wrap them in a package containing mem/time limit, testpoints count etc.

        System.out.println(tempLogger("-------------SUBMISSION--------------"));

        String root = System.getenv("BJUT_OJ_HOME");
        StringBuilder fileNameBuilder = new StringBuilder(root + "\\files\\");

        SubmissionUUIDGen gen = new SubmissionUUIDGen(userKey);
        final String submissionUUID = gen.uuidGen();
        System.out.println(tempLogger("SID: " + submissionUUID + " | Language: " + language));

        switch (language) {
            case "c": fileNameBuilder.append(submissionUUID).append("_main.c"); break;
            case "cpp": fileNameBuilder.append(submissionUUID).append("_main.cpp"); break;
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
        if("c".equals(language)){
            CCheckerCore c = new CCheckerCore(submissionUUID, debugInfo, targetProblem);
            try {
                ret = c.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("cpp".equals(language)){
            CppCheckerCore cpp = new CppCheckerCore(submissionUUID, debugInfo, targetProblem);
            try {
                ret = cpp.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("java".equals(language)){
            JavaCheckerCore j = new JavaCheckerCore(submissionUUID, debugInfo, targetProblem);
            try {
                ret = j.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("python".equals(language)){
            PythonCheckerCore p = new PythonCheckerCore(submissionUUID, debugInfo, targetProblem);
            try {
                ret = p.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (ret != null) {
            if (!userKey.isEmpty() && ret.get("TimeElapsed") != null) {
                String status = ret.get("SubmissionStatus");
                if (!"Finished".equals(status)) {
                    submissionMapper.insert(new Submission(
                            null,
                            Integer.parseInt(userKey),
                            Integer.parseInt(targetProblem),
                            new Timestamp(System.currentTimeMillis()),
                            ret.get("SubmissionStatus"),
                            Integer.parseInt(ret.get("TimeElapsed")),
                            language)
                    );
                }
                System.out.println(tempLogger("Submission status: " + ret.get("SubmissionStatus")));
            }
        }
        System.out.println(tempLogger("-------------------------------------"));

        return ret;
    }

    public String tempLogger(String info) {
        return "[" + format.format(new Date(System.currentTimeMillis())) + "] " + info;
    }
}