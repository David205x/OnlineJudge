package com.oj.onlinejudge.service.checker.java;

import com.oj.onlinejudge.service.checker.GenericChecker;
import com.oj.onlinejudge.service.checker.impl.CodeParserImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JavaChecker extends CodeParserImpl implements GenericChecker {

    private final String MinGWPath = System.getenv("JAVA_HOME");
    ; // CHANGE THIS!!!!
    private final Map<String, String> prePacket = new HashMap<>();
    private final Map<String, String> postPacket = new HashMap<>();
    private String fileName;
    private String filePath;
    private String srcDir;

    public JavaChecker(String fileName, String filePath, String srcDir, String dstDir) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.srcDir = srcDir;
        this.dstDir = dstDir;
    }

    private String dstDir;
    @Override
    public Map<String, String> compileAndRunFile(String dstDir) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public Map<String, String> checker() {
        return null;
    }
}
