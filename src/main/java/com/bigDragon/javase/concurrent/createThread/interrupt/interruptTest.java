package com.bigDragon.javase.concurrent.createThread.interrupt;

/**
 interrupt
 一、简介
 在Java中，Thread.interrupt() 方法用于中断线程的执行。它通过将线程的中断状态设置为 true 来达到这个目的。但是，它并不会立即终止线程，
    而是提出一个中断请求，线程可以通过检查自身的中断状态来决定是否响应这个中断请求。
 */
public class interruptTest {
    public static void main(String[] args) {
        Thread myThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("线程运行中...");
                    Thread.sleep(1000); // 模拟耗时操作
                }
            } catch (InterruptedException e) {
                System.out.println("线程被中断了");
                // 可以选择终止线程
                // Thread.currentThread().interrupt(); // 再次设置中断状态，以便上层代码检查
            }
        });
        
        myThread.start();
        
        // 等待一段时间后中断线程
        try {
            Thread.sleep(5000); // 等待5秒钟
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 中断线程
        myThread.interrupt();
        System.out.println(myThread.getState());//TIMED_WAITING
    }
}
