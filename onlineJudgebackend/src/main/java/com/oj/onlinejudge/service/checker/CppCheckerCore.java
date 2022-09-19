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
        Map<String, String> compileResult = c.complieAndRunFile(root);
        if (!"Accepted".equals(compileResult.get("PreProcessStatus"))) {
            System.out.println(compileResult.get("PreProcessStatus")); // If not compiled successfully
        }
        else {
            Map<String,String> runResult = c.checker();
            String status = runResult.get("RunProcessStatus");
            System.out.println(status + " " + ((status.equals("WrongAnswer")) ? runResult.get("failedAt") : "" + "\n"));
        }
        c.clearUps();

        // TODO: Wrap the judge result in a json packet.
        return new HashMap<String, String>();
    }
}
