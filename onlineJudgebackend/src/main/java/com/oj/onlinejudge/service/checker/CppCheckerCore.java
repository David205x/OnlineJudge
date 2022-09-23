package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CppCheckerCore {

    String root = System.getenv("BJUT_OJ_HOME");

    private final String submissionUUID;

    private final String fname = "main.cpp"; // TODO: Redirect the input to json packet sent back fron frontend.
    CppChecker c;

    public CppCheckerCore(String submissionUUID) {
        this.submissionUUID = submissionUUID;

        if (root != null) {
            root += "\\files";
        }
        else {
            throw new RuntimeException("Cannot locate file storage");
        }
    }

    public Map<String, String> checkSubmission() throws IOException, InterruptedException {

        String fname = submissionUUID + "_main.cpp";

        CppChecker c = new CppChecker(fname, root, root, submissionUUID);
        Map<String, String> compileResult = c.compileAndRunFile(root);



        Map<String, String> resultPacket = new HashMap<>();

        if (("Accepted").equals(compileResult.get("RuntimeStatus"))) { // No compile error.
            Map<String,String> runResult  = c.checker();
            resultPacket.put("SubmissionStatus", runResult.get("JudgerStatus"));
        }
        else {
            resultPacket.put("SubmissionStatus", compileResult.get("RuntimeStatus"));
        }
        c.clearUps();

        return resultPacket;
    }
}