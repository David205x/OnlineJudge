package com.oj.onlinejudge.service.checker;

import java.io.IOException;
import java.util.Map;

public interface GenericChecker {

    public Map<String, String> complieAndRunFile(String dstDir) throws IOException, InterruptedException;
    public Map<String, String> checker();

}
