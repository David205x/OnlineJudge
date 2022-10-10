package com.oj.onlinejudge.service.checker.generic;

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

    public FileHelper(String filePath) {
        String[] items = filePath.split("\\\\");
        this.fileName = items[items.length - 1];
        this.filePath = filePath;
    }

    public boolean readAll() {

        int lineCnt = 0;

        try (
                FileInputStream fstream = new FileInputStream(this.filePath);
                InputStreamReader reader = new InputStreamReader(fstream, StandardCharsets.UTF_8);
                BufferedReader buf = new BufferedReader(reader);
        ) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = buf.readLine()) != null) {
                stringBuilder.append(line).append("\n");
                fullContentByLines.add(line);
                lineCnt++;
            }
            buf.close();
            fullContent = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public String getAll() {

        return fullContent;
    }

    public ArrayList<String> getAllByLines() {

        return fullContentByLines;
    }

    public boolean writeAll(String fileContent) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try(FileWriter fileWriter = new FileWriter(filePath);) {

            fileWriter.write(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
