package com.bigDragon.javase.concurrent.createThread.interrupt;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 interrupt
 一、简介
 在Java中，Thread.interrupt() 方法用于中断线程的执行。它通过将线程的中断状态设置为 true 来达到这个目的。但是，它并不会立即终止线程，
    而是提出一个中断请求，线程可以通过检查自身的中断状态来决定是否响应这个中断请求。
 */
public class interruptTest {
    public static void main(String[] args) throws InterruptedException{
        //是当线程处于运行状态时
        new interruptTest().threadRun();
        //当线程处于阻塞状态或者试图执行一个阻塞操作时
        new interruptTest().threadBlocked();
        //实际使用时，有时需要在编码时可能需要兼顾线程运行和线程阻塞两种情况
        new interruptTest().bothBlockedRun();
    }
    //是当线程处于运行状态时
    @Test
    public void threadRun() throws InterruptedException {
        Thread t1=new Thread(){
            @SneakyThrows
            @Override
            public void run(){
                while(true){
//                    TimeUnit.SECONDS.sleep(1);
                    //判断当前线程是否被中断
                    if (this.isInterrupted()){
                        System.out.println("线程中断");
                        break;
                    }else{
                        System.out.println("未被中断");
                    }
                }
            }
        };
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t1.interrupt();
        //...（未被中断）
        //未被中断
        //未被中断
        //线程中断
    }
    
    //当线程处于阻塞状态或者试图执行一个阻塞操作时
    @Test
    public void threadBlocked() throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                //while在try中，通过异常中断就可以退出run循环
                try {
                    while (true) {
                        //当前线程处于阻塞状态，异常必须捕捉处理，无法往外抛出
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Interruted When Sleep");
                    boolean interrupt = this.isInterrupted();
                    //中断状态被复位
                    System.out.println("interrupt:"+interrupt);
                }
            }
        };
        t1.start();
        TimeUnit.SECONDS.sleep(2);
        //中断处于阻塞状态的线程
        t1.interrupt();
        //Interruted When Sleep
        //interrupt:false
    }
    
    @Test
    public void bothBlockedRun() throws InterruptedException{
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
