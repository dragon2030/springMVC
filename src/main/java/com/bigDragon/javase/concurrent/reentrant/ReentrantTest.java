package com.bigDragon.javase.concurrent.reentrant;

import org.junit.Test;

/**
重入锁 Reentrant
 一、定义：
 可重入指的是一段代码可以被同一线程多次安全地进入，而不会引发死锁或数据不一致问题。
 
 ReentrantLock 结合了 CAS、队列管理和线程阻塞/唤醒机制，CAS 不是 ReentrantLock 的唯一机制，CAS 提供基础原子操作
 

 */
public class ReentrantTest {
    
    
    /**
     synchronized 的可重入性示例
     */
    @Test
    public void m1() {
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
