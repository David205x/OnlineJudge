package com.oj.onlinejudge.service.checker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

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

    List<String> relatedFiles = new ArrayList<>();

    public CppChecker(String fileName, String srcDir, String dstDir) {

        if (this.MinGWPath == null) {
            throw new RuntimeException("Cannot locate local MinGW");
        }

        this.fileName = fileName;
        this.srcDir = srcDir;
        this.filePath = srcDir + "\\" + fileName;

        this.dstDir = dstDir;
        this.inputFile = srcDir + "\\i";
        this.outputFile = dstDir + "\\o.txt";
        this.sampleOutputFile = dstDir + "\\o";
    }

    public Map<String, String> complieAndRunFile(String dstDir) throws IOException, InterruptedException {

        final String extraHeaders = "#include<cstdlib>\n#include<cmath>\n#include<Windows.h>\n";
        final String streamRedirector = "\n\tfreopen(\"i\",\"r\", stdin);\n\tfreopen(\"o.txt\",\"w\", stdout);\n";

        FileHelper submittedCode = new FileHelper("main.cpp", srcDir + "\\main.cpp");
        relatedFiles.add(srcDir + "\\main.cpp");
        submittedCode.readAll();
        // String srcCode = "#include<iostream>\n\nusing namespace std;\n\nint main() { \n\tSleep(200);\n\tstring s;\n\tcin >> s;\n\tcout << s << endl;\n\treturn 0;\n} ";
        String srcCode = submittedCode.getAll();

        final String standardMainFunc = "int main() {\n";

        StringBuilder codeBuilder = new StringBuilder();
        String[] injector = srcCode.split(".*int main\\(.*\\).*\\{.*");

        codeBuilder.append(extraHeaders).append(injector[0]).append(standardMainFunc).append(streamRedirector).append(injector[1]);
        String finalSrcCode = codeBuilder.toString();

        final String finalProduct = dstDir + "\\_main_.cpp";
        System.out.println(finalProduct);

        File cppFile = new File(finalProduct);

        if (cppFile.exists()) {
            if (!cppFile.delete()) {
                throw new RuntimeException("Error while overwriting file!");
            }
        }
        if (!cppFile.createNewFile()) {
            throw new RuntimeException("Error while creating file!");
        }

        if (cppFile.canWrite()) {
            try (FileWriter fileWriter = new FileWriter(cppFile);) {
                fileWriter.write(finalSrcCode);
            } catch (IOException e) {
                e.printStackTrace();
                prePacket.put("RuntimeStatus", "InternalError");
                return prePacket;
            }
        }

        relatedFiles.add(inputFile);
        relatedFiles.add(outputFile);
        relatedFiles.add(sampleOutputFile);
        relatedFiles.add(finalProduct);

        // TEST RUN
        int netMemUsage = 0;
        // TODO: RUN AN EMPTY COMMAND SHELL.

        // COMPILE
        Process complieProcess = null;
        String compileCmd = MinGWPath + "\\g++.exe " + finalProduct + " -o " + dstDir + "\\_main_";
        complieProcess = Runtime.getRuntime().exec(compileCmd);

        final InputStream errStream = complieProcess.getErrorStream();
        final String[] errMsg = {null};
        StringBuffer errInfo = new StringBuffer();

        relatedFiles.add(dstDir + "\\_main_.exe"); // .exe

        new Thread() { // Compiler output thread
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

            final int memoryLimit = 100;

            try {
                String runCmd = dstDir + "\\_main_.exe";
                final Process runProcess = Runtime.getRuntime().exec(runCmd, null, new File(dstDir + "\\"));
                if (runProcess != null) {
                    InputStream is = runProcess.getInputStream();
                    InputStream error = runProcess.getErrorStream();
                    new Thread() {
                        public void run() {
                            while (true) {
                                try {
                                    sleep(10);

//                                    映像名称                       PID 会话名              会话#       内存使用
//                                            ========================= ======== ================ =========== ============
//                                    _main_.exe                    1852 Console                    1      2,096 K

                                    String memoryCheckCmd = "tasklist /fi \"imagename eq _main_.exe\"";
                                    Process memChecker = Runtime.getRuntime().exec(memoryCheckCmd);
                                    InputStream memCheckerInputStream = memChecker.getInputStream();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(memCheckerInputStream, "GB2312"));

                                    String line = null;
                                    StringBuffer usedMem = new StringBuffer();

                                    while ((line = br.readLine()) != null) {
                                        usedMem.append(line).append("\n");
                                    }

                                    String memInfo = String.valueOf(usedMem);
                                    if (memInfo.contains("没有")) {
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

    public ArrayList<String> answerSplitter(String answerString, String seperator) { // I/O string slicer
        if (answerString.isEmpty()) {
            return new ArrayList<>();
        } else {
            String[] container = answerString.split("\\r?\\n");
            return new ArrayList<>(Arrays.asList(container));
        }
    }

    public Map<String, String> checker() {

        FileHelper submissionHelper = new FileHelper("o.txt", outputFile);
        submissionHelper.readAll();
        String submittedAnswer = submissionHelper.getAll();

        FileHelper sampleHelper = new FileHelper("o", sampleOutputFile);
        sampleHelper.readAll();
        String sampleAnswer = sampleHelper.getAll();

        try {
            ArrayList<String> submitted = answerSplitter(submittedAnswer, "\n");
            ArrayList<String> sample = answerSplitter(sampleAnswer, "\n");
            if (sample != null && submitted != null) {
                for (int i = 0; i < sample.size(); i++) {
                    if (!(sample.get(i).equals(submitted.get(i)))) {
                        postPacket.put("JugderStatus", "WrongAnswer");
                        postPacket.put("failedAt", Integer.toString(i + 1));
                        return postPacket;
                    }
                }
            } else {
                postPacket.put("JugderStatus", "InternalError");
                return postPacket;
            }
        } catch (Exception e) {
            postPacket.put("JugderStatus", "InternalError");
            e.printStackTrace();
            return postPacket;
        }
        postPacket.put("JugderStatus", "Accepted");
        return postPacket;
    }

    public boolean clearUps() {

        for (String fileItem : this.relatedFiles) {
            try {
                File file = new File(fileItem);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
