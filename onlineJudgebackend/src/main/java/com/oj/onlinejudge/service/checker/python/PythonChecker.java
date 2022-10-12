package com.oj.onlinejudge.service.checker.python;

import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.checker.generic.FileHelper;
import com.oj.onlinejudge.service.checker.generic.GenericChecker;
import com.oj.onlinejudge.service.checker.generic.SampleWrapper;
import com.oj.onlinejudge.service.checker.impl.CodeParserImpl;
import com.oj.onlinejudge.utils.FilePathUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PythonChecker extends CodeParserImpl implements GenericChecker {

    private final String root = FilePathUtil.BJUT_OJ_HOME + "\\files";
    ; // CHANGE THIS!!!!
    private final Map<String, String> prePacket = new HashMap<>();
    private final Map<String, String> postPacket = new HashMap<>();

    private String submissionUUID;
    private String targetProblem;
    private SampleWrapper sw;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private List<String> relatedFiles = new ArrayList<>();
    private Map<String, String> paths = new HashMap<>();

    public PythonChecker(String dstDir, String submissionUUID, String targetProblem) {

        if (this.root == null) {
            throw new RuntimeException("Cannot locate local oj home");
        }

        this.submissionUUID = submissionUUID;
        this.targetProblem = targetProblem;

        sw = new SampleWrapper();
        sw.initWrapper(dstDir, submissionUUID, targetProblem);

        paths.put("rootPath", dstDir + "\\");
        paths.put("submissionMainFile", dstDir + "\\Main_" + submissionUUID + ".py");
        paths.put("proceededMainFile", dstDir + "\\_Main_" + submissionUUID + ".py");
        paths.put("sampleInputFile", dstDir + "\\" + sw.getInputName());
        paths.put("sampleOutputFile", dstDir + "\\" + sw.getOutputName());
        paths.put("submissionOutputFile", dstDir + "\\" + submissionUUID + "_o.txt");

    }

    @Override
    public Map<String, String> compileAndRunFile(String debugInfo) throws IOException, InterruptedException, SQLException {

        final long[] timeElapsed = {-1};
        prePacket.put("TimeElapsed", "-1");

        boolean enableDebugMode = (debugInfo != null);
        String[] restrictions = sw.getRuntimeLimits();
        if (restrictions == null) {
            prePacket.put("RuntimeStatus", "SQLError");
            return prePacket;
        }

        final int testpoints = Integer.parseInt(restrictions[2]);

        // Step 1. LOAD SAMPEL IO FROM DB
        if (!enableDebugMode) {
            if (!(sw.getSamplesFromDB() && sw.sliceSamples(testpoints))) {
                prePacket.put("RuntimeStatus", "IOSamplesError");
                return prePacket;
            }
            sw.wrapOutputSamples();
            relatedFiles.add(paths.get("sampleOutputFile"));
            Logger.basicLogger("Samples files loaded.");
        } else {
            Logger.basicLogger("Debugging mode enabled.");
        }
        relatedFiles.add(paths.get("sampleInputFile"));

        File of = new File(paths.get("submissionOutputFile"));
        of.createNewFile();
        relatedFiles.add(paths.get("submissionOutputFile"));

        // Step 2. GET USER SUBMISSION
        FileHelper submittedCode = new FileHelper(paths.get("submissionMainFile"));
        relatedFiles.add(paths.get("submissionMainFile"));
        submittedCode.readAll();
        String srcCode = submittedCode.getAll();
        Logger.basicLogger("Submission source extracted.");

        // Step 3. BAKE THE SUBMISSION SOURCE FILE
        final String extraHeaders = "import sys\nimport os\n";
        // final String standardMainFunc = "if __name__ == '__main__':\n";
        final String streamRedirector =
                extraHeaders +
                        "fin = open(r'" + paths.get("sampleInputFile") + "', 'r')\n" +
                        "fout = open(r'" + paths.get("submissionOutputFile") + "', 'a')\n" +
                        "sys.stdin = fin\n" +
                        "sys.stdout = fout\n";
        final String fileCloser =
                "\nfout.close()\nfin.close()\n";

        String finalSrcCode = streamRedirector + srcCode + fileCloser;

        FileHelper helper = new FileHelper(paths.get("proceededMainFile"));
        if (!helper.writeAll(finalSrcCode)) {
            prePacket.put("RuntimeStatus", "InternalError");
            return prePacket;
        }
        relatedFiles.add(paths.get("proceededMainFile"));
        Logger.basicLogger("Source code baked.");

        // Step 4. PYTHON DOESNT NEED TO COMPILE

        // Step 5. RUN
        final long[] timeLimitExceededFlag = {-1}; // if greater than 0 it means TLE happens.
        final long[] memoryLimitExceededFlag = {-1}; // if greater than 0 it means MLE happens.

        final long timeLimit = Integer.parseInt(restrictions[1]);
        final int memoryLimit = Integer.parseInt(restrictions[0]);

        long PID = -1;

        // SYNTAX ERROR CHECK
        String testrunCmd =
                "python " + paths.get("submissionMainFile");
        final Process testrunProcess = Runtime.getRuntime().exec(testrunCmd, null, new File(paths.get("rootPath")));

        final InputStream testErrStream = testrunProcess.getErrorStream();
        StringBuffer testErrInfo = new StringBuffer();

        BufferedReader cebr = new BufferedReader(new InputStreamReader(testErrStream, StandardCharsets.UTF_8));
        new Thread(() -> {
            try {
                String line = null;
                while ((line = cebr.readLine()) != null) {
                    testErrInfo.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    testErrStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(timeLimit);
        testrunProcess.destroy();
        if (!testErrInfo.toString().isEmpty()) {
            prePacket.put("RuntimeStatus", "CompileError");
            return prePacket;
        }

        for (int curtp = 0; curtp < testpoints; curtp++) {

            if (!enableDebugMode) { // Runner
                if (!sw.wrapInputSamples(curtp)) {
                    prePacket.put("RuntimeStatus", "IOSamplesError");
                    return prePacket;
                }
                Logger.basicLogger("Code running on testpoint #" + (curtp + 1));
            } else { // Debugger
                if (curtp != 0) { // Debugger only runs once.
                    return prePacket;
                }
                if (debugInfo.isEmpty()) {
                    prePacket.put("RuntimeStatus", "DebuggerError");
                    return prePacket;
                }
                if (!sw.wrapInputSamples(debugInfo)) {
                    prePacket.put("RuntimeStatus", "IOSamplesError");
                    return prePacket;
                }
                Logger.basicLogger("Code running on debugging mode.");
            }

            try {
                String runCmd =
                        "python " + paths.get("proceededMainFile");
                final Process runProcess = Runtime.getRuntime().exec(runCmd, null, new File(paths.get("rootPath")));

                PID = runProcess.pid();

                String memDetectCmd = paths.get("rootPath") + "mem.exe " + PID + " " + timeLimit;
                final Process mdProcess = Runtime.getRuntime().exec(memDetectCmd, null, new File(paths.get("rootPath")));
                final InputStream memUsageStream = mdProcess.getInputStream();
                StringBuilder memInfo = new StringBuilder();

                // TIMER THREAD
                final long clockStart = System.currentTimeMillis();
                new Thread(() -> {
                    while (runProcess.isAlive()) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    long curTimeElapsed = System.currentTimeMillis() - clockStart;
                    prePacket.put("TimeElapsed", Long.toString(Math.max(curTimeElapsed, timeElapsed[0])));
                }).start();

                // MEM DETECTION THREAD
                new Thread(() -> {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(memUsageStream, StandardCharsets.UTF_8));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            memInfo.append(line).append("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
                mdProcess.waitFor();
                mdProcess.destroy();

                if (runProcess.isAlive()) {
                    timeLimitExceededFlag[0] = System.currentTimeMillis() - clockStart;
                } else {
                    timeLimitExceededFlag[0] = -1;
                }
                int memUsage = Integer.parseInt(memInfo.toString().trim());
                memoryLimitExceededFlag[0] = memUsage - memoryLimit;
                runProcess.destroy();
            } catch (Exception e) {
                e.printStackTrace();
                prePacket.put("RuntimeStatus", "InternalError");
                return prePacket;
            }

            // Step 6. GET RUNTIME STATUS
            if (timeLimitExceededFlag[0] < 0) {
                prePacket.put("RuntimeStatus", "Accepted");
                if (memoryLimitExceededFlag[0] > 0) {
                    prePacket.put("RuntimeStatus", "MemoryLimitExceeded");
                    return prePacket;
                }
            } else if (!"Accepted".equals(prePacket.get("RuntimeStatus"))) {
                prePacket.put("RuntimeStatus", "TimeLimitExceeded");
                return prePacket;
            }
        }
        return prePacket;
    }

    public ArrayList<String> answerSplitter(String answerString) { // I/O string slicer
        if (answerString.isEmpty()) {
            return null;
        } else {
            String[] container = answerString.split("\\r?\\n");
            return new ArrayList<>(Arrays.asList(container));
        }
    }

    @Override
    public Map<String, String> debugger() {
        FileHelper submissionHelper = new FileHelper(paths.get("submissionOutputFile"));
        submissionHelper.readAll();
        String submittedAnswer = submissionHelper.getAll();

        if (submittedAnswer.isEmpty()) {
            postPacket.put("DebuggerStatus", "InvalidOutput");
        } else {
            postPacket.put("DebuggerStatus", submittedAnswer.trim());
        }

        return postPacket;
    }

    @Override
    public Map<String, String> checker() {

        FileHelper submissionHelper = new FileHelper(paths.get("submissionOutputFile"));
        submissionHelper.readAll();
        String submittedAnswer = submissionHelper.getAll();

        if (submittedAnswer.isEmpty()) {
            postPacket.put("JudgerStatus", "WrongAnswer");
            postPacket.put("failedAt", Integer.toString(0));
            return postPacket;
        }

        FileHelper sampleHelper = new FileHelper(paths.get("sampleOutputFile"));
        sampleHelper.readAll();
        String sampleAnswer = sampleHelper.getAll();

        try {
            ArrayList<String> submitted = answerSplitter(submittedAnswer);
            ArrayList<String> sample = answerSplitter(sampleAnswer);
            if (sample != null && submitted != null) {
                if (sample.size() != submitted.size()) {
                    postPacket.put("JudgerStatus", "WrongAnswer");
                    postPacket.put("failedAt", Integer.toString(-1));
                    return postPacket;
                }
                for (int i = 0; i < sample.size(); i++) {
                    if (!(sample.get(i).equals(submitted.get(i)))) {
                        postPacket.put("JudgerStatus", "WrongAnswer");
                        postPacket.put("failedAt", Integer.toString(i + 1));
                        return postPacket;
                    }
                }
            } else {
                postPacket.put("JudgerStatus", "InternalError");
                return postPacket;
            }
        } catch (Exception e) {
            postPacket.put("JudgerStatus", "InternalError");
            e.printStackTrace();
            return postPacket;
        }
        postPacket.put("JudgerStatus", "Accepted");
        return postPacket;
    }

    @Override
    public void clearUps() {

        for (String fileItem : this.relatedFiles) {
            try {
                File file = new File(fileItem);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

}
