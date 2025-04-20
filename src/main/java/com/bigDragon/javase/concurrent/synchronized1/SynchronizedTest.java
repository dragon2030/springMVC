package com.bigDragon.javase.concurrent.synchronized1;

import org.openjdk.jol.info.ClassLayout;

/**
 博客
 https://blog.csdn.net/Gaomengsuanjia_/article/details/145742347?ops_request_misc=%257B%2522request%255Fid%2522%253A%252288b61dfeb4eabbb6dca39951c0601843%2522%252C%2522scm%2522%253A%252220140713.130102334.pc%255Fall.%2522%257D&request_id=88b61dfeb4eabbb6dca39951c0601843&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~first_rank_ecpm_v1~rank_v31_ecpm-5-145742347-null-null.142
 【看的半懂不懂 等看书的时候再取搞懂】
 
 Java synchronized底层原理深度解析
 一、对象头
 在Java中，所有的对象都有一个对象头（Object Header），它存储了与对象管理和同步相关的信息。对象头主要由两部分组成：Mark Word和Class Pointer。
 
 二、偏向锁 
 它假设在大多数情况下，某个对象的锁只会被同一个线程获取
 
 三、轻量锁
 乐观锁：通过CAS操作竞争锁。
 
 四、重量锁
 重量级锁是悲观锁，通过monitor实现，每一个锁对象都有且仅有一个自己的monitor对象。
 
 
 四、锁膨胀
 */
public class SynchronizedTest {

    
    //使用Java对象内存布局工具(JOL)查看对象头的Mark Word，分析锁状态
    public static void main(String[] args) {
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
        /**
         OFF  SZ   TYPE DESCRIPTION               VALUE
         0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
         8   4        (object header: class)    0xf80001e5
         12   4        (object alignment gap)    
         Instance size: 16 bytes
         Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 
         java.lang.Object object internals:
         OFF  SZ   TYPE DESCRIPTION               VALUE
         0   8        (object header: mark)     0x00000073ca0ff538 (thin lock: 0x00000073ca0ff538)
         8   4        (object header: class)    0xf80001e5
         12   4        (object alignment gap)    
         Instance size: 16 bytes
         Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
         */
    }

    
}
