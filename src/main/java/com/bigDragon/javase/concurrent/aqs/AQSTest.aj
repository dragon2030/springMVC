package com.bigDragon.javase.concurrent.aqs;

/**
 AQS：全名AbstractQuenedSynchronizer 抽象队列式同步器。
 
 一、AQS核心原理：
 通过一个volatile的int状态变量（state）和CLH队列（使用Node实现FIFO双向链表）实现线程的排队与唤醒，
 采用模板方法模式让子类定义具体的资源获取/释放规则，底层依赖CAS操作保证原子性和LockSupport实现线程阻塞/唤醒。
 
 三、AQS资源共享方式：
 独占Exclusive（排它锁模式）和共享Share（共享锁模式）
 
 四、状态转换总结
 节点入队(waitStatus=0) → 
 前驱节点设为SIGNAL(-1) → 
 阻塞等待 → 
 前驱unlock()唤醒 → 
 被唤醒节点tryAcquire()成功 → 
 节点变为头节点(waitStatus=0) → 
 持锁运行 → 
 释放锁(unlock())，
 再唤醒下个节点。
 
 博客：https://blog.csdn.net/JavaShark/article/details/125300628?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522f74f63a50257efa185642e03b6a3fa75%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=f74f63a50257efa185642e03b6a3fa75&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-125300628-null-null.142
 源码解析：ReentrantLock.md
 */
public aspect AQSTest {
}
