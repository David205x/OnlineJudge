package com.oj.onlinejudge.service.checker.java;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JavaCheckerCore {
    String root = System.getenv("BJUT_OJ_HOME");

    private final String submissionUUID;

    public JavaCheckerCore(String submissionUUID) {
        this.submissionUUID = submissionUUID;

        if (root != null) {
            root += "\\files";
        }
        else {
            throw new RuntimeException("Cannot locate file storage");
        }
    }

    public Map<String, String> checkSubmission() throws IOException, InterruptedException, SQLException {

        String fname = "_Main_" + submissionUUID + ".java";

        JavaChecker j = new JavaChecker(fname, root, root, submissionUUID);
        Map<String, String> compileResult = j.compileAndRunFile(null);

        Map<String, String> resultPacket = new HashMap<>();

        if (("Accepted").equals(compileResult.get("RuntimeStatus"))) { // No compile error.
            Map<String,String> runResult  = j.checker();
            resultPacket.put("SubmissionStatus", runResult.get("JudgerStatus"));
        }
        else {
            resultPacket.put("SubmissionStatus", compileResult.get("RuntimeStatus"));
        }
        j.clearUps();

        return resultPacket;
    }
}
