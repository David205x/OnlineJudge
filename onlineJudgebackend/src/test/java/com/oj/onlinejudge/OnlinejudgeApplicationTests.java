package com.oj.onlinejudge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java. io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class OnlinejudgeApplicationTests {
    @Test
    public void testFunction() throws IOException, InterruptedException {

        List<String> fileIndex = new LinkedList<>();

        // TODO: Change this file header to the standard c/cpp headers.
        final String headers = "#include<cstdlib>\n#include<cmath>\n";

        // TODO: Change the string source.
        // String src = "#include<cstdio>\n\nint main(int argc, char* argv[]) {\n    int t;\n    t = 1;\n    int cnt = 1;\n    while(t--){\n        solve(cnt++);\n    }\n    return 0;\n}\n";
        String src = "#include<iostream>\n\nusing namespace std;\n\nint main() { \n\tcout << \"test function.\" << endl;\n\treturn 0;\n} ";

        StringBuilder codeBldr = new StringBuilder(headers);
        final String injectedString = "\n\tfreopen(\"i.txt\",\"r\", stdin);\n\tfreopen(\"o.txt\",\"w\", stdout);\n";

        String[] strInjector = src.split(".*int main\\(.*\\).*\\{.*", 2);

        codeBldr.append(strInjector[0]).append("int main() {\n").append(injectedString).append(strInjector[1]);
        String srcCode = codeBldr.toString();
        // TODO: add a map for return values.

        // TODO: make file format as [problemKey_userKey_timestamp].txt
//        StringBuilder dateBldr = new StringBuilder();
//        Date date = new Date();
//        long currDate = date.getTime();

        String baseFileName = "E:\\vue\\OnlineJudge\\tmpfiles\\main.cpp";
        String baseFileDir = "E:\\vue\\OnlineJudge\\tmpfiles";
        fileIndex.add(baseFileName);

//        String[] finalFileName = baseFileName.split(".*\\.txt.*", 2);
//
//        for (String s : finalFileName) {
//            System.out.println(s);
//        }
//
//        dateBldr.append(finalFileName[0]).append(currDate).append(finalFileName[1]);

//        System.out.println(dateBldr);

        File finalProduct = new File(baseFileName);
        // finalProduct.deleteOnExit();
        if (!finalProduct.createNewFile()) {
            throw new IOException("cannot create new file!");
        }

        if (finalProduct.canWrite()) {
            try (FileWriter fileWriter = new FileWriter(baseFileName);) {
                fileWriter.write(srcCode);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        String inputFile = "E:\\vue\\OnlineJudge\\tmpfiles\\i.txt";
        String outputFile = "E:\\vue\\OnlineJudge\\tmpfiles\\o.txt";
        fileIndex.add(inputFile);
        fileIndex.add(outputFile);

        List<String> cmdGroup = new LinkedList<>();
        cmdGroup.add("cmd.exe");
        cmdGroup.add(Character.toString(baseFileDir.charAt(0))+":");
        cmdGroup.add("g++ " + baseFileName + " -o " + baseFileDir + "\\main");
        ProcessBuilder processBuilder = new ProcessBuilder(cmdGroup);
        Process process = processBuilder.start();

//        String cmdLine = "E:\\vue\\MinGW\\bin\\g++.exe " + baseFileName + " -o " + baseFileDir + "\\main";
//        System.out.println(cmdLine);
//        Process process = Runtime.getRuntime().exec(cmdLine);

        System.out.println(process.getErrorStream());
        process.waitFor();
        process.destroy();

        return;
    }

}
