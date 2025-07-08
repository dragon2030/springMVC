package com.bigDragon.javase.concurrent.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: bigDragon
 * @create: 2025/7/4
 * @Description:
 */
public class ReentrantLockTest {
    public static void main (String[] args) {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        reentrantLock.unlock();
    
        try {
            reentrantLock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }
}
