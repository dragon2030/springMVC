package com.bigDragon.javase.concurrent.reentrantLock;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
重入锁 Reentrant
 一、定义：
 可重入指的是一段代码可以被同一线程多次安全地进入，而不会引发死锁或数据不一致问题。
 
 ReentrantLock 结合了 CAS、队列管理和线程阻塞/唤醒机制，CAS 不是 ReentrantLock 的唯一机制，CAS 提供基础原子操作

 */
public class ReentrantTest {
    public static void main (String[] args) {
        //synchronized 的可重入性示例
        new ReentrantTest().synchronizedReentrant();
        //ReentrantLock可重入性展示
        new ReentrantTest().ReentrantLockReentrant();
        //模拟乐观锁的+1操作
        new ReentrantTest().incrementOne();
    }
    //模拟乐观锁的+1操作
    private final ReentrantLock lock = new ReentrantLock();//非公平锁
    private final ReentrantLock lock2 = new ReentrantLock(true);//公平锁
    private int count = 0;
    public boolean incrementOne(){
        if (lock.tryLock()) {  // 尝试获取锁（非阻塞）
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;  // 获取锁失败时不阻塞
    }
    //ReentrantLock可重入性展示
    @Test
    public void ReentrantLockReentrant(){
        Thread thread1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 1: First lock acquired");
                nestedMethod();
                System.out.println("Thread 1: Nested method complete");
            } finally {
                lock.unlock();
            }
        });
    
        Thread thread2 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("Thread 2: Lock acquired");
            } finally {
                lock.unlock();
            }
        });
    
        thread1.start();
        thread2.start();
        /**
         Thread 1: First lock acquired
         Nested method: Lock acquired
         Thread 1: Nested method complete
         Thread 2: Lock acquired
         在两个线程中展示了锁的可重入性。
         在nestedMethod方法中，线程可以再次获取同一个锁，而不会造成死锁或阻塞，因为ReentrantLock允许同一个线程多次获取锁。
         在Thread 1执行可重入锁后，Thread 2正常获得锁
         */
    }
    private void nestedMethod() {
        lock.lock();
        try {
            System.out.println("Nested method: Lock acquired");
        } finally {
            lock.unlock();
        }
    }
    /**
     synchronized 的可重入性示例
     */
    @Test
    public void synchronizedReentrant() {
        new ReentrantTest().method1();
        //method1
        //method2
        // 不会发生死锁
    }
    public synchronized void method1() {
        System.out.println("method1");
        method2(); // 调用另一个同步方法
    }
    public synchronized void method2() {
        System.out.println("method2");
    }
    

}
