package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public abstract class CheckerCaller {
    protected GenericChecker Checker;
    private String root = System.getenv("BJUT_OJ_HOME");
    private String submissionUUID;
    public CheckerCaller() {
        if (root != null) {
            root += "\\files";
        }
        else {
            throw new RuntimeException("Cannot locate file storage");
        }
    }

    public void setChecker(GenericChecker checker) {
        Checker = checker;
    }

    public abstract Map<String, String> checkSubmission() throws IOException, InterruptedException, SQLException;
}
