import java.io.*;
import java.util.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Scanner;

public class Main{

	public static void main(String args[]) throws FileNotFoundException, NoSuchElementException{

		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		System.out.println("#" + String.valueOf(Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue()) + "^");
	

		System.setIn(new FileInputStream("D:/OnlineJudge/files/111112035862_i"));
		System.setOut(new PrintStream(new FileOutputStream("D:/OnlineJudge/files/111112035862_o.txt")));


        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while(t != 0){
            t--;
            t++;
        }
      
    }
}
