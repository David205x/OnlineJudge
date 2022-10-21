package com.oj.onlinejudge.service.checker.utils.queues;

import com.oj.onlinejudge.mapper.UserMapper;
import com.oj.onlinejudge.service.Logger;
import com.oj.onlinejudge.service.impl.user.submission.GetSubmissionServiceImpl;
import com.oj.onlinejudge.service.user.submission.GetSubmissionService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SubmissionHandler implements Runnable {

    @Autowired
    private UserMapper userMapper;
    private final BlockingQueue<GetSubmissionServiceImpl> blockingQueue;

    private static Map<String, Map<String, String>> ret = null;

    private final int[] threadNo = {-1};

    public SubmissionHandler(BlockingQueue<GetSubmissionServiceImpl> blockingQueue, Map<String, Map<String, String>>  resultPool, int threadNo) {
        this.blockingQueue = blockingQueue;
        ret = resultPool;
        this.threadNo[0] = threadNo;
    }

    @Override
    public void run() {
        QueueLocks.setSubmissionHandlerRunningFlag(true);
        try {
            while (true) {
                GetSubmissionServiceImpl recent = blockingQueue.poll(10, TimeUnit.MILLISECONDS);
                if (recent != null) {
                    ret.put(recent.getSUUID(), recent.callChecker());
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            QueueLocks.setSubmissionHandlerRunningFlag(false);
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
