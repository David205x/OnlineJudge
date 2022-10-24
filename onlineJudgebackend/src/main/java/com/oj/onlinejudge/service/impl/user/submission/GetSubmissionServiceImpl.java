package com.oj.onlinejudge.service.impl.user.submission;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oj.onlinejudge.mapper.SubmissionMapper;
import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.pojo.Submission;
import com.oj.onlinejudge.pojo.User;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.checker.c.CCheckerCore;
import com.oj.onlinejudge.service.checker.cpp.CppCheckerCore;
import com.oj.onlinejudge.service.checker.utils.FileHelper;
import com.oj.onlinejudge.service.checker.java.JavaCheckerCore;
import com.oj.onlinejudge.service.checker.python.PythonCheckerCore;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetSubmissionServiceImpl implements GetSubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;
    @Autowired
    private UserMapper userMapper;
    private Map<String, String> params = new HashMap<>();

    @Override
    public String getSUUID() {return params.get("SID");}

    @Override
    public GetSubmissionServiceImpl getSubmission(String userKey,
                                                  String code,
                                                  String language,
                                                  String debugInfo,
                                                  String targetProblem,
                                                  String SID) {
        params.put("userKey", userKey);
        params.put("code", code);
        params.put("language", language);
        params.put("debugInfo", debugInfo);
        params.put("targetProblem", targetProblem);
        params.put("SID", SID);
        return this;
    }

    @Override
    public Map<String, String> callChecker() throws IOException {

        Logger.titleLogger("SUBMISSION");

        String root = System.getenv("BJUT_OJ_HOME");
        StringBuilder fileNameBuilder = new StringBuilder(root + "\\files\\");

        String userKey = params.get("userKey");
        String code = params.get("code");
        String language = params.get("language");
        String debugInfo = params.get("debugInfo");
        String targetProblem = params.get("targetProblem");
        String SID = params.get("SID");

        String jSID = SID.replace("-", "_");

        Logger.basicLogger("Checker", "SID: " + SID + " | Language: " + language);

        switch (language) {
            case "c": fileNameBuilder.append(SID).append("_main.c"); break;
            case "cpp": fileNameBuilder.append(SID).append("_main.cpp"); break;
            case "java": fileNameBuilder.append("Main_").append(jSID).append(".java"); break;
            case "python": fileNameBuilder.append("Main_").append(SID).append(".py"); break;
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
            CCheckerCore c = new CCheckerCore(SID, debugInfo, targetProblem);
            try {
                ret = c.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("cpp".equals(language)){
            CppCheckerCore cpp = new CppCheckerCore(SID, debugInfo, targetProblem);
            try {
                ret = cpp.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("java".equals(language)){
            JavaCheckerCore j = new JavaCheckerCore(jSID, debugInfo, targetProblem);
            try {
                ret = j.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if("python".equals(language)){
            PythonCheckerCore p = new PythonCheckerCore(SID, debugInfo, targetProblem);
            try {
                ret = p.checkSubmission();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        if (ret != null) {

            ret.put("SID", SID);

            if (!userKey.isEmpty() && ret.get("TimeElapsed") != null) {
                String status = ret.get("SubmissionStatus");

                QueryWrapper<User> userWrapper = new QueryWrapper<>();
                userWrapper.eq("id", Integer.parseInt(userKey));
                List<User> Users = userMapper.selectList(userWrapper);

                if (!"Finished".equals(status)) {
                    submissionMapper.insert(new Submission(
                            null,
                            Integer.parseInt(userKey),
                            Users.get(0).getUsername(),
                            Integer.parseInt(targetProblem),
                            new Timestamp(System.currentTimeMillis()),
                            ret.get("SubmissionStatus"),
                            Integer.parseInt(ret.get("TimeElapsed")),
                            language)
                    );
                }
                Logger.basicLogger("Checker", "Submission status: " + ret.get("SubmissionStatus"));
            }
        }

        return ret;
    }
}