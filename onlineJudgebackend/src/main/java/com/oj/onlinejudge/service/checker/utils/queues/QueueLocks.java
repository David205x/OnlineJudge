package com.oj.onlinejudge.service.checker.utils.queues;

public class QueueLocks {
    private static boolean checkerManagerRunningFlag = false;
    private static boolean submissionHandlerRunningFlag = false;

    public static boolean isCheckerManagerRunningFlag() {
        return checkerManagerRunningFlag;
    }

    public static void setCheckerManagerRunningFlag(boolean checkerManagerRunningFlag) {
        QueueLocks.checkerManagerRunningFlag = checkerManagerRunningFlag;
    }

    public static boolean isSubmissionHandlerRunningFlag() {
        return submissionHandlerRunningFlag;
    }

    public static void setSubmissionHandlerRunningFlag(boolean submissionHandlerRunningFlag) {
        QueueLocks.submissionHandlerRunningFlag = submissionHandlerRunningFlag;
    }
}
