package com.oj.onlinejudge.service.checker.cpp;

import com.oj.onlinejudge.service.checker.CheckerCaller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CppCheckerCore extends CheckerCaller {

    String root = System.getenv("BJUT_OJ_HOME");

    String debugInfo;

    public CppCheckerCore(String submissionUUID, String debugInfo) {
        super();
        // TODO: Get debugInfo from frontend.
        String fname = submissionUUID + "_main.cpp";
        if (root != null) {
            root += "\\files";
        } else {
            throw new RuntimeException("Cannot locate file storage");
        }
        setChecker(new CppChecker(fname, root, root, submissionUUID));
        this.debugInfo = debugInfo;
    }

    public Map<String, String> checkSubmission() throws IOException, InterruptedException, SQLException {

        Map<String, String> compileResult = Checker.compileAndRunFile(debugInfo);

        Map<String, String> resultPacket = new HashMap<>();

        if (("Accepted").equals(compileResult.get("RuntimeStatus"))) { // No compile error.
            Map<String, String> runResult;
            if (debugInfo == null) {
                runResult = Checker.checker();
                resultPacket.put("SubmissionStatus", runResult.get("JudgerStatus"));
            } else {
                runResult = Checker.debugger();
                resultPacket.put("SubmissionStatus", runResult.get("DebuggerStatus"));
            }
        } else {
            resultPacket.put("SubmissionStatus", compileResult.get("RuntimeStatus"));
        }

        Checker.clearUps();

        return resultPacket;
    }
}