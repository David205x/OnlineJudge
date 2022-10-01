package com.oj.onlinejudge.service.checker.cpp;

import com.oj.onlinejudge.service.checker.CheckerCaller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CppCheckerCore extends CheckerCaller {

    String root = System.getenv("BJUT_OJ_HOME");

    public CppCheckerCore(String submissionUUID) {
        super();
        String fname = submissionUUID + "_main.cpp";
        if (root != null) {
            root += "\\files";
        }
        else {
            throw new RuntimeException("Cannot locate file storage");
        }
        setChecker(new CppChecker(fname, root, root, submissionUUID));
    }

    public Map<String, String> checkSubmission() throws IOException, InterruptedException, SQLException {

        Map<String, String> compileResult = Checker.compileAndRunFile(root);

        Map<String, String> resultPacket = new HashMap<>();

        if (("Accepted").equals(compileResult.get("RuntimeStatus"))) { // No compile error.
            Map<String,String> runResult  = Checker.checker();
            resultPacket.put("SubmissionStatus", runResult.get("JudgerStatus"));
        }
        else {
            resultPacket.put("SubmissionStatus", compileResult.get("RuntimeStatus"));
        }
        Checker.clearUps();

        return resultPacket;
    }
}