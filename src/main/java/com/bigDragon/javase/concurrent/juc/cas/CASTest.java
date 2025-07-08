package com.bigDragon.javase.concurrent.juc.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: bigDragon
 * @create: 2025/4/8
 * @Description:

 */
public class CASTest {
    public static void main (String[] args) {
        //源码解析
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(1);
        //返回旧值并进行加一
        atomicInteger.getAndIncrement();
        //进行加一 并返回更新后的值
        atomicInteger.incrementAndGet();
        
        //AtomicInteger
        new CASTest().AtomicIntegerTest();
        //AtomicReference
        new CASTest().AtomicReferenceTest();
        /**
         cas问题一：CPU开销大
         原生 CAS + 自旋锁会一直占用 CPU，直到成功。Unsafe 不提供超时判断。
         在高并发场景下，优先使用 Java 标准库的并发工具ReentrantLock 解决cas长期占用cpu无时间限制的问题
         */
        new CASTest().problemCpu();
    }
    
    
    public void AtomicIntegerTest(){
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.getAndIncrement();
        /**
         //直接计算字段的内存地址：对象基地址 + valueOffset 。比通过引用访问更高效
         unsafe.getAndAddInt(this, valueOffset, 1)
     
         public final int getAndAddInt(Object var1, long var2, int var4) {
         int var5;
         do {
         //获取当前值
         var5 = this.getIntVolatile(var1, var2);
         // 当前对象 要修改的字段在对象内存中的偏移量 预期值 要修改的值
         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
     
         return var5;
         }
         **/
    }
    //AtomicReference
    public void AtomicReferenceTest(){
        AtomicReference<Integer> atomicRef = new AtomicReference<>(100);

        // 如果当前值是100，则原子性地更新为200
        boolean success = atomicRef.compareAndSet(100, 200);
        System.out.println("Update successful? " + success); // true
        System.out.println("Current value: " + atomicRef.get()); // 200

        // 再次尝试更新（会失败，因为当前值已经是200）
        success = atomicRef.compareAndSet(100, 300);
        System.out.println("Update successful? " + success); // false
        System.out.println("Current value: " + atomicRef.get()); // 200
    }
    
    public void problemCpu(){
        try {
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLock.tryLock(4, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
