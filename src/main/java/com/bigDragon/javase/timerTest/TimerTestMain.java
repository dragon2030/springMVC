package com.bigDragon.javase.timerTest;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: bigDragon
 * @create: 2022/8/23
 * @Description:
 */
public class TimerTestMain {

    /**
     * 延迟100ms后，间隔1s打印出：hello world
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        }, 100, 1000);

    }
}
