package com.bigDragon.javase.concurrent.reentrantLock;

import org.apache.commons.collections.buffer.BoundedBuffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition
 生产者-消费者模型中的实战
    【简单的说就是用来替代wait notity】
 
 执行流程分析
 生产者线程：
 若队列满，调用 notFull.await() 释放锁并进入条件队列。
 被消费者通过 notFull.signal() 唤醒后，重新检查条件并插入数据。
 消费者线程：
 若队列空，调用 notEmpty.await() 释放锁并进入条件队列。
 被生产者通过 notEmpty.signal() 唤醒后，重新检查条件并取出数据。
 */
public class LockConditionTest {
    public static void main (String[] args) throws InterruptedException {
        LockConditionTest buffer = new LockConditionTest();
    
        // 生产者线程（生产10个数字）
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    buffer.put(i);
                    Thread.sleep(200); // 模拟生产耗时
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "生产者");
    
        // 消费者线程（消费10个数字）
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    buffer.take();
                    Thread.sleep(500); // 模拟消费耗时
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "消费者");
    
        producer.start();
        consumer.start();
    
        producer.join();
        consumer.join();
        System.out.println("运行结束");
    }
    // 有界缓冲区实现
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();  // 队列未满条件
    final Condition notEmpty = lock.newCondition(); // 队列非空条件
    
    final Object[] items = new Object[5]; // 容量为5的缓冲区
    int putPtr, takePtr, count;
    //【例子中count会有线程安全问题】
    // 生产数据
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                System.out.println(Thread.currentThread().getName() + " 发现队列满，等待...");
                notFull.await(); // 队列满时阻塞
            }
            items[putPtr] = x;
            if (++putPtr == items.length) putPtr = 0;
            count++;
            System.out.println(Thread.currentThread().getName() + " 生产: " + x + " | 队列大小: " + count);
            notEmpty.signal(); // 唤醒一个消费者
        } finally {
            lock.unlock();
        }
    }
    
    // 消费数据
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                System.out.println(Thread.currentThread().getName() + " 发现队列空，等待...");
                notEmpty.await(); // 队列空时阻塞
            }
            Object x = items[takePtr];
            items[takePtr] = null; // 显式清空引用
            if (++takePtr == items.length) takePtr = 0;
            count--;
            System.out.println(Thread.currentThread().getName() + " 消费: " + x + " | 队列大小: " + count);
            notFull.signal(); // 唤醒一个生产者
            return x;
        } finally {
            lock.unlock();
        }
    }
}
