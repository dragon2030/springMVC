package com.bigDragon.javase.concurrent.createThread;

/**
 * join测试
 *
 * join概念
 * t.join()方法只会使主线程(或者说调用t.join()的线程)进入等待池并等待t线程执行完毕后才会被唤醒。并不影响同一时刻处在运行状态的其他线程。
 *
 * 实现功能：
 * 主线程内运行多个线程，当所有线程执行完毕以后主线程再执行
 *
 * @author bigDragon
 * @create 2020-12-31 10:37
 */
public class JoinMethod {
    public static void main(String[] args) throws Exception{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName()+"启动");
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName()+"执行完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        System.out.println("主线程开始执行");
        Thread[] threads=new Thread[10];
        for(int i=0;i<10;i++){
            Thread thread = new Thread(runnable);
            thread.setName("thread"+i);
            thread.start();
            threads[i]=thread;
            //thread.join();
        }
        for(Thread thread:threads){
            thread.join();
        }
        System.out.println("主线程执行完毕");

    }
    /**
     主线程开始执行
     thread0启动
     thread1启动
     thread2启动
     thread3启动
     thread6启动
     thread7启动
     thread4启动
     thread5启动
     thread8启动
     thread9启动
     thread0执行完成
     thread7执行完成
     thread4执行完成
     thread6执行完成
     thread3执行完成
     thread2执行完成
     thread5执行完成
     thread9执行完成
     thread8执行完成
     thread1执行完成
     主线程执行完毕
     */
}
