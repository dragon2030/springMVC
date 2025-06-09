# 一、AQS核心原理：
* 通过一个volatile的int状态变量（state）和CLH队列（双向链表）实现线程的排队与唤醒
* 线程通过CAS去改变状态，成功则获取锁成功，失败则进入等待队列，等待被唤醒。
* 采用模板方法模式让子类定义具体的资源获取/释放规则，底层依赖CAS操作保证原子性和LockSupport实现线程阻塞/唤醒。
> CLH（Craig，Landin，and Hagersten）队列是一个虚拟的双向队列，虚拟的双向队列即不存在队列实例，仅存在节点之间的关联关系。AQS将每一条请求共享资源的线程封装成一个CLH锁队列的一个结点（Node），来实现锁的分配。
# 二、AQS设计思想
* AQS使用一个int成员变量来表示同步状态
* 使用Node实现FIFO队列，可以用于构建锁或者其他同步装置
* AQS资源共享方式：独占Exclusive（排它锁模式）和共享Share（共享锁模式）

# ReentrantLock
* ReentrantLock（可重入独占式锁）：state初始化为0，表示未锁定状态，A线程lock()时，会调用tryAcquire()独占锁并将state+1.之后其他线程再想tryAcquire的时候就会失败，直到A线程unlock（）到state=0为止，其他线程才有机会获取该锁。A释放锁之前，自己也是可以重复获取此锁（state累加），这就是可重入的概念。
>注意：获取多少次锁就要释放多少次锁，保证state是能回到零态的。

# CountDownLatch
* CountDownLatch任务分N个子线程去执行，state就初始化 为N，N个线程并行执行，每个线程执行完之后countDown（）一次，state就会CAS减一。当N子线程全部执行完毕，state=0，会unpark()主调用线程，主调用线程就会从await()函数返回，继续之后的动作。

# 博客
* https://way2j.com/a/546
> 面试题八股文 Java-AQS的原理
