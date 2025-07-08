# AQS

AQS (AbstractQueuedSynchronizer) 是 Java 并发包中一个核心的同步器框架，位于 `java.util.concurrent.locks` 包下。它为构建锁和同步器提供了基础框架，许多重要的并发工具如 ReentrantLock、Semaphore、CountDownLatch 等都是基于 AQS 实现的。

> [AQS 节点状态详细分析](# AQS 节点状态详细分析)

## AQS 的核心思想

AQS 使用一个 volatile int 类型的成员变量 `state` 来表示同步状态，通过内置的 FIFO 队列来完成资源获取线程的排队工作。

# 主要特点

## 同步状态管理

**同步状态管理**：通过 `getState()`、`setState()` 和 `compareAndSetState()` 方法操作 state 变量

- 使用 `volatile int` 类型的变量表示
- 通过 CAS (Compare-And-Swap) 操作保证原子性
- 不同子类对 state 的解释不同：
  - 锁实现：表示锁的持有计数 (0=未锁定，1=锁定，>1=重入次数)
  - 信号量：表示可用许可数量
  - CountDownLatch：表示剩余需要倒数的事件数

## 等待队列

**等待队列**：采用 CLH (Craig, Landin, and Hagersten) 队列作为等待队列

- 一个虚拟的双向队列 (FIFO)
- 节点类型为 `Node`，包含：
  - 线程引用
  - 等待状态 (WAITING, CANCELLED 等)
  - 前驱和后继指针

> 具有以下特点：
>
> 1. **头节点 (head)**：是一个"虚拟节点"（或叫哑节点），不关联任何线程
> 2. **尾节点 (tail)**：指向队列中最后一个节点
> 3. **真实节点**：从第二个节点开始都是真实的等待线程
>
> ```
> head (虚拟节点) -> 真实节点1 -> 真实节点2 -> ... -> tail (最后一个真实节点)
> ```
>

### Node

#### waitStatus字段（节点状态）

**AbstractQueuedSynchronizer（AQS）中，节点（Node）的waitStatus 字段**
在 AbstractQueuedSynchronizer（AQS）中，等待队列的每个节点（Node）都有一个 waitStatus 字段，用于表示节点的状态。常见的 waitStatus 值及其含义如下：


| 状态值 | 常量名    | 含义                             | **触发场景**                                                 |
| ------ | --------- | -------------------------------- | ------------------------------------------------------------ |
| -3     | PROPAGATE | 共享模式下传播释放状态           |                                                              |
| -2     | CONDITION | 节点在条件队列中等待             | 当线程调用 `Condition.await()` 时，节点会从锁队列转移到条件队列，状态变为 -2 |
| -1     | SIGNAL    | 表示当前节点的后继节点需要被唤醒 | 当线程需要阻塞时会改为 -1（`SIGNAL`）                        |
| 0      | 0         | 初始状态（新创建的节点）         | 默认 0，初始化                                               |
| 1      | CANCELLED | 节点因超时或中断被取消           | 线程被中断或超时会变为 1（`CANCELLED`）                      |

**cancel状态的情况说明**

* 若某个等待线程中途中断，节点会变成CANCELLED状态：
* 比如C节点因中断从阻塞状态被唤醒，此时C节点的waitStatus被设置为CANCELLED(1)，表示放弃锁的争夺：

```
B(Head, waitStatus=0, 持锁) → C(waitStatus=1, 已取消)
```

* 当B释放锁时，发现后继节点C状态为CANCELLED，AQS会跳过C节点，继续寻找下一个有效节点（如果有）。
* acquireQueued中cancelAcquire会清理取消状态的节点

**出现 `waitStatus=-2` 的代码路径**

只有当使用 `Condition` 时才会触发：

```
ReentrantLock lock = new ReentrantLock();
Condition condition = lock.newCondition(); // 创建条件变量

// 线程1
lock.lock();
condition.await(); // 此时会创建 waitStatus=-2 的节点
lock.unlock();

// 线程2
lock.lock();
condition.signal(); // 将 -2 节点从条件队列转移回锁队列
lock.unlock();
```

**状态流转示意图**

``` mermaid
flowchart LR
    A[锁队列] -->|lock.lock| B[节点状态: 0/-1]
    B -->|condition.await| C[条件队列: -2]
    C -->|condition.signal| D[移回锁队列: -1]
    D -->|获取锁| E[状态重置为 0]
```

------

## 模板方法模式

**模板方法模式**：提供了可重写的方法，由子类实现具体的同步逻辑

# AQS 的核心方法

**需要子类实现的方法（protected）：**

- `tryAcquire(int arg)`：尝试获取独占锁
- `tryRelease(int arg)`：尝试释放独占锁
- `tryAcquireShared(int arg)`：尝试获取共享锁
- `tryReleaseShared(int arg)`：尝试释放共享锁
- `isHeldExclusively()`：当前线程是否独占资源

**重要的公开方法：**

- `acquire(int arg)`：获取独占锁
- `acquireInterruptibly(int arg)`：可中断地获取独占锁
- `tryAcquireNanos(int arg, long nanosTimeout)`：带超时的尝试获取独占锁
- `release(int arg)`：释放独占锁
- `acquireShared(int arg)`：获取共享锁
- `releaseShared(int arg)`：释放共享锁

# 工作流程

### **获取资源流程 (以独占模式为例)**

1. 线程调用 acquire(int arg)
2. 先尝试 tryAcquire(arg) (由子类实现)
3. 如果获取失败，将线程封装为 Node 加入队列
4. 在队列中自旋检查前驱节点是否为头节点
5. 如果是头节点则再次尝试获取
6. 获取成功后将自己设为头节点
7. 如果获取失败则根据条件挂起线程

### 释放资源流程

1. 线程调用 release(int arg)
2. 先尝试 tryRelease(arg) (由子类实现)
3. 如果释放成功，唤醒后继节点中的线程

### 关键设计点

1. **自旋与阻塞**：线程在获取资源失败后会先自旋尝试，减少上下文切换
2. **公平性控制**：通过队列实现公平锁，非公平锁可以直接尝试获取
3. **条件变量**：支持 Condition 条件等待机制

# AQS 的使用示例

以下是一个简单的 Mutex (互斥锁) 实现示例：

```
class Mutex extends AbstractQueuedSynchronizer {
    // 尝试获取锁
    @Override
    protected boolean tryAcquire(int arg) {
        return compareAndSetState(0, 1);
    }
    
    // 尝试释放锁
    @Override
    protected boolean tryRelease(int arg) {
        setState(0);
        return true;
    }
    
    // 是否是独占锁
    @Override
    protected boolean isHeldExclusively() {
        return getState() == 1;
    }
    
    // 提供便捷的加锁方法
    public void lock() {
        acquire(1);
    }
    
    // 提供便捷的解锁方法
    public void unlock() {
        release(1);
    }
}
```

# AQS 的应用

基于 AQS 实现的常见同步器：

1. **ReentrantLock**：可重入的独占锁
2. **ReentrantReadWriteLock**：读写锁
3. **Semaphore**：信号量
4. **CountDownLatch**：倒计时门闩
5. **FutureTask**：异步任务结果获取
6. **ThreadPoolExecutor.Worker**：线程池中的工作线程

# AQS 的优点

1. **灵活性**：可以轻松实现各种同步模式
2. **高效性**：内部使用 CAS 操作和队列管理，性能高效
3. **可扩展性**：通过继承可以创建自定义同步器
4. **可靠性**：经过充分测试和验证

# AQS设计思想

* AQS使用一个int成员变量来表示同步状态
* 使用Node实现FIFO队列，可以用于构建锁或者其他同步装置
* AQS资源共享方式：独占Exclusive（排它锁模式）和共享Share（共享锁模式）

# 原理分析

## 独占模式工作原理 (如 ReentrantLock)

### 获取锁流程 (acquire)

1. **tryAcquire**：子类尝试直接获取锁

   - 成功：直接返回
   - 失败：进入排队流程

2. **入队**：

   - 将当前线程包装为 Node 加入队列尾部
   - 通过 CAS 保证线程安全

3. **自旋检查**：

   ```
   for (;;) {
       if (前驱是头节点 && tryAcquire成功) {
           setHead(currentNode);
           return;
       }
       if (应该阻塞) {
           阻塞线程;
       }
   }
   ```

4. **阻塞**：

   - 使用 `LockSupport.park()` 阻塞线程
   - 被唤醒后继续尝试获取锁

### 释放锁流程 (release)

1. **tryRelease**：子类尝试释放锁
   - 失败：抛出异常
   - 成功：继续后续流程
2. **唤醒后继**：
   - 找到队列中第一个未取消的节点
   - 使用 `LockSupport.unpark()` 唤醒其线程

## 共享模式工作原理 (如 Semaphore)

### 获取共享资源 (acquireShared)

1. **tryAcquireShared**：尝试获取共享资源
   - 返回值 >= 0：获取成功
   - 返回值 < 0：获取失败
2. **入队与自旋**：
   - 类似独占模式，但可传播唤醒信号

### 释放共享资源 (releaseShared)

1. **tryReleaseShared**：尝试释放资源
2. **传播唤醒**：可能唤醒多个等待线程

## 关键设计要点

1. **自旋优化**：
   - 在阻塞前多次尝试获取锁
   - 减少线程切换开销
2. **取消机制**：
   - 通过节点状态标记取消的线程
   - 清理时跳过已取消的节点
3. **条件队列**：
   - 每个条件变量对应一个条件队列
   - 与主同步队列交互实现 await/signal







