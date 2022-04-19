package com.bigDragon.timedTask.javaUtilTimer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: bigDragon
 * @create: 2022/3/28
 * @Description:
 */
public class TimerTest {
    public static void main(String[] args) {
        new TimerTest().test2();
    }

    public  void test1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer is running");
            }
        }, 2000);
    }

    public void test2(){
        Timer timer = new Timer();
        timer. scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Timer is running");
            }
        }, 2000, 5000);
    }
}
