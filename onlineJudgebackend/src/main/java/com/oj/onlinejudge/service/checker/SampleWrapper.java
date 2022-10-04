package com.oj.onlinejudge.service.checker;

import com.oj.onlinejudge.utils.FilePathUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void initWrapper(String fileStorage, String submissionUUID, String targetProblem) {
        this.sampleInput = new ArrayList<>();
        this.sampleOutput = new ArrayList<>();

        this.submissionUUID = submissionUUID;
        this.fileStorage = fileStorage;
        this.targetProblemKey = Integer.parseInt(targetProblem);

        inputFileBase = fileStorage + "\\" + submissionUUID + "_i";
        outputFileBase = fileStorage + "\\" + submissionUUID + "_o";

        sequencedInput = new ArrayList<>();
        this.step = -1;
    }

    public boolean getSamplesFromDB() throws SQLException {

        String fileRoot = FilePathUtil.BJUT_OJ_HOME + "\\onlineJudgebackend\\src\\main\\resources\\application.properties";

        FileHelper properties = new FileHelper(fileRoot);
        properties.readAll();
        String prop = properties.getAll();

        Pattern userReg = Pattern.compile("username=(.*)");
        Matcher userMat = userReg.matcher(prop);
        String user = null;
        if (userMat.find()) {
            user = userMat.group().split("=", 2)[1];
            System.out.println(user);
        }

        Pattern pwdReg = Pattern.compile("password=(.*)");
        Matcher pwdMat = pwdReg.matcher(prop);
        String password = null;
        if (pwdMat.find()) {
            password = pwdMat.group().split("=", 2)[1];
            System.out.println("password: " + password);
        }

        if (password == null || user == null) {
            return false;
        }

        String url = "jdbc:mysql://localhost:3306/online_judge?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8";
        String driver = "com.mysql.jdbc.Driver";

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
