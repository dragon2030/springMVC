package com.bigDragon.javase.concurrent.juc.cas;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
cas的aba问题
Java 8 中的 AtomicStampedReference 通过添加一个版本号(标记)来解决ABA问题。每次修改不仅比较引用，还比较版本号。
 */
public class AtomicStampedReferenceTest {
    private final static AtomicInteger num = new AtomicInteger(100);

    public static void main(String[] args) {
        /**
         * 存在aba问题
         * Thread-0 修改num之后的值：100
         * Thread-1 修改num之后的值：200
         */
        new AtomicStampedReferenceTest().casProblemABA();
        /**
         * 解决aba问题 使用了AtomicStampedReference
         */
        new AtomicStampedReferenceTest().casSolutionABA();
    }
    
    public void m1(){
        AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 0);

// 获取引用和版本号
        int[] stampHolder = new int[1];
        String currentRef = asr.get(stampHolder);
        int currentStamp = stampHolder[0];
    
        System.out.println("Current Value: " + currentRef + ", Stamp: " + currentStamp);
    }

    @Test
    public void casProblemABA(){
        new Thread(() -> {
            num.compareAndSet(100, 101);
            num.compareAndSet(101, 100);
            System.out.println(Thread.currentThread().getName() + " 11修改num之后的值：" + num.get());
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(3);
                num.compareAndSet(100, 200);
                System.out.println(Thread.currentThread().getName() + " 22修改num之后的值：" + num.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //Thread-0 11修改num之后的值：100
        //Thread-1 22修改num之后的值：200
    }
    /**
     initialRef - 初始引用值
        设置 AtomicStampedReference 初始维护的对象引用
     initialStamp - 初始版本标记(戳记)
        每次更新引用时都应该改变这个值,通常从0开始，但可以是任意整数,相当于为引用值添加了一个"版本号"
     */

    static Integer expectedReference100 = Integer.valueOf(100);
    static Integer newReference200 = Integer.valueOf(200);
    static Integer initialStamp1 = Integer.valueOf(1);
    private final static AtomicStampedReference<Integer> stamp = new AtomicStampedReference<>(expectedReference100, 1);

    @Test
    public void casSolutionABA() {
        new Thread(() -> {
            try {
                
                System.out.println(Thread.currentThread().getName() + " 第1次版本号:" + stamp.getStamp()+" reference:"+stamp.getReference());
                stamp.compareAndSet(expectedReference100, newReference200, initialStamp1, initialStamp1 + 1);
                System.out.println(Thread.currentThread().getName() + " 第2次版本号:" + stamp.getStamp()+" reference:"+stamp.getReference());
                stamp.compareAndSet(newReference200, expectedReference100, stamp.getStamp(), stamp.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + " 第3次版本号:" + stamp.getStamp()+" reference:"+stamp.getReference());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(3);
                System.out.println(Thread.currentThread().getName() + " 第1次版本号:" + stamp.getStamp());
                stamp.compareAndSet(100, 400, initialStamp1, initialStamp1 + 1);
                System.out.println(Thread.currentThread().getName() + " 获取到的值：" + stamp.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //Thread-0 第1次版本号:1 reference:100
        //Thread-0 第2次版本号:2 reference:200
        //Thread-0 第3次版本号:3 reference:100
        //Thread-1 第1次版本号:3
        
        //源码debug expectedStamp == current.stamp （1 == 3）false
    }

}
