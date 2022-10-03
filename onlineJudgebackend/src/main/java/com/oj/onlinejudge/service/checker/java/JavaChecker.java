package com.oj.onlinejudge.service.checker.java;

import com.oj.onlinejudge.service.checker.FileHelper;
import com.oj.onlinejudge.service.checker.GenericChecker;
import com.oj.onlinejudge.service.checker.SampleWrapper;
import com.oj.onlinejudge.service.checker.impl.CodeParserImpl;
import com.oj.onlinejudge.utils.FilePathUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JavaChecker extends CodeParserImpl implements GenericChecker {

    private final String JAVA_HOME = FilePathUtil.JAVA_HOME;            //获取主机中的 jdk 路径
    private final String BJUT_OJ_HOME = FilePathUtil.BJUT_OJ_HOME;
    ; // CHANGE THIS!!!!
    private final Map<String, String> prePacket = new HashMap<>();
    private final Map<String, String> postPacket = new HashMap<>();

    private String submissionUUID;
    private SampleWrapper sw;
    private final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private List<String> relatedFiles = new ArrayList<>();
    private Map<String, String> paths = new HashMap<>();


    public JavaChecker(String dstDir, String submissionUUID, String targetProblem) {
        if (this.JAVA_HOME == null) {
            throw new RuntimeException("Cannot locate local MinGW");
        }
        this.submissionUUID = submissionUUID;

        sw = new SampleWrapper();
        sw.initWrapper(dstDir, submissionUUID, targetProblem);

        paths.put("rootPath", dstDir + "\\");
        paths.put("submissionMainFile", dstDir + "\\Main_" + submissionUUID + ".java");
        paths.put("submissionExecutable", dstDir + "\\" + submissionUUID + ".class");
        paths.put("proceededMainFile", dstDir + "\\_Main_" + submissionUUID + ".java");
        paths.put("sampleInputFile", dstDir + "\\" + sw.getInputName());
        paths.put("sampleOutputFile", dstDir + "\\" + sw.getOutputName());
        paths.put("submissionOutputFile", dstDir + "\\" + submissionUUID + "_o.txt");
    }

    public String tempLogger(String info) {
        return "[" + format.format(new Date(System.currentTimeMillis())) + "] " + info;
    }

    @Override
    public Map<String, String> compileAndRunFile(String debugInfo) throws IOException, InterruptedException, SQLException {

        boolean enableDebugMode = (debugInfo != null);

        int testpoints = 2; // get this from problem db later.

        if (!enableDebugMode) {
            if (!(sw.getSamplesFromDB() && sw.sliceSamples(testpoints))) {
                prePacket.put("RuntimeStatus", "IOSamplesError");
                return prePacket;
            }
            sw.wrapOutputSamples();
            relatedFiles.add(paths.get("sampleOutputFile"));
            System.out.println(tempLogger("Samples files loaded."));
        } else {
            System.out.println(tempLogger("Debugging mode enabled."));
        }
        relatedFiles.add(paths.get("sampleInputFile"));
        relatedFiles.add(paths.get("submissionOutputFile"));

        FileHelper submittedCode = new FileHelper(paths.get("submissionMainFile"));
        relatedFiles.add(paths.get("submissionMainFile"));
        submittedCode.readAll();
        String srcCode = submittedCode.getAll();
        System.out.println(tempLogger("Submission source extracted."));
        final String extraHeaders = "import java.io.*;\nimport java.util.*;\nimport java.lang.management.ManagementFactory;\n" +
                "import java.lang.management.RuntimeMXBean;\n";
        String PrintStreamIn = "\n\t\tSystem.setIn(new FileInputStream(\"" + paths.get("sampleInputFile") + "\"));";
        String PrintStreamOut = "\n\t\tSystem.setOut(new PrintStream(new FileOutputStream(\"" + paths.get("submissionOutputFile") + "\", true)));\n";
        PrintStreamIn = PrintStreamIn.replaceAll("\\\\", "/");
        PrintStreamOut = PrintStreamOut.replaceAll("\\\\", "/");
        String streamRedirector = PrintStreamIn + PrintStreamOut;

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

        FileHelper helper = new FileHelper(paths.get("proceededMainFile"));
        if (!helper.writeAll(finalSrcCode)) {
            prePacket.put("RuntimeStatus", "InternalError");
            return prePacket;
        }
        relatedFiles.add(paths.get("proceededMainFile"));
        System.out.println(tempLogger("Source code baked."));


        // COMPILE
        Process compileProcess = null;
        String compileCmd = "javac " + paths.get("proceededMainFile");
        String runJavaCmd =  "java " + "-classpath " + BJUT_OJ_HOME + "\\files\\" + " _Main_" + submissionUUID;
        compileProcess = Runtime.getRuntime().exec(compileCmd);

        final InputStream errStream = compileProcess.getErrorStream();
        final String[] errMsg = {null};
        StringBuffer errInfo = new StringBuffer();

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

        relatedFiles.add(paths.get("submissionExecutable"));
        // RUN
        if (errInfo.toString().isEmpty()) { // Timer thread
            System.out.println(tempLogger("Source compiled."));
            final long timeLimit = 1000;

            long PID = -1;
            final long[] timeLimitExceededFlag = {-1}; // if greater than 0 it means TLE happens.
            final long[] memoryLimitExceededFlag = {-1}; // if greater than 0 it means MLE happens.
            final int memoryLimit = 64 << 20;

            for (int curtp = 0; curtp < testpoints; curtp++) {
                final long[] RetCode = {-1};
                if (!enableDebugMode) { // Runner
                    if (!sw.wrapInputSamples(curtp)) {
                        prePacket.put("RuntimeStatus", "IOSamplesError");
                        return prePacket;
                    }
                    System.out.println(tempLogger("Code running on testpoint #") + (curtp + 1));
                } else { // Debugger
                    if (curtp != 0) { // Debugger only runs for one time.
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
                    System.out.println(tempLogger("Code running on debugging mode."));
                }
                try {
                    final Process runProcess = Runtime.getRuntime().exec(runJavaCmd, null);

                    PID = runProcess.pid();
                    final long clockStart = System.currentTimeMillis();

                    String memDetectCmd = paths.get("rootPath") + "mem.exe " + PID + " " + timeLimit;
                    final Process mdProcess = Runtime.getRuntime().exec(memDetectCmd, null, new File(paths.get("rootPath")));
                    final InputStream memUsageStream = mdProcess.getInputStream();
                    StringBuilder memInfo = new StringBuilder();

                    // MEM DETECTION THREAD
                    new Thread(() -> {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(memUsageStream, StandardCharsets.UTF_8));
                            String line = null;
                            while ((line = br.readLine()) != null) {
                                memInfo.append(line).append("\n");
                            }
                            String finalMessage = "";
                            String lineError = null;
                            InputStream inputStreamError = runProcess.getInputStream();
                            BufferedReader brError = new BufferedReader(new InputStreamReader(inputStreamError, StandardCharsets.UTF_8));
                            StringBuilder runErrorMessage = new StringBuilder();
                            if(brError.ready()){
                                while ((lineError = brError.readLine()) != null) {
                                    runErrorMessage.append(lineError).append("\n");
                                }
                                if (!runErrorMessage.toString().isEmpty()) {
                                    finalMessage = runErrorMessage.toString();
                                    if(finalMessage.contains("NoSuchElementException")){
                                        RetCode[0] = 1;
                                    }
                                }
                            }
                            brError.close();
                            inputStreamError.close();
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
                if(RetCode[0] == 1){
                    prePacket.clear();
                    prePacket.put("RuntimeStatus", "Non Zero Exit Code");
                }
                else if (timeLimitExceededFlag[0] < 0) {
                    prePacket.put("RuntimeStatus", "Accepted");
                    if (memoryLimitExceededFlag[0] > 0) {
                        System.out.println(tempLogger("MemoryLimitExceeded!"));
                        prePacket.put("RuntimeStatus", "MemoryLimitExceeded");
                        return prePacket;
                    }
                } else if (!"Accepted".equals(prePacket.get("RuntimeStatus"))) {
                    System.out.println(tempLogger("TimeLimitExceeded!"));
                    prePacket.put("RuntimeStatus", "TimeLimitExceeded");
                    return prePacket;
                }
            }

        }
        else {
            prePacket.put("RuntimeStatus", "CompileError");
        }
        return prePacket;
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
