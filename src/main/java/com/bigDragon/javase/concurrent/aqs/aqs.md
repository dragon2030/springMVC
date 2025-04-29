# 一、AQS核心原理：
* 通过一个volatile的int状态变量（state）和CLH队列（双向链表）实现线程的排队与唤醒，采用模板方法模式让子类定义具体的资源获取/释放规则，底层依赖CAS操作保证原子性和LockSupport实现线程阻塞/唤醒。
 
# 二、AQS设计思想
* AQS使用一个int成员变量来表示同步状态
* 使用Node实现FIFO队列，可以用于构建锁或者其他同步装置
* AQS资源共享方式：独占Exclusive（排它锁模式）和共享Share（共享锁模式）
