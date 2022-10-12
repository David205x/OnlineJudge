package com.oj.onlinejudge.service;

// TODO: Wrap all tempLogger methods here, making its method static.

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static int timesCalled = 0;
    private static PrintStream stream = System.out;
    private static final int defaultTitleLength = 36;

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat richFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void switchStream(PrintStream ps) {
        stream = ps;
    }

    public static void basicLogger(String info) {
        timesCalled++;
        stream.println("[" + format.format(new Date(System.currentTimeMillis()))+ "] " + info);
        return;
    }

    public static void placeholderLogger() {
        timesCalled++;
        StringBuilder logger = new StringBuilder("[" + format.format(new Date(System.currentTimeMillis()))+ "] ");
        for (int i = 0; i < defaultTitleLength; i++) {
            logger.append("*");
        }
        stream.println(logger);
    }

    public static void titleLogger(String title) {
        timesCalled++;
        int totalLength = defaultTitleLength;
        if (title.length() >= totalLength) {
            totalLength = title.length() + 8;
        }
        int titleLength = title.length();
        int left = (totalLength - titleLength) >> 1;
        int right = totalLength - left - titleLength;

        StringBuilder logger = new StringBuilder("[" + format.format(new Date(System.currentTimeMillis()))+ "] ");
        for (int i = 0; i < left; i++) {
            logger.append("*");
        }
        logger.append(title);
        for (int i = 0; i < right; i++) {
            logger.append("*");
        }
        stream.println(logger);
    }

    public int loggerCalled() {return timesCalled;}

}
