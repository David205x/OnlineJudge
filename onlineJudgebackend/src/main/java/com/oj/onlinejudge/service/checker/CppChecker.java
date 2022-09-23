package com.oj.onlinejudge.service.checker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class CppChecker implements GenericChecker {

    private final String MinGWPath = System.getenv("BJUT_OJ_MINGW");
    ; // CHANGE THIS!!!!
    private final Map<String, String> prePacket = new HashMap<>();
    private final Map<String, String> postPacket = new HashMap<>();
    private String fileName;
    private String filePath;
    private String srcDir;
    private String dstDir;

    private String inputFile;
    private String outputFile;
    private String sampleOutputFile;
    private String submissionUUID;

    SampleWrapper sw;

    List<String> relatedFiles = new ArrayList<>();

    public CppChecker(String fileName, String srcDir, String dstDir, String submissionUUID) {

        if (this.MinGWPath == null) {
            throw new RuntimeException("Cannot locate local MinGW");
        }
        this.submissionUUID = submissionUUID;

        sw = new SampleWrapper(dstDir, submissionUUID);

        this.fileName = fileName;
        this.srcDir = srcDir;
        this.filePath = srcDir + "\\" + fileName;

        this.dstDir = dstDir;

        this.inputFile = srcDir + "\\" + sw.getInputName();
        this.sampleOutputFile = dstDir + "\\" + sw.getOutputName();
        this.outputFile = dstDir + "\\" + submissionUUID + "_o.txt";

    }

    public Map<String, String> complieAndRunFile(String dstDir) throws IOException, InterruptedException {

        final String extraHeaders = "#include<cstdlib>\n#include<cmath>\n#include<Windows.h>\n"; // optional

        sw.getSamplesFromDB();
        sw.wrapSamples();

        relatedFiles.add(inputFile);
        relatedFiles.add(sampleOutputFile);

        final String streamRedirector = "\n\tfreopen(\"" + sw.getInputName() + "\",\"r\", stdin);" +
                                        "\n\tfreopen(\"" + sw.getOutputName() +".txt\",\"w\", stdout);\n";

        final String finalFileName = submissionUUID + "_main.cpp";

        FileHelper submittedCode = new FileHelper(srcDir + "\\" + finalFileName);
        relatedFiles.add(dstDir + "\\" + finalFileName);
        submittedCode.readAll();
        String srcCode = submittedCode.getAll();

        final String standardMainFunc = "int main() {\n";

        StringBuilder codeBuilder = new StringBuilder();

        String[] injector = srcCode.split(".*int main\\(.*\\)\\s*\\{.*");

        if (injector.length != 2) {
            prePacket.put("RuntimeStatus", "CompileError");
            return prePacket;
        }

        codeBuilder.append(extraHeaders).append(injector[0]).append(standardMainFunc).append(streamRedirector).append(injector[1]);
        String finalSrcCode = codeBuilder.toString();

        final String finalProduct = dstDir + "\\_" + finalFileName;
        FileHelper helper = new FileHelper(finalProduct);
        if (!helper.writeAll(finalSrcCode)) {
            prePacket.put("RuntimeStatus", "InternalError");
            return prePacket;
        }

        relatedFiles.add(outputFile);
        relatedFiles.add(finalProduct);

        // TEST RUN
        int testrunMemUsage = 0;
        String[] testrunCmd = {"cmd.exe", "title testrun", "tasklist /fi \"imagename eq \"cmd.exe\""};
        // Process testrunner = Runtime.getRuntime().exec(testrunCmd);

        // COMPILE
        Process complieProcess = null;
        String compileCmd = MinGWPath + "\\g++.exe " + finalProduct + " -o " + dstDir + "\\" + submissionUUID;
        complieProcess = Runtime.getRuntime().exec(compileCmd);

        final InputStream errStream = complieProcess.getErrorStream();
        final String[] errMsg = {null};
        StringBuffer errInfo = new StringBuffer();

        relatedFiles.add(dstDir + "\\" + submissionUUID + ".exe");

        new Thread() {
            public void run() {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(errStream, StandardCharsets.UTF_8));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        errInfo.append(line).append("\n");
                    }
                    if (!errInfo.toString().isEmpty()) {
                        errMsg[0] = errInfo.toString();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        errStream.close();
                    } catch (IOException e) {
                        System.out.println(Arrays.toString(errMsg));
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        complieProcess.waitFor();
        complieProcess.destroy();

        // RUN
        if (errInfo.toString().isEmpty()) { // Timer thread
            final long timeLimit = 1000;
            final long clockStart = System.currentTimeMillis();

            final long[] timeLimitExceededFlag = {-1}; // if greater than 0 it means TLE happens.
            final int[] peakMemUsed = {-1};

            final int memoryLimit = 4096;


            try {
                String runCmd = dstDir + "\\" + submissionUUID + ".exe";
                final Process runProcess = Runtime.getRuntime().exec(runCmd, null, new File(dstDir + "\\"));
                if (runProcess != null) {
                    InputStream is = runProcess.getInputStream();
                    InputStream error = runProcess.getErrorStream();
                    new Thread() {
                        public void run() {
                            while (true) {
                                try {
                                    sleep(10);

                                    String memoryCheckCmd = "tasklist /fi \"imagename eq " + submissionUUID + ".exe\"";
                                    Process memChecker = Runtime.getRuntime().exec(memoryCheckCmd);
                                    InputStream memCheckerInputStream = memChecker.getInputStream();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(memCheckerInputStream, "GB2312"));

                                    String line = null;
                                    StringBuilder usedMem = new StringBuilder();

                                    while ((line = br.readLine()) != null) {
                                        usedMem.append(line).append("\n");
                                    }

                                    String memInfo = String.valueOf(usedMem);
                                    if (!memInfo.contains("===")) {
                                        memChecker.destroy();
                                    } else {

                                        int currentUsedMemory = memoryUsageExtractor(memInfo);
                                        if (currentUsedMemory > peakMemUsed[0]) {
                                            peakMemUsed[0] = currentUsedMemory;
                                        }
                                    }

                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                if (System.currentTimeMillis() - clockStart > timeLimit) { // TLE
                                    timeLimitExceededFlag[0] = System.currentTimeMillis() - clockStart;
                                    runProcess.destroy();
                                    return;
                                }
                            }
                        }
                    }.start();

                    runProcess.waitFor();
                    runProcess.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            while (timeLimitExceededFlag[0] < 0) {
                prePacket.clear();
                if (peakMemUsed[0] > memoryLimit) {
                    prePacket.put("RuntimeStatus", "MemoryLimitExceeded");
                    return prePacket;
                }
                else {
                    prePacket.put("RuntimeStatus", "Accepted");
                }
            }
            if (!("Accepted").equals(prePacket.get("RuntimeStatus"))) {
                prePacket.put("RuntimeStatus", "TimeLimitExceeded");
            }
        }
        else {
            prePacket.put("RuntimeStatus", "CompileError");
        }
        return prePacket;
    }

    public int memoryUsageExtractor(String bufferInfo) {
        String[] sliced = bufferInfo.split("\\s+");
        int index = sliced.length - 2;
        String memUsed = sliced[index].replace(",", "");
        return Integer.parseInt(memUsed);
    }

    public ArrayList<String> answerSplitter(String answerString) { // I/O string slicer
        if (answerString.isEmpty()) {
            return null;
        } else {
            String[] container = answerString.split("\\r?\\n");
            return new ArrayList<>(Arrays.asList(container));
        }
    }

    public Map<String, String> checker() {

        FileHelper submissionHelper = new FileHelper(outputFile);
        submissionHelper.readAll();
        String submittedAnswer = submissionHelper.getAll();
        if (submittedAnswer.isEmpty()) {
            postPacket.put("JudgerStatus", "WrongAnswer");
            postPacket.put("failedAt", Integer.toString(0));
            return postPacket;
        }

        FileHelper sampleHelper = new FileHelper(sampleOutputFile);
        sampleHelper.readAll();
        String sampleAnswer = sampleHelper.getAll();

        try {
            ArrayList<String> submitted = answerSplitter(submittedAnswer);
            ArrayList<String> sample = answerSplitter(sampleAnswer);
            if (sample != null && submitted != null) {
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
