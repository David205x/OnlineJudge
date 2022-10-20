package com.oj.onlinejudge.service.checker.utils.queues;

import net.minidev.json.JSONObject;

import java.util.concurrent.*;

public class QueueManager {

    private static final Integer maxQueueSize = 1024;

    private static BlockingQueue<JSONObject> blockingQueue;

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static BlockingQueue<JSONObject> getBlockingQueue() {
        return blockingQueue == null ? new LinkedBlockingQueue<>(maxQueueSize) : blockingQueue;
    }

    public static void requireCheckerTask(JSONObject task) throws InterruptedException {

        // if target queue defined in submission controller exists
        if (blockingQueue != null) {
            if (blockingQueue.offer(task)) {
                // start a new submission handler thread if none at present
                if (!QueueLocks.isSubmissionHandlerRunningFlag()) {
                    SubmissionHandler checkerThread = new SubmissionHandler(blockingQueue);
                    threadPool.execute(checkerThread);
                    QueueLocks.setSubmissionHandlerRunningFlag(true);
                }
            } else {
                // 10 attempts to offer the current submission to the queue
                for (int i = 0; i < 10; i++) {
                    if (blockingQueue.offer(task, 10, TimeUnit.SECONDS)) {
                        break;
                    }
                    Thread.sleep(20);
                }
            }
        } else {
            // TODO: QUEUE IS FULL OR NOT FUCTIONING, DO SOMETHING LATER
        }
    }
}

