package com.oj.onlinejudge.service.checker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileHelper implements IOHelper {

    private String fileName;
    private String filePath;

    private String fullContent;
    private final ArrayList<String> fullContentByLines = new ArrayList<>();
    private int fileLength;

    private InputStream fileStream;
    private InputStreamReader fileReader;

    public FileHelper(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public int readAll() {

        int lineCnt = 0;

        try(
                FileInputStream fstream = new FileInputStream(this.filePath);
                InputStreamReader reader = new InputStreamReader(fstream, StandardCharsets.UTF_8);
                BufferedReader buf = new BufferedReader(reader);
        ) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = buf.readLine()) != null) {
                stringBuilder.append(line).append("\n");
                fullContentByLines.add(line);
                lineCnt++;
            }
            buf.close();
            fullContent = stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lineCnt;
    }

    public String getAll() {

        return fullContent;
    }

    public int writeAll(){

        // TODO: Finish writeAll();

        return this.fileLength;
    }

}
