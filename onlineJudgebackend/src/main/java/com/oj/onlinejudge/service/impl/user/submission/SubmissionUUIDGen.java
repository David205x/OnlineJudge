package com.oj.onlinejudge.service.impl.user.submission;

import java.util.Date;

public class SubmissionUUIDGen {

    private Date date;
    private String userKey;

    public SubmissionUUIDGen(String userKey) {
        this.userKey = userKey;
    }

    public String uuidGen() {
        final long baseSeed = 0xdead;
        int keyLength = userKey.length();
        StringBuilder finalSubUUID = new StringBuilder();

        StringBuilder userKeyPart = new StringBuilder();
        while(userKeyPart.length() < (6 - keyLength)) {
            userKeyPart.append("0");
        }
        userKeyPart.append(userKey);
        char[] userKeyArray = userKeyPart.toString().toCharArray();

        for (char c : userKeyArray) {
            finalSubUUID.append((c - '0' + keyLength) % 10);
        }

        this.date = new Date();
        String time = Long.toString(date.getTime());
        char[] timestamp = time.substring(time.length() - 6).toCharArray();
//      char[] timestamp = Long.toString(Double.doubleToLongBits(time % 1e9)).toCharArray();

        for (char c : timestamp) {
            finalSubUUID.append((baseSeed + c) % 10);
        }

        return finalSubUUID.toString();
    }


}
