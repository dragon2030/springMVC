package com.bigDragon.javase.concurrent.createThread.threadState;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 java中线程的状态
 NEW (新建)
 RUNNABLE (可运行)
 BLOCKED (阻塞)
 WAITING (无限期等待)
 TIMED_WAITING (限期等待)
 TERMINATED (终止)
 */
public class ThreadState {
    public static void main (String[] args) throws Exception{
        //新建
        new ThreadState().thread_NEW();
        //可运行
        new ThreadState().thread_RUNNABLE();
        //阻塞
        new ThreadState().thread_BLOCKED();
        //无限期等待
        new ThreadState().thread_WAITING();
        //TIMED_WAITING (限期等待)
        new ThreadState().TIMED_WAITING();
        //TERMINATED (终止)
        new ThreadState().TERMINATED();
    }
    //TERMINATED (终止)
    @Test
    public void TERMINATED(){
        Thread thread = new Thread();
        thread.start();
        System.out.println(thread.getState());
        //RUNNABLE
    }
    //TIMED_WAITING (限期等待)
    @Test
    public void TIMED_WAITING(){
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(10*1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            Thread.sleep(100); // 等待，确保线程执行到需要的代码中
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getState());
        //TIMED_WAITING
    }
    //WAITING (无限期等待)
    @Test
    public void thread_WAITING(){
        Thread thread = new Thread(()->{
            System.out.println("LockSupport.park()之前线程状态："+Thread.currentThread().getState());
            LockSupport.park();
        });
        thread.start();
        try {
            Thread.sleep(100); // 等待，确保线程执行到需要的代码中
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("LockSupport.park()之后线程状态："+thread.getState());
        //LockSupport.park()之前线程状态：RUNNABLE
        //LockSupport.park()之后线程状态：WAITING
    }
    @Test
    public void thread_WAITING_test(){
        try {
            Thread thread = new Thread(()->{
                System.out.println("LockSupport.park()之前线程状态："+Thread.currentThread().getState());
                LockSupport.park();
            });
            thread.start();
            try {
                Thread.sleep(100); // 等待，确保线程执行到需要的代码中
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("LockSupport.park()之后线程状态："+thread.getState());
            thread.interrupt();
            System.out.println("LockSupport.park()之后线程状态："+thread.getState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //LockSupport.park()之前线程状态：RUNNABLE
        //LockSupport.park()之后线程状态：WAITING
    }
   
    //阻塞
    @Test
    public void thread_BLOCKED(){
        Runnable runnable= ()->{
            synchronized(this) {
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t.start();
        t2.start();
        System.out.println(t.getState());
        System.out.println(t2.getState());
        //RUNNABLE
        //BLOCKED
    }
    //可运行
    @Test
    public void thread_RUNNABLE(){
        Thread t = new Thread();
        t.start();
        System.out.println(t.getState());
        //RUNNABLE
    }
    //新建
    @Test
    public void thread_NEW(){
        Thread t = new Thread();
        System.out.println(t.getState());
        //NEW
    }
}
