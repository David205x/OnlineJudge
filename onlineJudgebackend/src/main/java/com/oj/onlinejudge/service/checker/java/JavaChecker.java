package com.oj.onlinejudge.service.checker.java;

import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.checker.GenericChecker;
import com.oj.onlinejudge.service.checker.SampleWrapper;
import com.oj.onlinejudge.service.checker.impl.CodeParserImpl;
import com.oj.onlinejudge.utils.FilePathUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class JavaChecker extends CodeParserImpl implements GenericChecker {

    private final String JAVA_HOME = FilePathUtil.JAVA_HOME;            //获取主机中的 jdk 路径
    private final String BJUT_OJ_HOME = FilePathUtil.BJUT_OJ_HOME;
    ; // CHANGE THIS!!!!
    private final Map<String, String> prePacket = new HashMap<>();      // 编译阶段返回
    private final Map<String, String> postPacket = new HashMap<>();     // 运行阶段的返回
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

    public JavaChecker(String fileName, String srcDir, String dstDir, String submissionUUID) {
        this.fileName = fileName;
        this.srcDir = srcDir;
        this.dstDir = dstDir;

        if (this.JAVA_HOME == null) {
            throw new RuntimeException("Cannot locate local JAVA_HOME");
        }
        this.submissionUUID = submissionUUID;

        sw = new SampleWrapper();
        sw.initWrapper(1, dstDir, submissionUUID);

        this.fileName = fileName;
        this.srcDir = srcDir;
        this.filePath = srcDir + "\\" + fileName;

        this.dstDir = dstDir;

        this.inputFile = srcDir + "\\" + sw.getInputName();

        this.sampleOutputFile = dstDir + "\\" + sw.getOutputName();
        this.outputFile = dstDir + "\\" + submissionUUID + "_o.txt";
    }

    @Override
    public Map<String, String> compileAndRunFile(String debugInfo) throws IOException, InterruptedException, SQLException {

        final String extraHeaders = "import java.io.*;\nimport java.util.*;\nimport java.lang.management.ManagementFactory;\n" +
                "import java.lang.management.RuntimeMXBean;\n";

        sw.getSamplesFromDB();
        sw.sliceSamples(2);

        relatedFiles.add(inputFile);
        relatedFiles.add(sampleOutputFile);
        String PrintStreamIn = "\n\t\tSystem.setIn(new FileInputStream(\"" + inputFile + "\"));";
        String PrintStreamOut = "\n\t\tSystem.setOut(new PrintStream(new FileOutputStream(\"" + outputFile + "\")));\n";
        PrintStreamIn = PrintStreamIn.replaceAll("\\\\", "/");
        PrintStreamOut = PrintStreamOut.replaceAll("\\\\", "/");
        String streamRedirector = PrintStreamIn + PrintStreamOut;

        final String finalFileName = "Main_" + submissionUUID + ".java";
        FileHelper submittedCode = new FileHelper(srcDir + "\\" + finalFileName);
        relatedFiles.add(dstDir + "\\" + finalFileName);
        submittedCode.readAll();
        String srcCode = submittedCode.getAll();
        final String standardMainFunc = "\tpublic static void" + " main(String args[]) throws FileNotFoundException, NoSuchElementException" + "{\n";
        final String standardMainFunc1 = "public class" + " _Main_" + submissionUUID + "{\n";
        String[] insertCode = new String[2];
        insertCode[0] = "";
        insertCode[1] = standardMainFunc + streamRedirector;

        Map<String, String> response = Response(srcCode, ".*void\\s* main\\s*\\(.*\\)\\s*\\{.*", insertCode);
        if("CompileError".equals(response.get("error_message"))) {
            prePacket.put("RuntimeStatus", "CompileError");
            return prePacket;
        }
        srcCode = response.get("ParsedCodeString");
        insertCode[0] = extraHeaders;
        insertCode[1] = standardMainFunc1;
        response = Response(srcCode, ".*public\\s* class\\s* Main\\s*\\{.*", insertCode);
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


        // COMPILE
        Process compileProcess = null;
        String compileCmd = "javac " + BJUT_OJ_HOME + "\\files\\" + fileName;
        String runJavaCmd =  "java " + "-classpath " + BJUT_OJ_HOME + "\\files\\" + " _Main_" + submissionUUID;
        compileProcess = Runtime.getRuntime().exec(compileCmd);
        final InputStream errStream = compileProcess.getErrorStream();
        final String[] errMsg = {null};
        StringBuffer errInfo = new StringBuffer();
        relatedFiles.add(dstDir + "\\_Main_" + submissionUUID + ".class");
        relatedFiles.add(dstDir + "\\_Main_" + submissionUUID + ".java");

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
            final long clockStart = System.currentTimeMillis();
            final long[] RetCode = {-1};
            final boolean[] isOver = {false};
            final long[] timeLimitExceededFlag = {-1}; // if greater than 0 it means TLE happens.
            //final int[] peakMemUsed = {-1};

            //final int memoryLimit = 40960;
            try {
                final Process runProcess = Runtime.getRuntime().exec(runJavaCmd, null, new File(dstDir + "\\"));
                if (runProcess != null) {
                    final String[] pid = {null};
                    new Thread(() -> {
                        String finalMessage = "";
                        String line = null;
                        InputStream inputStream = runProcess.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                        StringBuilder runPidMessage = new StringBuilder();
                        while (true) {
                            try {
                                Thread.sleep(10);

                                if(br.ready() && pid[0] == null) {
                                    while ((line = br.readLine()) != null) {
                                        runPidMessage.append(line).append("\n");
                                    }
                                    if (!runPidMessage.toString().isEmpty()) {
                                        finalMessage = runPidMessage.toString();
                                        pid[0] = finalMessage.substring(finalMessage.indexOf("#") + 1, finalMessage.indexOf("^"));
                                        System.out.println(pid[0]);
                                        br.close();
                                        inputStream.close();
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }

                            if (System.currentTimeMillis() - clockStart > timeLimit) { // TLE
                                if(!isOver[0]){
                                    timeLimitExceededFlag[0] = System.currentTimeMillis() - clockStart;
                                }
                                isOver[0] = true;
                                runProcess.destroy();
                                return;
                            }
                        }
                    }).start();
                    runProcess.waitFor();
                    runProcess.destroy();
                    InputStream inputStream = runProcess.getErrorStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder runErrorMessage = new StringBuilder();
                    String finalMessage = "";
                    String line = null;
                    try {
                        if(br.ready()){
                            while ((line = br.readLine()) != null) {
                                runErrorMessage.append(line).append("\n");
                            }
                            if (!runErrorMessage.toString().isEmpty()) {
                                finalMessage = runErrorMessage.toString();
                                if(finalMessage.contains("NoSuchElementException")){
                                    RetCode[0] = 1;
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(RetCode[0] == 1){
                        prePacket.clear();
                        prePacket.put("RuntimeStatus", "Non Zero Exit Code");
                    }else if(timeLimitExceededFlag[0] > 0){
                        prePacket.clear();
                        prePacket.put("RuntimeStatus", "TimeLimitExceeded");
                    }else{
                        prePacket.clear();
                        prePacket.put("RuntimeStatus", "Accepted");
                    }
                    br.close();
                    inputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            prePacket.put("RuntimeStatus", "CompileError");
        }
        return prePacket;
    }

    @Override
    public Map<String, String> debugger() {
        return null;
    }

    @Override
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
                if(sample.size() <= submitted.size()){  // 提交代码输出多
                    for (int i = 0; i < sample.size(); i++) {
                        if (!(sample.get(i).equals(submitted.get(i)))) {
                            postPacket.put("JudgerStatus", "WrongAnswer");
                            postPacket.put("failedAt", Integer.toString(i + 1));
                            return postPacket;
                        }
                    }
                    if (submitted.size() > sample.size()){
                        postPacket.put("JudgerStatus", "WrongAnswer");
                        postPacket.put("failedAt", "more ans");
                        return postPacket;
                    }
                }else {                                 // 提交代码输出少
                    for (int i = 0; i < submitted.size(); i++) {
                        if (!(sample.get(i).equals(submitted.get(i)))) {
                            postPacket.put("JudgerStatus", "WrongAnswer");
                            postPacket.put("failedAt", Integer.toString(i + 1));
                            return postPacket;
                        }
                    }
                    postPacket.put("JudgerStatus", "WrongAnswer");
                    postPacket.put("failedAt", "less ans");
                    return postPacket;
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

        if("Accepted".equals(prePacket.get("RuntimeStatus"))){
            postPacket.put("JudgerStatus", "Accepted");
        } else {
            postPacket.put("JudgerStatus", prePacket.get("RuntimeStatus"));
        }

        return postPacket;
    }

    private ArrayList<String> answerSplitter(String answerString) {
        if (answerString.isEmpty()) {
            return null;
        } else {
            String[] container = answerString.split("\\r?\\n");
            return new ArrayList<>(Arrays.asList(container));
        }
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
