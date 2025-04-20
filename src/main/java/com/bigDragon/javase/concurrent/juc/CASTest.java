package com.bigDragon.javase.concurrent.juc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: bigDragon
 * @create: 2025/4/8
 * @Description:
 * 1、基本概念：
 *      CAS是原子指令，一种基于锁的操作，而且是乐观锁，又称无锁机制。CAS操作包含三个基本操作数：内存位置、期望值和新值。
 * 2、CAS基本原理
 *      在执行CAS操作时，计算机会检查内存位置当前是否存放着期望值，如果是，则将内存位置的值更新为新值；若不是，则不做任何修改，保持原有值不变，
 *      并返回当前内存位置的实际值。
 *      
 *      CAS操作通过一条CPU的原子指令，保证了比较和更新的原子性。
 *      cmpxchg（Compare and Exchange）是 x86/x64 架构 中的一条汇编指令，用于实现原子性的 比较并交换（Compare-and-Swap, CAS） 操作。
 *      它是 Java 中 Unsafe 类 CAS 操作（如 compareAndSwapInt）的底层硬件支持。
 * 3、Java中的CAS实现
 *      在Java中，CAS机制被封装在jdk.internal.misc.Unsafe类中，尽管这个类并不建议在普通应用程序中直接使用，但它是构建更高层次并发工具的基础，
 *          例如java.util.concurrent.atomic包下的原子类如AtomicInteger、AtomicLong等。这些原子类通过JNI调用底层硬件提供的CAS指令，
 *          从而在Java层面上实现了无锁并发操作。
 *      Java的标准库中，特别是jdk.internal.misc.Unsafe类提供了一系列compareAndSwapXXX方法，这些方法底层确实是通过C++编写的内联汇编来调用对应CPU架构的cmpxchg指令，
 *          从而实现原子性的比较和交换操作。

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
        
        //cas核心源码
        new CASTest().coreCode();
        /**
         cas问题一：CPU开销大
         原生 CAS + 自旋锁会一直占用 CPU，直到成功。Unsafe 不提供超时判断。
         在高并发场景下，优先使用 Java 标准库的并发工具ReentrantLock 解决cas长期占用cpu无时间限制的问题
         */
        new CASTest().problemCpu();
    }
    
    
    public void coreCode(){
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
    
    public void problemCpu(){
        try {
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLock.tryLock(4, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
