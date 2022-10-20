package com.oj.onlinejudge.service.checker.utils.queues;

import com.oj.onlinejudge.service.impl.user.submission.GetSubmissionServiceImpl;
import net.minidev.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SubmissionHandler implements Runnable {

    private final BlockingQueue<JSONObject> blockingQueue;

    public SubmissionHandler(BlockingQueue<JSONObject> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        QueueLocks.setSubmissionHandlerRunningFlag(true);
        try {
            while (QueueLocks.isCheckerManagerRunningFlag()) {
                // get the checker task at the front of the queue and checks it
                JSONObject recent = blockingQueue.poll(10, TimeUnit.SECONDS);
                if (recent != null) {
                    GetSubmissionServiceImpl checker = new GetSubmissionServiceImpl();
                    // the JSON result of the checker is present here
                }
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            QueueLocks.setSubmissionHandlerRunningFlag(false);
            Thread.currentThread().interrupt();
        }
    }
}
