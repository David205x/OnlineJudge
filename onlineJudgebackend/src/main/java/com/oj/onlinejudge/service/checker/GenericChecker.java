package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public interface GenericChecker {

    public Map<String, String> compileAndRunFile(String debugFlag) throws IOException, InterruptedException, SQLException;
    public Map<String, String> checker();
    public Map<String, String> debugger();
    public void clearUps();

}
