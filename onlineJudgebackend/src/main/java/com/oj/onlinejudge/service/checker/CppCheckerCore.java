package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CppCheckerCore {

    private final String root = "E:\\vue\\OnlineJudge\\files"; // CHANGE THIS!!!!
    private final String fname = "main.cpp"; // TODO: Redirect the input to json packet sent back fron frontend.
    CppChecker c = new CppChecker(fname, root, root);

    public Map<String, String> checkSubmission() throws IOException, InterruptedException {
        Map<String, String> compileResult = c.complieAndRunFile(root);
        if (!(compileResult.get("PreProcessStatus").equals("Accepted"))) {
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
