package com.oj.onlinejudge.service.checker;

import java.io.File;
import java.util.ArrayList;

public class SampleWrapper {

    private ArrayList<Long> sampleInput;
    private ArrayList<Long> sampleOutput;

    private String root;
    private String submissionUUID;

    public SampleWrapper(String root, String submissionUUID) {
        this.root = root;
        this.submissionUUID = submissionUUID;
    }

    public boolean getSamplesFromDB() {
        // TODO: fetch samples from DB to arraylists.
        // TODO: Use methods in ProblemParser to parse IOSamples.

        return true;
    }

    public boolean wrapSamples() {
        // TODO: wrap samples in two files.

        FileHelper ifileHelper = new FileHelper(root + "\\sample\\i");
        FileHelper ofileHelper = new FileHelper(root + "\\sample\\o");

        FileHelper inputHelper = new FileHelper(root + "\\" + submissionUUID + "_i");
        FileHelper outputHelper = new FileHelper(root + "\\" + submissionUUID + "_o");
        ifileHelper.readAll();
        inputHelper.writeAll(ifileHelper.getAll());
        ofileHelper.readAll();
        outputHelper.writeAll(ofileHelper.getAll());
        return true;
    }

    public String getInputName() {
        return submissionUUID + "_i";
    }

    public String getOutputName() {
        return submissionUUID + "_o";
    }


}
