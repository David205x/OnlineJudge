package com.oj.onlinejudge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


@SpringBootTest
class OnlinejudgeApplicationTests {
    @Test
    void contextLoads() throws IOException {

        try {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            throw new RuntimeException(String.valueOf(Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue()));
        }catch (Exception ex){}
//        Logger LOGGER = LoggerFactory.getLogger("OnlinejudgeApplicationTests.class");
//        try {
//            LOGGER.debug("main方法开始执行了~~~");
//            LOGGER.info("我开始记录第二行日志，我要开始做除法~~~");
//            int a = 10;
//            int b = 0;
//            LOGGER.trace("a = "+ a);
//            LOGGER.trace("b = " + b);
//
//            //System.out.println(a / b);
//        } catch (Exception e){
//            LOGGER.error("功能出现异常，"+ e);
//        }
//        int a[][] = new int[10000][10000];
//        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
//
//        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage(); //椎内存使用情况
//        long totalMemorySize = memoryUsage.getInit(); //初始的总内存
//
//        long maxMemorySize = memoryUsage.getMax(); //最大可用内存
//
//        long usedMemorySize = memoryUsage.getUsed(); //已使用的内存
//
//        System.out.println("初始内存 " + (double)(totalMemorySize) / 1024 / 1024);
//        System.out.println("最大可用内存 " + (double)maxMemorySize / 1024 / 1024);
//        System.out.println("已使用内存 " + (double)usedMemorySize / 1024 / 1024);
        //        String[] s = new String[1];
//        s[0] = "PYTHONPATH=D:/python";
//        String runCmd = "D:\\python\\python.exe C:\\Users\\luo'xing'yue\\Desktop\\test.py";
//        try{
//            final Process runProcess = Runtime.getRuntime().exec(runCmd, s, new File("C:/Users/luo'xing'yue/Desktop/"));
//            final Process checkProcess = Runtime.getRuntime().exec("tasklist /fi \"imagename eq py.exe\"");
//            InputStream memCheckerInputStream = checkProcess.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(memCheckerInputStream, "GB2312"));
//
//            String line = null;
//            StringBuffer usedMem = new StringBuffer();
//
//            while ((line = br.readLine()) != null) {
//                usedMem.append(line).append("\n");
//            }
//            System.out.println(usedMem.toString());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }


}
