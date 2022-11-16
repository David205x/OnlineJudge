package com.oj.onlinejudge.service.checker.utils.queues;

import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.impl.user.submission.GetSubmissionServiceImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class QueueManager {

    private static final Integer maxQueueSize = 1024;

    private static BlockingQueue<GetSubmissionServiceImpl> blockingQueue;
    private static Map<String, Map<String, String>> resultPool;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    private static int threadCount = 0;
    private static int curThread = 0;
    private static final int maxThreadCount = 16;

    public static void initResultPool() {
        resultPool = new HashMap<>();
    }

    public static void initBlockingQueue() {
        blockingQueue = new LinkedBlockingQueue<>(maxQueueSize);
    }

    public static Map<String, Map<String, String>> getResultPool() {
        return resultPool == null ? new HashMap<>() : resultPool;
    }

    public static BlockingQueue<GetSubmissionServiceImpl> getBlockingQueue() {
        return blockingQueue == null ? new LinkedBlockingQueue<>(maxQueueSize) : blockingQueue;
    }

    public static void requestCheckerTask(GetSubmissionServiceImpl task) throws InterruptedException {

        if (blockingQueue != null) {
            if (!QueueLocks.isSubmissionHandlerRunningFlag()) {
                while(threadCount < maxThreadCount) {
                    SubmissionHandler checkerThread = new SubmissionHandler(blockingQueue, resultPool, threadCount);
                    threadCount++;
                    threadPool.execute(checkerThread);
                }
                QueueLocks.setSubmissionHandlerRunningFlag(true);
            }
            for (int i = 0; i < 10; i++) {
                if (blockingQueue.offer(task, 10, TimeUnit.MILLISECONDS)) {
                    curThread = (curThread + 1) % maxThreadCount;
                    break;
                }
                Thread.sleep(10);
            }
        } else {
            Logger.basicLogger("QueueManager", "Queue is currently full!");
        }
    }

    public static Map<String, String> pollCheckerTask(String SID) throws InterruptedException {

        if (resultPool != null && !resultPool.isEmpty()) {
            Map<String, String> ret =  resultPool.get(SID);
            resultPool.remove(SID);
            return ret;
        } else {
            return null;
        }
    }
}

