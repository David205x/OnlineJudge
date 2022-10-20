package com.oj.onlinejudge.service.checker.utils.queues;

import net.minidev.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CheckerManager implements Runnable {
    private final BlockingQueue<JSONObject> blockingQueue;

    public CheckerManager(BlockingQueue<JSONObject> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        QueueLocks.setCheckerManagerRunningFlag(true);
        try {
            while (QueueLocks.isSubmissionHandlerRunningFlag())
            {
                // this should be the jsonobject send from frontend, containing the user's submission data.
                JSONObject checkerTask = new JSONObject();

                int counter = 0;
                // 10 attempts to offer the current submission to the checker task manager.
                while(!blockingQueue.offer(checkerTask, 10, TimeUnit.SECONDS) && counter++ < 10) {
                    Thread.sleep(20);
                }

                Thread.sleep(20);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            QueueLocks.setCheckerManagerRunningFlag(false);
            Thread.currentThread().interrupt();
        }
    }
}
