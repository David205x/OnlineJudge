package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CppCheckerCore {

    String root = System.getenv("BJUT_OJ_HOME");
    private final String fname = "main.cpp"; // TODO: Redirect the input to json packet sent back fron frontend.
    CppChecker c;

    public CppCheckerCore() {
        if (root != null) {
            root += "\\files";
        }
        else {
            throw new RuntimeException("Cannot locate file storage");
        }
        c = new CppChecker(fname, root, root);
    }

    public Map<String, String> checkSubmission() throws IOException, InterruptedException {

        String root = System.getenv("BJUT_OJ_HOME");
        if (root == null) {
            throw new RuntimeException("Cannot locate file storage");
        }
        root += "\\files";
        // TODO: Redirect the input to json packet sent back fron frontend.
        String fname = "main.cpp";

        CppChecker c = new CppChecker(fname, root, root);
        Map<String, String> compileResult = c.complieAndRunFile(root);
        Map<String,String> runResult = c.checker();

        Map<String, String> resultPacket = new HashMap<>();

        if (("Accepted").equals(compileResult.get("RuntimeStatus"))) { // No compile error.
            resultPacket.put("SubmissionStatus", runResult.get("JudgerStatus"));
        }
        else {
            resultPacket.put("SubmissionStatus", compileResult.get("RuntimeStatus"));
        }
        c.clearUps();

        return resultPacket;
    }
}
