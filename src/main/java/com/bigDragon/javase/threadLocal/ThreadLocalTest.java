package com.bigDragon.javase.threadLocal;

import org.junit.Test;

/**
 一、定义
 ThreadLocal 主要是做数据隔离，它是线程的局部变量，是每一个线程所单独持有的，其他线程不能对其进行访问，相对隔离的。
 二、解决了什么问题
 解决多线程的并发访问。ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。
 三、ThreadLocal原理
 每个Thread对象都有一个ThreadLocalMap，当创建一个ThreadLocal的时候，就会将该ThreadLocal对象添加到该Map中，其中键就是ThreadLocal，值可以是任意类型。
 Entry extends WeakReference<ThreadLocal<?>
 哈希冲突解决（开放寻址）
 四、内存泄漏问题
 
 */
public class ThreadLocalTest {
    public static void main (String[] args) {
        //threadLocal使用的简单样例
        new ThreadLocalTest().threadLocalDemo();
    }
    // java 8.0 创建一个ThreadLocal变量
//    private static final ThreadLocal<Integer> threadLocalCount = ThreadLocal.withInitial(() -> 0);
    // 创建一个ThreadLocal变量
    private static ThreadLocal<Integer> threadLocalCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0; // 初始值为0
        }
    };
    //threadLocal使用的简单样例
    @Test
    public void threadLocalDemo(){
        // 创建并启动5个线程
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                // 获取当前线程的局部变量值
                int count = threadLocalCount.get();
                // 修改值
                threadLocalCount.set(count + 1);
                // 打印结果
                System.out.println(Thread.currentThread().getName()
                        + ": count = " + threadLocalCount.get());
                // 使用完后清除，防止内存泄漏
                threadLocalCount.remove();
            }, "Thread-" + i).start();
        }
        //Thread-1: count = 1
        //Thread-0: count = 1
        //Thread-3: count = 1
        //Thread-2: count = 1
        //Thread-4: count = 1
    }
}
