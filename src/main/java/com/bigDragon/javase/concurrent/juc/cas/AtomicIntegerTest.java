package com.bigDragon.javase.concurrent.juc.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: bigDragon
 * @create: 2023/3/27
 * @Description:
 *  cas解决线程安全问题
 */
public class AtomicIntegerTest {
    private static AtomicInteger atomicInteger = new AtomicInteger();

    public static void main(String[] args) {

        for(int i=0;i<100;i++){
            new Thread(() -> {
                AtomicIntegerTest.atomicInteger.incrementAndGet();
                System.out.println("count = " + atomicInteger.get());
            }).start();

        }
    }
}
