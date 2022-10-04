package com.oj.onlinejudge.service.checker;

import com.oj.onlinejudge.pojo.IO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SampleWrapper {

    private List<String> sampleInput;
    private ArrayList<String> sequencedInput;
    private List<String> sampleOutput;
    private String inputFileBase;
    private String outputFileBase;
    private String fileStorage;
    private String submissionUUID;
    private int targetProblemKey;
    private int step;

    public void initWrapper(int targetProblem, String fileStorage, String submissionUUID) {
        this.sampleInput = new ArrayList<>();
        this.sampleOutput = new ArrayList<>();

        this.submissionUUID = submissionUUID;
        this.fileStorage = fileStorage;
        this.targetProblemKey = targetProblem;

        inputFileBase = fileStorage + "\\" + submissionUUID + "_i";
        outputFileBase = fileStorage + "\\" + submissionUUID + "_o";

        sequencedInput = new ArrayList<>();
        this.step = -1;
    }

    public boolean getSamplesFromDB() throws SQLException {
        
        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://localhost:3306/online_judge?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8";
        String user="root";
        String password="B9XJ/mQaW%*/";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {

            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT input, output FROM io WHERE problemKey = ?;";
            pst = conn.prepareStatement(sql);
            pst.setString(1, Integer.toString(targetProblemKey));
            rs = pst.executeQuery();

            while (rs.next()) {
                sampleInput.add(rs.getString(1) + "\n");
                sampleOutput.add(rs.getString(2) + "\n");
            }

            return sampleInput.size() == sampleOutput.size();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean wrapOutputSamples() {
        StringBuilder outputBuilder = new StringBuilder();
        for (String o : sampleOutput) {
            outputBuilder.append(o);
        }
        FileHelper outputHelper = new FileHelper(outputFileBase);
        return outputHelper.writeAll(outputBuilder.toString());
    }

    public boolean sliceSamples(int slices) {

        step = sampleInput.size() % slices == 0 ? sampleInput.size() / slices : -1;
        if (step < 0) {
            return false;
        }
        for (int i = 0; i < slices; i++) {
            StringBuilder inputBuilder = new StringBuilder();
            for (int j = i * step; j < (i + 1) * step; j++) {
                inputBuilder.append(sampleInput.get(j));
            }
            sequencedInput.add(inputBuilder.toString());
        }
        return true;
    }

    public boolean wrapInputSamples(int index) {
        if (index >= sequencedInput.size()) {
            return false;
        }
        FileHelper inputHelper = new FileHelper(inputFileBase);
        return inputHelper.writeAll(Integer.toString(step) + "\n" + sequencedInput.get(index));
    }

    public boolean wrapInputSamples(String debugIO) {
        debugIO = debugIO.trim();
        debugIO += "\n";
        FileHelper inputHelper = new FileHelper(inputFileBase);
        return inputHelper.writeAll("1" + "\n" + debugIO);
    }

    public String getInputName() {
        return submissionUUID + "_i";
    }

    public String getOutputName() {
        return submissionUUID + "_o";
    }


}
