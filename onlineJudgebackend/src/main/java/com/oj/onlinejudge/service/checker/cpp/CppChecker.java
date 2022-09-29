package com.oj.onlinejudge.service.checker.cpp;

import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.checker.GenericChecker;
import com.oj.onlinejudge.service.checker.SampleWrapper;
import com.oj.onlinejudge.service.checker.impl.CodeParserImpl;
import com.oj.onlinejudge.utils.FilePathUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class CppChecker extends CodeParserImpl implements GenericChecker {

    private final String MinGWPath = FilePathUtil.BJUT_OJ_MINGW;
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

    public Map<String, String> compileAndRunFile(String dstDir) throws IOException, InterruptedException {

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

        String[] insertCode = new String[2];
        insertCode[0] = extraHeaders;
        insertCode[1] = standardMainFunc + streamRedirector;
        Map<String, String> response = Response(srcCode, ".*int main\\(.*\\)\\s*\\{.*", insertCode);
        if("CompileError".equals(response.get("error_message"))) {
            prePacket.put("RuntimeStatus", "CompileError");
            return prePacket;
        }
        String finalSrcCode = response.get("ParsedCodeString");

        final String finalProduct = dstDir + "\\_" + finalFileName;
        FileHelper helper = new FileHelper(finalProduct);
        if (!helper.writeAll(finalSrcCode)) {
            prePacket.put("RuntimeStatus", "InternalError");
            return prePacket;
        }

        relatedFiles.add(outputFile);
        relatedFiles.add(finalProduct);


        Process compileProcess = null;
        String compileCmd = MinGWPath + "\\g++.exe " + finalProduct + " -o " + dstDir + "\\" + submissionUUID + ".exe";
        compileProcess = Runtime.getRuntime().exec(compileCmd);

        final InputStream errStream = compileProcess.getErrorStream();
        final String[] errMsg = {null};
        StringBuffer errInfo = new StringBuffer();

        relatedFiles.add(dstDir + "\\" + submissionUUID + ".exe");

        // COMPILE
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

        compileProcess.waitFor();
        compileProcess.destroy();

        // RUN
        if (errInfo.toString().isEmpty()) { // Timer thread
            final long timeLimit = 1000;

            final long[] timeLimitExceededFlag = {-1}; // if greater than 0 it means TLE happens.
            final long[] memoryLimitExceededFlag = {-1}; // if greater than 0 it means MLE happens.
            long PID = -1;
            final int memoryLimit = 256 * 1024;

            try {
                String runCmd = dstDir + "\\" + submissionUUID + ".exe";
                final Process runProcess = Runtime.getRuntime().exec(runCmd, null, new File(dstDir + "\\"));

                PID = runProcess.pid();
                final long clockStart = System.currentTimeMillis();

                String memDetectCmd = dstDir + "\\mem.exe " + PID + " " + timeLimit;
                final Process mdProcess = Runtime.getRuntime().exec(memDetectCmd, null, new File(dstDir + "\\"));
                final InputStream memUsageStream = mdProcess.getInputStream();
                StringBuilder memInfo = new StringBuilder();

                // MEM DETECTION
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

                if(runProcess.isAlive()){
                    timeLimitExceededFlag[0] = System.currentTimeMillis() - clockStart;
                }
                int memUsage = Integer.parseInt(memInfo.toString().trim());
                memoryLimitExceededFlag[0] = memUsage - memoryLimit;
                System.out.println(memUsage);
                runProcess.destroy();

            } catch (Exception e) {
                e.printStackTrace();
                prePacket.put("RuntimeStatus", "InternalError");
                return prePacket;
            }

            if (timeLimitExceededFlag[0] < 0) {
                prePacket.put("RuntimeStatus", "Accepted");
                if (memoryLimitExceededFlag[0] > 0) {
                    prePacket.put("RuntimeStatus", "MemoryLimitExceeded");
                    return prePacket;
                }
            }
            else if(!"Accepted".equals(prePacket.get("RuntimeStatus"))){
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
