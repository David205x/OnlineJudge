package com.oj.onlinejudge;

import org.junit.jupiter.api.Test;
import org.python.util.PythonInterpreter;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.Scanner;

@SpringBootTest
class OnlinejudgeApplicationTests {
    @Test
    void contextLoads() {
        PythonInterpreter interpreter = new PythonInterpreter();

        try {
            FileInputStream fis = new FileInputStream("url");
            {
                //将标准输入重定向到fis输入流
                System.setIn(fis);
                //使用System.in创建Scanner对象，用于获取标准输入
                Scanner sc = new Scanner(System.in);
                //增加下面一行把回车作为分隔符
                sc.useDelimiter("\n");
                //判断是否还有下一个输入项
                while(sc.hasNext())
                {
                    //输出输入项
                    System.out.println("键盘输入的内容时："+sc.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        interpreter.execfile("D:/test.py");
    }

}
