package com.bigDragon.juc;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author: bigDragon
 * @create: 2023/3/27
 * @Description:
 *  cas的aba问题
 * https://blog.csdn.net/MonkeyBrothers/article/details/113102271
 * AtomicStampedReference源码分析
 */
public class AtomicStampedReferenceTest {
    private final static AtomicInteger num = new AtomicInteger(100);

    public static void main(String[] args) {
        AtomicStampedReferenceTest test = new AtomicStampedReferenceTest();
        /**
         * 存在aba问题
         * Thread-0 修改num之后的值：100
         * Thread-1 修改num之后的值：200
         */
//        test.casProblemABA();
        /**
         * 解决aba问题
         */
        test.casSolutionABA();
    }

    public void casProblemABA(){
        new Thread(() -> {
            num.compareAndSet(100, 101);
            num.compareAndSet(101, 100);
            System.out.println(Thread.currentThread().getName() + " 修改num之后的值：" + num.get());
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                num.compareAndSet(100, 200);
                System.out.println(Thread.currentThread().getName() + " 修改num之后的值：" + num.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private final static AtomicStampedReference<Integer> stamp = new AtomicStampedReference<>(100, 1);

    public void casSolutionABA() {
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 第1次版本号:" + stamp.getStamp());
            stamp.compareAndSet(100, 200, stamp.getStamp(), stamp.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " 第2次版本号:" + stamp.getStamp());
            stamp.compareAndSet(200, 100, stamp.getStamp(), stamp.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + " 第2次版本号:" + stamp.getStamp());
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + " 第1次版本号:" + stamp.getStamp());
                stamp.compareAndSet(100, 400, stamp.getStamp(), stamp.getStamp() + 1);
                System.out.println(Thread.currentThread().getName() + " 获取到的值：" + stamp.getReference());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
