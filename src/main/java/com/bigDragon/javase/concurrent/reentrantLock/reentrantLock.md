# ReentrantLock 

Reentrant 美 /rɪˈentrənt/ 可重入
【共享模式的aqs有点像同步模型，没看看吐了】

# 简介

`ReentrantLock` 是 Java 并发包 (`java.util.concurrent.locks`) 中的一个可重入互斥锁实现，它提供了比内置 `synchronized` 关键字更灵活的锁操作。

ReentrantLock 是 Lock 接口的一个具体实现类

## 基本特性

1. **可重入性**：同一个线程可以多次获取同一把锁而不会导致死锁

2. **可中断**：支持在等待锁的过程中响应中断

3. **公平性选择**：可以创建公平锁或非公平锁

4. **条件变量支持**：可以创建多个 `Condition` 对象

5. 可见性：**可以保证可见性**

   > [ReentrantLock可见性保证](# ReentrantLock可见性保证)
   >
   > 线程在释放锁时会强制将工作内存中的修改刷新到主内存；获取锁时会强制从主内存重新加载变量。
   >
   > 可见性保障
   >
   > 1. **happens-before 关系**：ReentrantLock 的实现遵循 Java 内存模型的规范，确保 unlock 操作 happens-before 后续的 lock 操作。
   > 2. **内存屏障**：在底层实现中，ReentrantLock 使用了 volatile 变量和 CAS 操作，这些都会插入内存屏障，保证变量的修改对所有线程可见。

6. 有序性

   > ReentrantLock 提供有限的有序性保证：
   >
   > 1. 临界区内的有序性：在锁保护的临界区内，代码的执行顺序不会被重排序到锁外（但临界区内的指令仍可能被重排序，只要不影响单线程语义）。
   > 2. 跨线程的有序性：一个线程在释放锁前的所有操作，对另一个获取该锁的线程都是可见且有序的。

7. **原子性**：`lock()` 和 `unlock()` 之间的操作是互斥执行的

## 核心方法

### 获取锁

- `lock()` - 获取锁，如果锁不可用则阻塞

- `lockInterruptibly()` - 可中断地获取锁

  > [ReentrantLock 的 lockInterruptibly() 方法详解](# ReentrantLock 的 lockInterruptibly() 方法详解)

- `tryLock()` - 尝试非阻塞地获取锁

  > ```java
  > //尝试获取锁-示例代码
  > if (lock.tryLock()) {
  >    try {
  >        // 获取锁成功
  >    } finally {
  >        lock.unlock();
  >    }
  > } else {
  >    // 获取锁失败
  > }
  > ```

- `tryLock(long timeout, TimeUnit unit)` - 带超时的尝试获取锁

### 释放锁

- `unlock()` - 释放锁

# 基本用法

```
public class Counter {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();  // 获取锁
        try {
        // 临界区代码
            count++;
        } finally {
            lock.unlock();  // 必须在finally块中释放锁
        }
    }

    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

## 公平锁 vs 非公平锁

`ReentrantLock` 有两种模式：

1. **非公平锁（默认）**：
   - 新请求锁的线程可能插队获取锁
   - 吞吐量更高
   - 创建方式：`new ReentrantLock()`
2. **公平锁**：
   - 按照请求顺序获取锁
   - 避免线程饥饿
   - 创建方式：`new ReentrantLock(true)`

# Condition 条件变量

`Condition` 是 Java 并发编程中一个重要的接口，它提供了比传统的 `Object` 监视器方法（`wait()`, `notify()`, `notifyAll()`）更灵活、更强大的线程间通信机制。

## 1. Condition 基本概念

`Condition` 接口是 `java.util.concurrent.locks` 包的一部分，通常与 `Lock` 配合使用。一个 `Lock` 可以创建多个 `Condition` 对象，允许线程在不同的条件下等待。

主要方法

- `await()` - 主要等待方法。使当前线程等待，直到被通知或中断
- `awaitUninterruptibly()` - 使当前线程等待，不响应中断
- `awaitNanos(long nanosTimeout)` - 使当前线程等待，直到被通知、中断或超时
- `awaitUntil(Date deadline)` - 使当前线程等待，直到被通知、中断或到达指定时间
- `signal()` - 主要唤醒方法。唤醒一个等待线程
- `signalAll()` - 唤醒所有等待线程

## 2. 基本使用示例

```
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean flag = false;

    public void await() throws InterruptedException {
        lock.lock();
        try {
            while (!flag) {
                System.out.println("线程等待中...");
                condition.await();
            }
            System.out.println("线程被唤醒，继续执行");
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        lock.lock();
        try {
            flag = true;
            condition.signal();
            System.out.println("发送唤醒信号");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo demo = new ConditionDemo();
        
        Thread t1 = new Thread(() -> {
            try {
                demo.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        t1.start();
        
        Thread.sleep(2000);
        demo.signal();
        
        t1.join();
    }
}
```

> **t1.join();**
>
> [关于 ConditionDemo 示例中 `t1.join()` 的详细解释](# 关于 ConditionDemo 示例中 `t1.join()` 的详细解释)

## 3. 多个 Condition 的使用

一个 `Lock` 可以创建多个 `Condition`，这在实现复杂同步逻辑时非常有用：

```
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 有界缓冲区实现，使用两个Condition分别管理"非满"和"非空"状态
 */
public class BoundedBuffer {
    // 可重入锁
    private final Lock lock = new ReentrantLock();
    
    // 两个条件变量：
    // notFull - 当缓冲区不满时满足条件
    // notEmpty - 当缓冲区不空时满足条件
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    // 缓冲区存储数组
    private final Object[] items = new Object[100];
    // 写入指针（下一个要写入的位置）
    private int putptr = 0;
    // 读取指针（下一个要读取的位置）
    private int takeptr = 0;
    // 当前缓冲区中的元素数量
    private int count = 0;
    
    /**
     * 向缓冲区放入元素
     * @param x 要放入的元素
     */
    public void put(Object x) throws InterruptedException {
        lock.lock();  // 获取锁
        try {
            // 当缓冲区已满时等待
            while (count == items.length) {
                System.out.println("缓冲区满，生产者等待...");
                notFull.await();  // 在notFull条件上等待
            }
            
            // 放入元素
            items[putptr] = x;
            // 移动写入指针，如果到达末尾则循环回到开头
            if (++putptr == items.length) putptr = 0;
            count++;  // 元素计数增加
            
            System.out.println("生产元素: " + x + ", 当前数量: " + count);
            
            // 唤醒可能在notEmpty上等待的消费者
            notEmpty.signal();
        } finally {
            lock.unlock();  // 释放锁
        }
    }
    
    /**
     * 从缓冲区取出元素
     * @return 取出的元素
     */
    public Object take() throws InterruptedException {
        lock.lock();  // 获取锁
        try {
            // 当缓冲区为空时等待
            while (count == 0) {
                System.out.println("缓冲区空，消费者等待...");
                notEmpty.await();  // 在notEmpty条件上等待
            }
            
            // 取出元素
            Object x = items[takeptr];
            // 移动读取指针，如果到达末尾则循环回到开头
            if (++takeptr == items.length) takeptr = 0;
            count--;  // 元素计数减少
            
            System.out.println("消费元素: " + x + ", 当前数量: " + count);
            
            // 唤醒可能在notFull上等待的生产者
            notFull.signal();
            return x;
        } finally {
            lock.unlock();  // 释放锁
        }
    }
}
```

**工作原理文字说明**

1. **数据结构**

- **环形缓冲区**：使用数组实现，通过 `putptr` 和 `takeptr` 指针循环移动
- **两个条件变量**：
  - `notFull`：当缓冲区不满时满足，生产者线程可以继续放入数据
  - `notEmpty`：当缓冲区不空时满足，消费者线程可以继续取出数据

2. **生产者逻辑 (`put` 方法)**

1. 获取锁
2. 检查缓冲区是否已满：
   - 如果满了，在 `notFull` 条件上等待
3. 放入数据到 `putptr` 位置
4. 更新 `putptr` 和 `count`
5. 唤醒在 `notEmpty` 上等待的消费者
6. 释放锁

3. **消费者逻辑 (`take` 方法)**

1. 获取锁
2. 检查缓冲区是否为空：
   - 如果为空，在 `notEmpty` 条件上等待
3. 从 `takeptr` 位置取出数据
4. 更新 `takeptr` 和 `count`
5. 唤醒在 `notFull` 上等待的生产者
6. 释放锁

4. **关键点**

- 使用两个独立的 `Condition` 分别管理生产者和消费者的等待队列

- `while` 循环检查条件，防止虚假唤醒

- 指针到达数组末尾时循环回到开头，实现环形缓冲区

  > 不会存在覆盖数据，队列元素满时，会阻塞知道非满条件触发

- 每次操作后只唤醒对方线程（生产者唤醒消费者，消费者唤醒生产者）

## 4. Condition 与 Object 监视器方法的区别

| 特性             | Condition                       | Object 监视器方法         |
| :--------------- | :------------------------------ | :------------------------ |
| 关联             | 与 Lock 关联                    | 与 synchronized 关联      |
| 多个等待队列     | 支持 (一个 Lock 多个 Condition) | 不支持 (只有一个等待队列) |
| 超时等待         | 支持                            | 不支持                    |
| 不响应中断的等待 | 支持 (awaitUninterruptibly)     | 不支持                    |
| 公平性           | 取决于 Lock 的实现              | 非公平                    |

## 5. 注意事项

1. **始终在 lock() 和 unlock() 之间使用 Condition**：否则会抛出 `IllegalMonitorStateException`
2. **使用 while 循环检查条件**：避免虚假唤醒(spurious wakeup)
3. **注意锁的释放**：确保在 finally 块中释放锁
4. **避免信号丢失**：在调用 signal() 前确保条件已经满足
5. **性能考虑**：在高度竞争的环境下，Condition 可能比内置锁更高效

## 6. 实际应用场景

1. 生产者-消费者问题
2. 线程池任务调度
3. 有限状态机
4. 阻塞队列实现
5. 读写锁实现

通过合理使用 Condition，可以构建更灵活、更高效的并发程序，解决复杂的线程同步问题。



# ReentrantLock与 synchronized 的比较

| 特性       | ReentrantLock               | synchronized       |
| :--------- | :-------------------------- | :----------------- |
| 实现方式   | Java 代码实现               | JVM 内置实现       |
| 锁获取方式 | 显式调用 lock()/unlock()    | 隐式获取/释放      |
| 可中断性   | 支持                        | 不支持             |
| 公平性     | 可配置                      | 非公平             |
| 条件变量   | 支持多个 Condition          | 只有一个等待队列   |
| 性能       | Java 5/6 更好，Java 6+ 相当 | Java 6+ 优化后相当 |
| 代码灵活性 | 更高                        | 较低               |

# ReentrantLock最佳实践

1. 总是在 `try-finally` 块中释放锁，确保锁被释放
2. 考虑使用 `tryLock()` 避免死锁
3. 公平锁会降低吞吐量，除非必要否则使用非公平锁
4. 优先考虑 `synchronized`，只有在需要高级功能时才使用 `ReentrantLock`

`ReentrantLock` 提供了比 `synchronized` 更灵活的锁操作，但同时也需要开发者更小心地管理锁的获取和释放。





# ReentrantLock加锁解锁

ReentrantLock 是 Java 并发包中基于 AQS (AbstractQueuedSynchronizer) 实现的可重入互斥锁。

**主体流程**

* 文字描述：
  * 如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并将共享资源设置为锁定状态。
  * 如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制（AQS）
* 伪代码：ReentrantLock（可重入独占式锁）：state初始化为0，表示未锁定状态，A线程lock()时，会调用tryAcquire()独占锁并将state+1.之后其他线程再想tryAcquire的时候就会失败，直到A线程unlock（）到state=0为止，其他线程才有机会获取该锁。A释放锁之前，自己也是可以重复获取此锁（state累加），这就是可重入的概念。
>注意：获取多少次锁就要释放多少次锁，保证state是能回到零态的。
## 一、核心概念

1. **可重入性**：同一个线程可以多次获取同一把锁
2. **公平性选择**：支持公平锁和非公平锁两种模式
3. **条件变量**：通过 Condition 对象支持更细粒度的线程通信

## 二、主要组件

1. **Sync 内部类**：继承自 AbstractQueuedSynchronizer (AQS)

   - NonfairSync：非公平锁实现
   - FairSync：公平锁实现

   >Sync是AQS的子类
   >
   >```java
   >abstract static class Sync extends AbstractQueuedSynchronizer
   >```

2. **AQS 队列**：维护等待锁的线程队列

# 加锁-lock

### 1. 构造方法

```
public ReentrantLock() {
    sync = new NonfairSync(); // 默认非公平锁
}

public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}
```

### 加锁方法调用

```java
ReentrantLock reentrantLock = new ReentrantLock();
reentrantLock.lock();
// -----ReentrantLock源码-----
public void lock() {
    sync.lock();
}
```

* Sync 类有两个子类，分别是FairSync和NonfairSync

  > ```
  >     // 非公平锁实现
  >     static final class NonfairSync extends Sync {
  >         // 实现...
  >     }
  >     
  >     // 公平锁实现
  >     static final class FairSync extends Sync {
  >         // 实现...
  >     }
  > ```

* ReentrantLock创建时会初始化内部类选择公平/非公平



### 3. 非公平锁实现 (NonfairSync)

#### 源码结构

```java
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**
         * Performs lock.  Try immediate barge, backing up to normal
         * acquire on failure.
         */
        final void lock() {
            ...
        }

        protected final boolean tryAcquire(int acquires) {
            ...
        }
    }
```

#### NonfairSync.lock()

调用链路：ReentrantLock.lock()->NonfairSync.lock()

	final void lock() {
	    // 第一步直接尝试CAS获取锁（插队行为）
	    if (compareAndSetState(0, 1))
	        setExclusiveOwnerThread(Thread.currentThread()); // 设置当前线程为锁的持有者
	    else
	        acquire(1); // 调用AQS的acquire方法
	}

#### NonfairSync.tryAcquire()

调用链路：ReentrantLock.lock()->Sync.lock()->acquire()->NonfairSync.tryAcquire()

 ```  java
 //尝试直接获取锁(在非公平锁执行中是可插队的体现)
 protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
 }
 ```

#### Sync.nonfairTryAcquire()

> [ReentrantLock 非公平锁的两次 CAS 操作分析](# ReentrantLock 非公平锁的两次 CAS 操作分析)

调用链路：ReentrantLock.lock()->Sync.lock()->AbstractQueuedSynchronizer.acquire()->NonfairSync.tryAcquire()->Sync.nonfairTryAcquire()

主要作用：尝试直接cas获取锁(在非公平锁执行中是可插队的体现)

```
//注意：nonfairTryAcquire调用的是Sync父类的方法
abstract static class Sync extends AbstractQueuedSynchronizer {

    // Sync中的非公平获取-尝试直接获取锁
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState(); // 获取当前状态
        if (c == 0) { // 如果当前状态为0，表示锁是空闲的
            if (
            //!hasQueuedPredecessors() && //在公平锁中存在，是公平性核心，用于检查是否有排队线程
            compareAndSetState(0, arg) // 再次尝试CAS获取资源（非公平性体现）
            ) { 
                setExclusiveOwnerThread(current); // 设置当前线程为锁的持有者
                return true; // 获取锁成功
            }
        }
        else if (current == getExclusiveOwnerThread()) { // 如果当前线程已经是锁的持有者，允许重入
            // 重入逻辑
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc); // 更新状态
            return true; // 获取锁成功
        }
        return false; // 获取锁失败
    }
}
```

#### 

### 4. 公平锁实现 (FairSync)

#### 源码结构

```java
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
            ...
        }

        /**
         * Fair version of tryAcquire.  Don't grant access unless
         * recursive call or no waiters or is first.
         */
        protected final boolean tryAcquire(int acquires) {
            ...
        }
    }
```

#### FairSync.lock()

调用链路：ReentrantLock.lock()->FairSync.lock()

    final void lock() {
        acquire(1); // 直接进入队列排队
    }



#### FairSync.tryAcquire()

`FairSync.tryAcquire()` **在公平锁模式下，通过严格的排队检查、可重入支持和CAS操作，确保锁的获取既符合线程公平性，又能高效处理重入请求**。与非公平锁（`NonfairSync`）相比，它避免了线程插队现象，但可能带来更高的上下文切换开销。

调用链路：ReentrantLock.lock()->FairSync.lock()->(aqs)acquire()->FairSync.tryAcquire()

主要作用：

* **遵循公平性原则**

  * 在锁未被占用时，会先检查同步队列中是否有其他线程在排队（`hasQueuedPredecessors()`）。

  - **只有确认没有更早的等待线程时**，当前线程才会尝试获取锁，确保严格的先到先得（FIFO）顺序。

* **CAS原子操作竞争锁**
  - 当锁未被占用且无排队线程时，通过 `compareAndSetState(0, 1)` 原子性地将锁状态从 `0`（未占用）改为 `1`（占用），成功则标记当前线程为锁持有者。
* 处理可重入逻辑
  - 如果当前线程已经是锁的持有者（重入场景），则通过增加 `state` 计数（CAS操作）直接成功获取锁，无需排队。

```

protected final boolean tryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
     // 获取当前状态
    int c = getState();
    if (c == 0) {// 如果当前状态为0，表示锁是空闲的
    // 关键区别：检查是否有前驱节点
        if (!hasQueuedPredecessors() //在公平锁中存在，是公平性核心，用于检查是否有排队线程
            && compareAndSetState(0, acquires)) // 尝试cas获取锁
        {
            setExclusiveOwnerThread(current); // 设置当前线程为锁的持有者
            return true; // 获取锁成功
        }
    }
    else if (current == getExclusiveOwnerThread()) { // 如果当前线程已经是锁的持有者，允许重入
    	// 重入逻辑与非公平锁相同
        int nextc = c + arg;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc); // 更新状态
    	return true; // 获取锁成功
    }
    return false; // 获取锁失败
}
```

#### AbstractQueuedSynchronizer.hasQueuedPredecessors()

> [`hasQueuedPredecessors()` 方法深度解析](# `hasQueuedPredecessors()` 方法深度解析)

`hasQueuedPredecessors()` 是 ReentrantLock 公平模式下的核心方法，用于判断当前线程获取锁时是否需要排队。

调用链路：ReentrantLock.lock()->FairSync.lock()->AbstractQueuedSynchronizer.acquire()->FairSync.tryAcquire()->AbstractQueuedSynchronizer.hasQueuedPredecessors()

    // hasQueuedPredecessors() 是 ReentrantLock 公平模式下的核心方法，用于判断当前线程获取锁时是否需要排队。
    public final boolean hasQueuedPredecessors() {
        Node t = tail; // 尾节点
        Node h = head; // 头节点
        Node s;
        return h != t && // 队列不为空
            ((s = h.next) == null || s.thread != Thread.currentThread());
    }

### 公平锁和非公平锁源码主要区别

```
final void lock() {
    acquire(1); // 公平锁 直接调用 AQS 的 acquire 方法,不会尝试CAS 操作直接获取锁
}

protected final boolean tryAcquire(int acquires) {
    // 检查队列中是否有等待线程
    if (hasQueuedPredecessors())
        return false;
    // 其余与非公平锁相同
}
```

1. 公平锁直接调用 acquire 方法
2. 公平锁tryAcquire 会先检查队列中是否有等待线程，只有队列为空或当前线程是队首线程时才会尝试获取锁

### 5. 释放锁实现

```
// Sync中的实现
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    
    boolean free = false;
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```

## AQS-lock 交互细节

> [AQS](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\reentrantLock\AQS.md)

### acquire 流程

`acquire` 方法是 AQS 中用于获取资源的模板方法，它定义了获取资源的流程框架，具体的资源获取逻辑由子类实现。其核心逻辑如下：

1. 先尝试获取资源（tryAcquire）
2. 如果获取失败，将当前线程加入等待队列
3. 在队列中自旋或阻塞等待，直到获取到资源

调用链路：ReentrantLock.lock()->Sync.lock()->acquire()

```
public final void acquire(int arg) {
    if (!tryAcquire(arg) && // 子类实现的尝试获取
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```

1. tryAcquire() - 尝试直接获取锁(由子类实现)

   * 参数 表示请求的许可数量（对于独占锁通常是1）

   * 返回值：true 表示获取成功，false 表示失败

   * 作用：它会检查锁状态，如果是无锁状态则尝试 CAS 获取锁。如果是重入情况（当前线程已持有锁），则增加重入计数

2. addWaiter(Node.EXCLUSIVE) - 创建节点并加入队列

   * 作用：将当前线程包装为独占模式节点加入等待队列

   * 参数：Node.EXCLUSIVE 表示独占模式

   * 过程：
     * 创建新节点（包含当前线程）
     * 快速尝试将节点加入队尾（如果队列已存在）
     * 如果队列为空或快速尝试失败，则调用完整入队方法

3. acquireQueued(node, arg) - 在队列中等待获取锁

   * 作用：让已经在队列中的节点循环尝试获取锁

   * 参数：node：刚加入队列的节点 1：请求的许可数量

   * 内部逻辑：
     * 进入循环不断尝试获取锁
       * 每次失败后检查是否需要挂起线程（通过前驱节点的状态）
       * 如果被挂起后又被唤醒，继续尝试获取锁
       * 如果在等待过程中被中断，返回 true，否则返回 false


4. selfInterrupt() - 如果在等待过程中被中断，重新设置中断标志

   * 源码

     > static void selfInterrupt() {
     >   	Thread.currentThread().interrupt();
     > }

#### tryAcquire 方法

调用链路：ReentrantLock.lock()->Sync.lock()->acquire()->tryAcquire 

```
protected boolean tryAcquire(int arg) {
    throw new UnsupportedOperationException();
}
```

这是一个需要子类实现的钩子方法，AQS 本身不提供具体实现。子类需要根据具体的同步需求实现这个方法。

#### addWaiter 方法

创建节点并加入队列。将当前线程包装成一个 Node 节点并添加到队列尾部：

1. 创建包含当前线程的节点
2. 先快速尝试直接添加到队尾（如果队列已存在）
3. 如果快速尝试失败，调用 `enq` 方法完成入队

mode：节点模式，两种取值：

* Node.EXCLUSIVE：独占模式（用于ReentrantLock）
* Node.SHARED：共享模式（用于Semaphore/CountDownLatch等）

调用链路：ReentrantLock.lock()->Sync.lock()->acquire()->addWaiter 

```java
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // 快速尝试直接添加到队尾
    Node pred = tail;
    if (pred != null) {// 队列已初始化
        node.prev = pred;// 1.设置前驱指针,设置新节点的prev指向当前tail
        if (compareAndSetTail(pred, node)) {// 2.CAS更新tail,将tail从pred更新为新节点
            pred.next = node;// 3.设置后继指针,将原tail的next指向新节点
            return node;
        }
    }
    // 如果快速尝试失败，则进入完整入队流程（慢速路径）
    enq(node);
    return node;
}
```



##### enq方法

完整入队（enq方法）,当快速尝试失败（队列为空或CAS竞争失败）时调用，通过自旋 CAS 操作确保节点被正确添加到队列中：

1. 如果队列为空，先初始化一个空节点作为头节点
2. 不断尝试将节点添加到队尾，直到成功

调用链路：ReentrantLock.lock()->Sync.lock()->acquire()->addWaiter->enq

```java
//完整入队流程
private Node enq(final Node node) {
    for (;;) { // 保证会一直自旋，直到成功
        Node t = tail;
        if (t == null) { // 队列未初始化
            if (compareAndSetHead(new Node())) // 虚拟节点（dummy node）作为head
                tail = head;//将tail也指向这个dummy节点
        } else {//节点追加,与快速路径类似，但在自旋中保证最终成功：
            node.prev = t;//设置新节点的prev指针
            if (compareAndSetTail(t, node)) {//CAS更新tail
                t.next = node;//设置前驱的next指针
                return t;
            }
        }
    }
}
```



#### acquireQueued

负责管理等待队列中的节点，并决定线程如何进行排队等待获取锁。这是核心的队列中获取资源的方法

* 工作流程
  1. 只有前驱节点是头节点时才会尝试获取资源（公平性保证）
  2. 获取成功后将自己设为头节点
  3. 获取失败后判断是否需要阻塞（`shouldParkAfterFailedAcquire`）
  4. 如果需要阻塞则调用 `parkAndCheckInterrupt` 阻塞并检查中断
  5. 如果整个过程失败，取消获取（`cancelAcquire`）
  6. 如果被挂起后又被唤醒，继续尝试获取锁
  7. 如果在等待过程中被中断，返回 true，否则返回 false

* 参数：
  node：当前线程对应的队列节点（由addWaiter创建）
  arg：获取锁的参数（通常为1，表示获取一个许可）
* 返回值：
  true：线程在等待过程中被中断
  false：正常获取到锁

调用链路：ReentrantLock.lock()->Sync.lock()->acquire(1)->acquireQueued

```
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) { // 自旋尝试获取锁，直到成功获取为止或者线程被中断
            final Node p = node.predecessor(); // 获取当前节点的前驱节点
            
            // 检查前驱节点 p 是否是当前队列的头节点 head，并尝试获取锁。
            // 意思为：当前节点是队列中的第一个真实等待节点,这个节点有资格尝试获取锁
            if (p == head && tryAcquire(arg)) {
                // 头节点 head 在等待队列中起着重要的作用，它通常是已经成功获取锁或者最近一次释放锁的节点。
                setHead(node);// 将当前节点 node 设置为新的头节点
                p.next = null; // help GC 取消掉前驱节点的引用,以便垃圾回收器能够释放内存空间
                failed = false;
                return interrupted;
            }
            // 用于判断在获取锁失败后，当前线程是否应该被挂起（park）
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())//挂起线程：调用LockSupport.park阻塞当前线程
                interrupted = true;
        }
    } finally {
        if (failed)
            //通常只有抛出异常时failed为true
            //取消当前节点 node 的获取锁操作。这个操作通常用于清理等待队列中因为获取锁失败而处于等待状态的节点。
            cancelAcquire(node);
    }
}
```

> * setHead
>
>   setHead(node)的作用
>   
>   **关键点**：`setHead(node)`确实是将当前节点设置为新的头节点，但它的意义不在于"虚拟节点"的概念，而在于维护AQS队列的正确性和高效性。
>   
>   **1. 为什么需要setHead(node)**
>   
>   当线程成功获取锁时：
>   
>   - 当前节点(已经获取锁的线程对应的节点)不再需要排队
>   - 该节点成为新的"虚拟头节点"，代表当前持有锁的线程
>   - 原来的头节点(真正的虚拟节点)可以被丢弃
>   
>   **2. 虚拟节点的本质**
>   
>   AQS队列确实使用虚拟节点机制：
>   
>   - 初始时队列有一个虚拟节点(dummy node)
>   - 每当一个线程获取锁后，它的节点会成为新的虚拟节点
>   - 这样设计有几个优势：
>     - 统一了空队列和非空队列的处理逻辑
>     - 简化了边界条件判断
>     - 使得队列始终有一个节点，避免复杂的空队列检查
>   
>   源码
>   
>   > ```
>   > private void setHead(Node node) {
>   >  head = node;
>   >  node.thread = null;
>   >  node.prev = null;
>   > }
>   > ```

##### shouldParkAfterFailedAcquire

这个方法决定获取失败后是否需要阻塞：

1. 如果前驱节点状态是 SIGNAL，表示可以安全阻塞
2. 如果前驱节点已取消，跳过这些节点
3. 否则将前驱节点状态设为 SIGNAL

调用链路：ReentrantLock.lock()->Sync.lock()->acquire(1)->acquireQueued->shouldParkAfterFailedAcquire
用于判断在获取锁失败后，当前线程是否应该被挂起（park）

* 参数说明
  * pred: 当前节点的前驱节点
  * node: 当前节点（当前线程对应的节点）
* 返回说明：当前线程是否应该被挂起
  * true 挂起
  * false 不挂起

* 方法作用
  * 判断在获取锁失败后，当前线程是否应该被挂起，以避免不必要的自旋消耗CPU资源。
* 源码分析

```
  private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
      int ws = pred.waitStatus;
      if (ws == Node.SIGNAL)
          /*
           * 前驱节点的waitStatus是SIGNAL(-1)，表示前驱节点释放锁后会通知当前节点
           * 所以可以安全地park当前线程
           */
          return true;
      if (ws > 0) {
          /*
           * 前驱节点被取消了（CANCELLED=1），跳过这些被取消的节点
           * 直到找到一个未被取消的前驱节点
           */
          do {
              node.prev = pred = pred.prev;
          } while (pred.waitStatus > 0);
          pred.next = node;
      } else {
          /*
           * waitStatus为 初始状态(0)或PROPAGATE(-3)
           * 使用CAS将前驱节点的waitStatus设置为SIGNAL(-1)，表示需要通知
           */
          compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
      }
      return false;
  }
```
> `compareAndSetWaitStatus(pred, ws, Node.SIGNAL)` 将前驱节点状态**从当前状态(0或-3)变为SIGNAL(-1)**
>
> 为什么需要这个转换？
>
> 1. **建立通知机制**：
>    - 将前驱节点设为SIGNAL意味着："当你释放锁时，请通知我"
>    - 这是等待/通知机制的基础

##### parkAndCheckInterrupt 

阻塞当前线程并返回中断状态：

1. 调用 `LockSupport.park` 阻塞当前线程
2. 被唤醒后返回中断状态（并清除中断标志）

调用链路：ReentrantLock.lock()->Sync.lock()->acquire(1)->acquireQueued->parkAndCheckInterrupt 

```
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}
```

**工作流程**

1. 当线程获取锁失败后，会进入等待队列
2. 在适当的时机（前驱节点状态为 SIGNAL），调用 `park()` 阻塞线程
3. 当持有锁的线程释放锁时，会唤醒队列中的后继节点（通过 `unpark`）
4. 被唤醒的线程继续尝试获取锁

> [LockSupport.park vs Object.wait vs Condition.await](# LockSupport.park vs Object.wait vs Condition.await)

# 解锁-unlock

* unlock() 是释放锁的核心方法，其实现涉及锁状态管理、线程唤醒和队列操作等多个关键并发机制。

## unlock

**ReentrantLock.unlock()**

```
public void unlock() {
    sync.release(1); // 委托给Sync内部类
}
```

### release

AbstractQueuedSynchronizer.release()

**ReentrantLock.unlock()->sync.release(1)**

```
public final boolean release(int arg) {
    if (tryRelease(arg)) {      // 尝试释放锁（子类实现）
        Node h = head;          // 获取头节点
        //这个条件语句的作用是在释放锁时，检查头节点的状态是否正常。如果头节点的等待状态不为正常状态，则表示当前头节点可能处于一种特殊状态
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);  // 唤醒后继节点
        return true;
    }
    return false;
}
```

> 为什么用 != 0 而不是 == -1？
>
> 1. **实际可能的状态值**：
>    - `SIGNAL(-1)`：最常见情况，表示需要唤醒后继
>    - `PROPAGATE(-3)`：共享模式下可能出现
>    - `0`：初始状态（不需要唤醒）
>    - `>0`：CANCELLED（不应该出现在头节点）

#### tryRelease

尝试释放锁

ReentrantLock.unlock()->sync.release()->ReentrantLock.tryRelease()

```
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;  // getState() 获取当前锁状态（对于 ReentrantLock 表示重入次数）
    if (Thread.currentThread() != getExclusiveOwnerThread())//确保只有锁持有者能调用unlock
        throw new IllegalMonitorStateException(); // 非持有线程调用unlock
  
    boolean free = false;
    if (c == 0) {               // 完全释放锁
        free = true;    //当state归零时才返回true
        setExclusiveOwnerThread(null);//清owner
    }
    setState(c);                // 更新state（即使未完全释放）
    return free;
}
```

状态更新顺序：先清owner，再setState（避免指令重排问题）

#### unparkSuccessor

ReentrantLock.unlock()->sync.release()->unparkSuccessor
`unparkSuccessor` 是 AQS (AbstractQueuedSynchronizer) 中的一个关键方法，用于唤醒等待队列中合适的后继节点。

```
private void unparkSuccessor(Node node) {
    int ws = node.waitStatus;
    if (ws < 0) // 如果状态为SIGNAL或CONDITION等负值状态，尝试原子性地重置为0
        compareAndSetWaitStatus(node, ws, 0); // 清除信号状态

    // 查找下一个需要唤醒的节点
    Node s = node.next;
    if (s == null || s.waitStatus > 0) { // 后继节点无效（取消或null）
        s = null;
        //从后向前（tail → head）
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)      // 从尾向前找有效节点
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread);   // 唤醒线程
}
```

> 从后向前（tail → head）
>
> 核心原因：并发环境下的队列完整性
>
> 1. 节点入队操作的原子性问题
>
> AQS 的入队操作（`enq` 和 `addWaiter`）不是原子性的，它分为几个步骤：
>
> ```
> // 入队操作的非原子性步骤：
> 1. 新建节点的 prev 指向当前 tail
> 2. CAS 设置新节点为 tail（此时新节点的 next 还是 null）
> 3. 将原 tail 的 next 指向新节点
> ```
>
> 在步骤2完成但步骤3未完成时，如果另一个线程执行 `unparkSuccessor`，从前往后遍历就会漏掉这个新节点。
>
> 2. 为什么从后向前遍历能解决这个问题
>
> 因为节点总是**先设置 prev 指针，后设置 next 指针**：
>
> - 新节点的 prev 指针在 CAS 操作前就设置好了（可靠）
> - next 指针在 CAS 操作后才设置（可能延迟）
>
> 所以从 tail 向前遍历可以保证：
>
> - 一定能看到所有已入队的节点
> - 不会因为并发入队操作而漏掉任何节点

## 与AQS核心相关方法

release()方法开始进入AQS核心类AbstractQueuedSynchronizer

```
ReentrantLock.unlock()
  → Sync.release(1) (AQS)
    → tryRelease(1) (Sync实现)
    → unparkSuccessor() (AQS)
```

# 加锁解锁-原理分析

## **加锁流程详解**

```
[线程T1尝试加锁]
│
├─ (1) 首次加锁
│   ├─ state = 0 (锁未被占用)
│   ├─ CAS(0→1) 成功
│   └─ 设置独占线程 = T1
│
├─ (2) 重入加锁
│   ├─ state ≥ 1 (锁已被T1持有)
│   ├─ 检查独占线程 == T1
│   └─ state += 1 (重入计数增加)
│
└─ (3) 竞争加锁（失败）
    ├─ state ≥ 1 (锁被其他线程持有)
    ├─ 创建Node加入CLH队列尾部
    ├─ 进入阻塞状态 (LockSupport.park)
    └─ 被唤醒后重新尝试tryAcquire
```

## **解锁流程详解**

```
[线程T1释放锁]
│
├─ (1) 完全释放 (state-1 = 0)
│   ├─ 清空独占线程 (owner=null)
│   ├─ 唤醒队列头节点的后继线程
│   └─ 被唤醒线程竞争锁
│
└─ (2) 部分释放 (state-1 > 0)
    └─ 仅更新state计数 (仍保持T1独占)
```

## 多线程下lock/unlock的状态流转图

```
┌───────────────┐    ┌───────────────┐
│ 初始状态       │    │ 线程T1        │
│ state=0       │───▶│ lock()       │
│ owner=null    │    │ CAS(0→1)成功  │
└───────────────┘    └───────────────┘
                          │
                          ▼
┌───────────────┐    ┌───────────────┐
│ 锁定状态       │    │ 线程T2        │
│ state=1       │◀──┤ lock()       │
│ owner=T1      │    │ 加入CLH队列    │
└───────────────┘    └───────────────┘
                          │
                          ▼
┌───────────────┐    ┌───────────────┐
│ 重入状态       │    │ 线程T1        │
│ state=2       │◀──┤ unlock()     │
│ owner=T1      │    │ state=1       │
└───────────────┘    └───────────────┘
                          │
                          ▼
┌───────────────┐    ┌───────────────┐
│ 唤醒线程       │    │ 线程T2        │
│ 唤醒T2        │───▶│ 获取锁        │
└───────────────┘    │ state=1       │
                     │ owner=T2      │
                     └───────────────┘
```

## 多线程下lock/unlock时AQS队列状态变化

**逐节点分析lock/unlock时AQS队列状态变化过程（公平锁举例更直观）**
**一句话总结：节点入队(waitStatus=0) → 前驱节点设为SIGNAL(-1) → 阻塞等待 → 前驱unlock()唤醒 → 被唤醒节点tryAcquire()成功 → 节点变为头节点(waitStatus=0) → 持锁运行 → 释放锁(unlock())，再唤醒下个节点。**

* 假设现在有三个线程依次执行了lock()：
  * 线程A（第一个执行lock）
  * 线程B（第二个执行lock）
  * 线程C（第三个执行lock）
  * 线程A unlock

### 线程A执行lock()：

* 线程A调用tryAcquire()成功获取锁，此时AQS的state从0变为1，持有锁。
* 队列状态：队列为空（只有头节点，头节点代表当前持有锁的线程/或空节点），头节点waitStatus初始为0，不用修改。

```
A(Head, waitStatus=0, 持有锁)
```

### 线程B执行lock()：

* 线程B调用tryAcquire()失败（state已为1），进入等待队列。
* B节点入队，添加到A的后面。
* 队列结构为：

```
A(Head, waitStatus=0, 持锁) → B(waitStatus=0)
```

* 此时B入队后会调用shouldParkAfterFailedAcquire检查前驱节点A的状态：
* 由于前驱节点A的waitStatus=0，方法会将A的waitStatus设置为SIGNAL(-1)，表示当前节点（A）在释放锁时需要唤醒后继节点（B）。
* 队列状态变化为：

```
A(Head, waitStatus=-1, 持锁，释放锁时必须唤醒后继) → B(waitStatus=0)
```

* B线程检查完成后调用park()，B线程进入阻塞状态。

### 线程C执行lock()：

* 线程C调用tryAcquire()也失败（state仍为1），同样进入等待队列。
* C节点入队，追加到B后面：

```
A(Head, waitStatus=-1, 持锁) → B(waitStatus=0, 阻塞中) → C(waitStatus=0)
```

* C入队后，调用shouldParkAfterFailedAcquire检查前驱节点B的状态：
* 此时前驱节点B的状态waitStatus=0，因此设置B的waitStatus为SIGNAL(-1)，意味着B在未来获取锁并释放锁时必须唤醒C。
* 队列状态更新为：

```
A(Head, waitStatus=-1, 持锁) → B(waitStatus=-1, 阻塞中，唤醒后继) → C(waitStatus=0)
```

* C线程检查完成调用park()，进入阻塞状态。

**线程释放锁后的节点状态变化**

### A调用unlock()

* 此时线程A完成任务，调用unlock()释放锁：
  * 调用tryRelease()成功，state=0，释放锁成功。
  * A节点状态waitStatus为-1(SIGNAL)，意味着要唤醒后继节点B。
  * 调用unpark()唤醒线程B。
  * 同时将A的waitStatus重置为0（唤醒后状态恢复初始）。
* B被唤醒后尝试获取锁(tryAcquire())，获取成功：(acquireQueued方法中 中断的线程继续执行)
  * B设置为头节点。
  * 旧的头节点A出队，B成为新头节点，节点状态重置为0。
  * 队列更新为：

```
B(Head, waitStatus=0, 持锁) → C(waitStatus=0, 阻塞中)
````

* 此时B为头节点持有锁。B的waitStatus重置为0（新头节点状态总是重置）。
* C节点此时waitStatus为0，C唤醒机制还未触发，后续再有新的节点入队或B释放锁时再更新。







# Condition
* Condition notEmpty = lock.newCondition() 是 Java 并发编程中基于显式锁（Lock）的线程通信机制的核心实现，主要用于精细化控制线程的等待/唤醒
>对比Object.wait()/notify()
## 1. 基础概念
* (1) Condition 是什么？
  *  属于 java.util.concurrent.locks 包，必须与 Lock 配合使用（不能单独存在）。
  *  作用：替代传统的 Object.wait()/notify()，提供更灵活的线程等待与唤醒机制。
  *  核心方法：
    *  await()：释放锁并等待（类比 wait()）
    *  signal()：唤醒一个等待线程（类比 notify()）
    *  signalAll()：唤醒所有等待线程（类比 notifyAll()）
* (2) 典型应用场景
  * 生产者-消费者模型（不同条件控制队列空/满）
  *  线程池任务调度
  *  多阶段任务协作
##  底层实现原理
* 与 AQS（AbstractQueuedSynchronizer）的关系
  * 每个 Condition 对象内部维护一个条件队列（单向链表），存储调用 await() 的线程。
  * 当调用 signal() 时，将条件队列中的线程转移到锁的同步队列（AQS 核心队列），等待获取锁。



# ReentrantLock中断处理

在 Java 的 `ReentrantLock` 中，**中断（Interruption）** 的处理逻辑取决于线程是在 **尝试获取锁** 还是 **已经在锁等待队列中**。以下是详细的代码路径和反应：

------

### **1. 线程在 `lock()` 时被中断**

**非响应中断的 `lock()`**

```
ReentrantLock lock = new ReentrantLock();
lock.lock(); // 如果其他线程持有锁，当前线程会阻塞，但不会响应中断
```

- **行为**：即使线程被中断（`thread.interrupt()`），仍会继续等待锁，**不会抛出异常**。
- **中断状态**：中断标志会被保留（`Thread.interrupted()` 返回 `true`），但锁获取流程不受影响。

**响应中断的 `lockInterruptibly()`**

```
try {
    lock.lockInterruptibly(); // 可能抛出 InterruptedException
} catch (InterruptedException e) {
    // 处理中断
}
```

- **行为**：

  1. 如果线程在等待锁时被中断，会立即抛出 `InterruptedException`。
  2. 线程会**退出锁获取流程**，并移除 AQS 队列中的对应节点（`waitStatus=1`，标记为取消）。

- **源码关键路径**：

  ```
  public void lockInterruptibly() throws InterruptedException {
      sync.acquireInterruptibly(1); // AQS 模板方法
  }
  ```

  - 最终调用 `AbstractQueuedSynchronizer.doAcquireInterruptibly`，在 `parkAndCheckInterrupt()` 中检测中断。

------

### **2. 线程在 `unlock()` 时被中断**

- **行为**：`unlock()` 操作**不会被中断**，因为它是非阻塞操作。
- **特殊场景**：如果线程在 `unlock()` 前被中断，且持有锁，则：
  - 正常释放锁（`state` 减 1）。
  - 中断状态对解锁无影响，但可能影响后续业务逻辑。

------

### **3. 中断的底层处理流程**

**加锁时中断（以 `lockInterruptibly()` 为例）**


``` mermaid
flowchart TD
    A[lockInterruptibly] --> B[AQS.acquireInterruptibly]
    B --> C[tryAcquire 尝试获取锁]
    C -->|失败| D[addWaiter 加入CLH队列]
    D --> E[acquireQueued 自旋获取]
    E --> F[parkAndCheckInterrupt 阻塞检查中断]
    F -->|被中断| G[抛出InterruptedException]
    G --> H[取消节点并清理队列]
```

**关键代码片段**

```
// AbstractQueuedSynchronizer.doAcquireInterruptibly
private void doAcquireInterruptibly(int arg) throws InterruptedException {
    final Node node = addWaiter(Node.EXCLUSIVE);
    try {
        for (;;) {
            final Node p = node.predecessor();
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null;
                return;
            }
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt()) {
                throw new InterruptedException(); // 直接抛出异常
            }
        }
    } catch (Throwable t) {
        cancelAcquire(node); // 清理取消的节点
        throw t;
    }
}
```

------

### **4. 中断后的状态变化**

| 场景                       | 线程状态变化             | AQS 队列变化                        |
| :------------------------- | :----------------------- | :---------------------------------- |
| `lockInterruptibly()` 中断 | 抛出异常，线程终止获取锁 | 节点被移出队列（`waitStatus=1`）    |
| `lock()` 中断              | 保留中断标志，继续阻塞   | 节点保持等待状态（`waitStatus=-1`） |
| `unlock()` 时中断          | 无影响                   | 无影响                              |

------

### **5. 最佳实践**

1. **需要响应中断**：使用 `lockInterruptibly()` + `try-finally`：

   ```
   try {
       lock.lockInterruptibly();
       try {
           // 临界区代码
       } finally {
           lock.unlock();
       }
   } catch (InterruptedException e) {
       // 处理中断（如回滚操作）
   }
   ```
   
2. **不响应中断**：用 `lock()`，但需手动检查中断状态：

   ```
   lock.lock();
   try {
       if (Thread.interrupted()) {
           // 补偿逻辑
       }
       // 临界区代码
   } finally {
       lock.unlock();
   }
   ```

------

### **总结**

- **`lock()`**：忽略中断，仅记录标志位。
- **`lockInterruptibly()`**：立即响应中断，抛出异常。
- **`unlock()`**：始终成功，与中断无关。

通过理解这些行为，可以更好地设计可中断的并发任务和资源清理逻辑。

# ReentrantLock 设计哲学

> [ReentrantLock 的设计哲学](# ReentrantLock 的设计哲学)

# 扩展

## synchronized与volatile、ReentrantLock的区别
* https://way2j.com/a/549#synchronized%E4%B8%8EReentrantLock
> 	Lock 等待锁时可用interrupt来中断等待 synchronized不能实现

# 其他

## ReentrantLock可见性保证

Java 中 `ReentrantLock` 的**可见性保证**（即线程释放锁时强制刷新工作内存到主内存，获取锁时强制从主内存重新加载变量）是通过 **内存屏障（Memory Barriers）** 和 **happens-before 规则** 实现的。具体来说，它的底层实现依赖 `AbstractQueuedSynchronizer (AQS)` 和 `volatile` 变量，并结合了 CPU 级别的内存屏障指令。

------

**1. 底层机制：`volatile` 变量 + CAS + 内存屏障**

`ReentrantLock` 的内部实现基于 `AbstractQueuedSynchronizer (AQS)`，而 AQS 使用了一个 `volatile int state` 变量来表示锁的状态（0=未锁定，1=锁定）。`volatile` 变量的读写会插入内存屏障，从而保证：

- **写操作（释放锁）**：强制将当前线程的工作内存刷新到主内存（`StoreStore` + `StoreLoad` 屏障）。
- **读操作（获取锁）**：强制从主内存重新加载变量（`LoadLoad` + `LoadStore` 屏障）。

**示例：AQS 中的 `state` 变量**

```
// AbstractQueuedSynchronizer 中的关键变量
private volatile int state;  // volatile 保证可见性
```

- 当线程 **获取锁（`lock()`）** 时，会读取 `state`，触发 `LoadLoad` 和 `LoadStore` 屏障，强制从主内存加载最新值。
- 当线程 **释放锁（`unlock()`）** 时，会修改 `state`，触发 `StoreStore` 和 `StoreLoad` 屏障，强制将修改刷回主内存。

------

**2. Happens-Before 规则**

Java 内存模型（JMM）规定：

- **解锁操作（`unlock()`） happens-before 后续的加锁操作（`lock()`）**。
- 这意味着：
  - **释放锁的线程** 在 `unlock()` 之前的所有修改，对 **获取锁的线程** 在 `lock()` 之后都是可见的。

**示例：`ReentrantLock` 的可见性保证**

```
ReentrantLock lock = new ReentrantLock();
int sharedVar = 0;

// 线程1：修改 sharedVar 并释放锁
lock.lock();
try {
    sharedVar = 42;  // 修改共享变量
} finally {
    lock.unlock();   // 强制刷新到主内存
}

// 线程2：获取锁并读取 sharedVar
lock.lock();
try {
    System.out.println(sharedVar);  // 保证读取到 42
} finally {
    lock.unlock();
}
```

- 线程1 在 `unlock()` 时，`sharedVar = 42` 会被强制刷回主内存。
- 线程2 在 `lock()` 时，会强制从主内存重新加载 `sharedVar`，保证读取到最新值 42。

------

**3. CPU 级内存屏障（Memory Barriers）**

在底层，`volatile` 和 `CAS`（`compareAndSet`）操作会插入 CPU 级的内存屏障指令（如 `mfence`、`lfence`、`sfence`），防止指令重排序并保证内存可见性：

- **`lock()` 时**：
  - 读取 `volatile state`，插入 `LoadLoad` + `LoadStore` 屏障，防止后续读操作重排序到锁之前。
- **`unlock()` 时**：
  - 修改 `volatile state`，插入 `StoreStore` + `StoreLoad` 屏障，确保之前的写操作对其他线程可见。

------

**4. 对比 `synchronized`**

`synchronized` 也提供类似的可见性保证，但 `ReentrantLock` 的底层实现更透明：

| 特性       | `ReentrantLock`                        | `synchronized`           |
| :--------- | :------------------------------------- | :----------------------- |
| **可见性** | 通过 `volatile` + 内存屏障             | 通过 JVM 管程（Monitor） |
| **有序性** | 临界区内允许重排序（不影响单线程语义） | 临界区内允许重排序       |
| **实现**   | 基于 `AQS` + `CAS`                     | 基于 JVM 内置锁          |
| **灵活性** | 支持 `tryLock()`、可中断锁等           | 不支持                   |

------

**结论**

`ReentrantLock` 的可见性保证是通过：

1. **`volatile` 变量**（`AQS` 的 `state`）强制刷新/加载内存。
2. **happens-before 规则**（`unlock()` → `lock()` 保证可见性）。
3. **CPU 内存屏障**（防止指令重排序）。

因此，**`ReentrantLock` 能确保释放锁时强制刷新工作内存，获取锁时强制从主内存重新加载变量**，从而保证多线程环境下的可见性。

## 关于 ConditionDemo 示例中 `t1.join()` 的详细解释

在 `ConditionDemo` 示例中调用 `t1.join()` 有以下几个重要原因，我来详细说明：

**1. 主线程等待子线程完成**

`join()` 方法的主要作用是让当前线程（这里是主线程）等待调用 `join()` 的线程（这里是 `t1`）终止。如果不调用 `join()`：

```
public static void main(String[] args) throws InterruptedException {
    ConditionDemo demo = new ConditionDemo();
    
    Thread t1 = new Thread(() -> {
        try {
            demo.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
    
    t1.start();
    
    Thread.sleep(2000);
    demo.signal();
    
    // 如果没有 t1.join()，主线程可能在此处结束
}
```

没有 `join()` 时，主线程可能在 `t1` 完成前就结束了，导致我们无法观察到完整的程序执行过程。

**2. 确保程序完整执行**

在这个特定示例中，`t1.join()` 确保了：

1. 主线程创建并启动 `t1` 线程
2. 主线程休眠 2 秒
3. 主线程调用 `demo.signal()` 唤醒 `t1`
4. 主线程等待 `t1` 完成它的工作

如果没有 `join()`，程序可能在 `t1` 被唤醒但还未执行完后续代码时就退出了。

**3. 防止 JVM 过早退出**

Java 程序在所有非守护线程结束后才会退出。在这个简单示例中：

- 主线程是唯一的非守护线程（`t1` 也是非守护线程，默认情况下）
- 如果没有 `join()`，主线程可能在 `t1` 还在等待时就已经结束
- 但因为 `t1` 是非守护线程，JVM 会等待 `t1` 结束，所以在这个简单例子中不加 `join()` 也能看到完整输出

但在更复杂的程序中，如果不恰当管理线程生命周期，可能会导致主线程结束后其他线程被强制终止。

**4. 保证示例的演示效果**

在这个教学示例中，`join()` 的主要目的是：

- 确保示例代码的执行顺序清晰可见
- 防止主线程过早退出导致控制台输出不完整
- 展示一种良好的线程同步实践模式

**5. 如果不使用 join() 会怎样？**

在这个特定示例中，由于：

1. `t1` 是非守护线程
2. 主线程调用了 `signal()` 唤醒了 `t1`
3. 程序逻辑简单，执行时间短

所以即使不加 `join()`，程序也能正常运行并显示完整输出。但在实际开发中，这是一种不可靠的做法，因为：

- 如果 `t1` 执行时间较长，主线程结束后可能导致资源未正确释放
- 在多线程环境中，线程执行顺序不确定，可能导致意外结果
- 不利于调试和问题排查

**6. 最佳实践建议**

在演示代码和生产代码中，都建议：

1. 对启动的子线程进行适当的管理
2. 使用 `join()` 或其它同步机制确保线程正确完成
3. 对于需要等待线程结束的场景，明确使用同步机制
4. 避免依赖 JVM 的线程管理行为

**总结**

在这个 `ConditionDemo` 示例中，`t1.join()` 的主要作用是：

- 确保主线程等待 `t1` 完成
- 保证示例的演示效果完整
- 展示正确的多线程同步实践
- 防止主线程过早退出导致观察不到完整执行过程

虽然在这个简单示例中不加 `join()` 也能工作，但在实际开发中，正确处理线程生命周期是编写健壮并发程序的重要部分。

## ReentrantLock 的 lockInterruptibly() 方法详解

`lockInterruptibly()` 是 `ReentrantLock` 类中的一个重要方法，它提供了可中断的锁获取机制。与普通的 `lock()` 方法相比，它允许在等待获取锁的过程中响应中断。

**方法定义**

```
public void lockInterruptibly() throws InterruptedException
```

**主要特点**

1. **可中断性**：如果线程在等待锁的过程中被中断，会抛出 `InterruptedException`
2. **公平性**：是否公平取决于锁的构造方式（公平锁或非公平锁）
3. **重入性**：与 `lock()` 方法一样支持重入

**与 lock() 方法的区别**

| 特性     | lock()                        | lockInterruptibly()         |
| :------- | :---------------------------- | :-------------------------- |
| 中断响应 | 不响应中断                    | 响应中断                    |
| 异常抛出 | 不会抛出 InterruptedException | 会抛出 InterruptedException |
| 中断处理 | 保持等待状态                  | 抛出异常并停止等待          |

**使用示例**

```
ReentrantLock lock = new ReentrantLock();

try {
    // 可中断地获取锁
    lock.lockInterruptibly();
    try {
        // 临界区代码
    } finally {
        lock.unlock();
    }
} catch (InterruptedException e) {
    // 处理中断异常
    Thread.currentThread().interrupt(); // 恢复中断状态
    // 执行其他清理工作
}
```

**适用场景**

1. 当需要支持任务取消时
2. 在需要响应中断的线程中使用
3. 实现可取消的任务执行框架

**注意事项**

1. 必须正确处理 `InterruptedException`
2. 解锁操作应放在 finally 块中确保执行
3. 中断后应恢复中断状态（调用 `Thread.currentThread().interrupt()`）
4. 使用此方法会增加一定的性能开销

`lockInterruptibly()` 方法为并发编程提供了更灵活的锁获取方式，特别是在需要支持任务取消或响应中断的场景中非常有用。

## `hasQueuedPredecessors()` 方法深度解析

`hasQueuedPredecessors()` 是 ReentrantLock 公平模式下的核心方法，用于判断当前线程获取锁时是否需要排队。下面我将从多个角度深入分析这个方法。

**方法定义**

```
public final boolean hasQueuedPredecessors() {
    Node t = tail; // 尾节点
    Node h = head; // 头节点
    Node s;
    return h != t && // 队列不为空
        ((s = h.next) == null || s.thread != Thread.currentThread());
}
```

**方法执行逻辑分解**

1. **初始检查**：
   - 获取队列的 head 和 tail 节点
   - `h != t` 判断队列是否为空
     - 相等表示队列为空（新初始化时 head=tail=空节点）
     - 不等表示队列中有等待节点
2. **二次检查**：
   - 如果队列不为空，获取头节点的下一个节点 `h.next`
   - 两种情况返回 true（需要排队）：
     - `s == null`：并发情况下其他线程正在入队
     - `s.thread != Thread.currentThread()`：队首不是当前线程

**四种典型场景分析**

| 场景 | 队列状态                 | h != t | s == null | s.thread != current | 返回值 | 含义     |
| :--- | :----------------------- | :----- | :-------- | :------------------ | :----- | :------- |
| 1    | 队列为空                 | false  | -         | -                   | false  | 无需排队 |
| 2    | 有节点且当前线程是队首   | true   | false     | false               | false  | 无需排队 |
| 3    | 有节点且当前线程不是队首 | true   | false     | true                | true   | 需要排队 |
| 4    | 正在入队（竞争状态）     | true   | true      | -                   | true   | 需要排队 |

**源码设计精妙之处**

1. **并发安全处理**：

   - 方法无锁操作，仅读取volatile变量（head/tail）

   - 通过`h.next == null`处理其他线程正在入队的临界状态

     > 此处的临界状态是 [addWaiter 方法](# addWaiter 方法)进行cas替换最后一个节点，同时这个节点的next元素没有volatile保护，还在并发更新中

2. **性能优化**：

   - 先检查`h != t`快速路径（多数情况队列为空）
   - 避免不必要的线程比较

3. **公平性保证**：

   - 只有队列为空或当前线程是队首时才允许获取锁
   - 严格保证FIFO顺序

**与AQS的关联**

1. **节点状态流转**：

   - 新节点入队时，会先设置prev指针，再CAS设置tail
   - 最后设置前驱的next指针（存在短暂时间窗口h.next==null）

2. **与acquireQueued的配合**：

   ```
   final boolean acquireQueued(final Node node, int arg) {
       for (;;) {
           final Node p = node.predecessor();
           if (p == head && tryAcquire(arg)) {  // 只有前驱是head才能尝试
               setHead(node);
               // ...
           }
           // ...
       }
   }
   ```

   - 双重检查保证：hasQueuedPredecessors()和p==head

**典型使用场景**

在公平锁的tryAcquire中：

```
protected final boolean tryAcquire(int acquires) {
    if (!hasQueuedPredecessors() && // 检查是否需要排队
        compareAndSetState(0, acquires)) {
        setExclusiveOwnerThread(current);
        return true;
    }
    // ...
}
```

**注意事项**

1. **虚假返回true**：

   - 在并发入队时可能短暂返回true，即使队列最终只有当前线程
   - 这是为了保守保证公平性

2. **与nonfairTryAcquire对比**：

   ```
   final boolean nonfairTryAcquire(int acquires) {
       // 直接尝试CAS，不检查队列
       if (c == 0) {
           if (compareAndSetState(0, acquires)) {
               // ...
           }
       }
       // ...
   }
   ```

3. **性能影响**：

   - 公平模式下每次获取锁都需要此检查
   - 非公平锁省略此检查可提高吞吐量约5-10倍

这个方法虽然代码简短，但通过精妙的设计实现了：

1. 严格的公平性保证
2. 高并发的正确性
3. 最佳的性能平衡

## ReentrantLock 非公平锁的两次 CAS 操作分析

在 Java 的 `ReentrantLock` 非公平锁实现中，确实会在 `NonfairSync.lock()` 和 `Sync.nonfairTryAcquire()` 方法中执行两次 CAS 操作，这看起来有些冗余，但实际上是有特定设计考虑的。

**两次 CAS 操作的原因**

1. **第一次 CAS (在 NonfairSync.lock())**:

   ```
   final void lock() {
       if (compareAndSetState(0, 1))  // 第一次 CAS
           setExclusiveOwnerThread(Thread.currentThread());
       else
           acquire(1);
   }
   ```

   - 这是一个"快速路径"尝试，目的是在锁未被持有(状态为0)时，快速获取锁而不进入完整的获取流程
   - 如果成功，可以避免创建节点和入队等开销
   - 体现了非公平锁的特性：新来的线程可以"插队"尝试获取锁

2. **第二次 CAS (在 nonfairTryAcquire())**:

   ```
   final boolean nonfairTryAcquire(int acquires) {
       // ...
       if (c == 0) {
           if (compareAndSetState(0, acquires)) {  // 第二次 CAS
               setExclusiveOwnerThread(current);
               return true;
           }
       }
       // ...
   }
   ```

   - 这是在 acquire() 方法流程中的标准获取尝试
   - 即使第一次快速尝试失败，锁可能已经被释放(比如持有锁的线程很快释放了锁)
   - 再次检查并尝试获取，减少线程挂起的可能性

**设计考虑**

1. **性能优化**：第一次 CAS 是快速路径，很多情况下锁是可用的，这样可以避免完整获取流程的开销
2. **减少线程挂起**：即使第一次失败，在进入队列前再尝试一次，避免不必要的线程挂起
3. **非公平性体现**：新线程总是有机会"插队"获取锁，而不是直接排队

**执行流程示例**

```
线程A调用lock():
1. 第一次CAS成功 → 直接获取锁
   或
1. 第一次CAS失败 → 进入acquire()
   2. 调用tryAcquire() (实际是nonfairTryAcquire())
      3. 第二次CAS尝试
         成功 → 获取锁
         失败 → 加入队列等待
```

这种设计是典型的"优化快速路径，但保留完整路径"的实现方式，在并发编程中很常见。

## AQS 节点状态详细分析

在 AbstractQueuedSynchronizer (AQS) 中，每个等待线程都被封装为一个 Node 节点，这些节点通过状态（waitStatus）来协调线程间的同步。AQS 的节点状态是整个同步器实现的核心机制之一。

**Node 节点状态常量**

在 AQS 的 Node 类中定义了以下几种状态：

```
static final class Node {
    // 节点状态常量
    static final int CANCELLED =  1;  // 节点取消状态
    static final int SIGNAL    = -1;  // 后继节点需要被唤醒
    static final int CONDITION = -2;  // 节点在条件队列中等待
    static final int PROPAGATE = -3;  // 共享模式下传播状态
    
    volatile int waitStatus;  // 当前节点状态
    // 其他字段...
}
```

**各状态详细解析**

**1. CANCELLED (1)**

**含义**：表示该节点对应的线程已经取消获取锁。

**触发场景**：

- 线程在等待过程中被中断（且中断处理策略为取消）
- 线程在等待过程中超时
- 其他原因导致的获取锁失败

**特点**：

- 这是唯一一个正值状态
- 处于此状态的节点会被移出同步队列
- 节点的线程不会再被唤醒

**2. SIGNAL (-1)**

**含义**：表示当前节点的后继节点需要被唤醒。

**触发场景**：

- 当一个线程获取锁失败被加入队列时，它的前驱节点会被设置为 SIGNAL 状态
- 表示"当前节点释放锁或取消时，需要唤醒后继节点"

**特点**：

- 这是同步队列中最常见的状态
- 保证了锁释放时的可靠通知机制
- 节点在释放锁时会检查这个状态

**3. CONDITION (-2)**

**含义**：表示节点当前在条件队列中等待。

**触发场景**：

- 当线程调用 `Condition.await()` 时
- 节点从同步队列转移到条件队列

**特点**：

- 只在条件队列中使用
- 当条件满足时，节点会从条件队列转移回同步队列
- 转移后状态会被重置为 0

**4. PROPAGATE (-3)**

**含义**：共享模式下传播释放状态。

**触发场景**：

- 在共享模式下（如 Semaphore）
- 当一个释放操作需要传播给多个节点时

**特点**：

- 仅用于共享模式
- 确保共享资源的释放能正确传播
- 在 doReleaseShared 方法中使用

**5. 初始状态 (0)**

**含义**：节点的初始状态或表示不需要特殊处理。

**触发场景**：

- 新创建的节点
- 从条件队列转移回同步队列的节点

**特点**：

- 既不是上述任何一种特殊状态
- 可能很快会转变为其他状态

**状态转换图**

```
初始状态(0)
  │
  ├── 获取锁失败 ──> SIGNAL(-1) ──> 成功/取消 ──> CANCELLED(1)
  │
  ├── await() ──> CONDITION(-2) ──> signal() ──> 重新入队(0)
  │
  └── 共享模式 ──> PROPAGATE(-3)
```

**状态在同步过程中的作用**

**获取锁时（acquire）**

1. 新节点入队时，会将前驱节点的 waitStatus 设置为 SIGNAL
2. 这表示"当前节点释放锁时需要唤醒我"
3. 如果前驱节点状态>0（CANCELLED），则跳过这些节点

**释放锁时（release）**

1. 检查头节点的 waitStatus
2. 如果状态<0（通常是 SIGNAL），则唤醒后继节点
3. 唤醒后后继节点会尝试获取锁

**取消时（cancelAcquire）**

1. 将节点状态设置为 CANCELLED
2. 清理节点链接
3. 如果前驱节点是 SIGNAL，可能需要唤醒后继节点

**关键方法中的状态处理**

**shouldParkAfterFailedAcquire**

```
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        return true; // 前驱节点是SIGNAL，可以安全park
    if (ws > 0) { // 前驱节点已取消
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        // 设置前驱节点为SIGNAL
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```

**cancelAcquire**

```
private void cancelAcquire(Node node) {
    if (node == null)
        return;
    node.thread = null;
    // 跳过已取消的前驱节点
    Node pred = node.prev;
    while (pred.waitStatus > 0)
        node.prev = pred = pred.prev;
    // 获取pred的后继节点（可能是node）
    Node predNext = pred.next;
    // 将节点状态设为CANCELLED
    node.waitStatus = Node.CANCELLED;
    // 如果是尾节点，则CAS设置尾节点为pred
    if (node == tail && compareAndSetTail(node, pred)) {
        compareAndSetNext(pred, predNext, null);
    } else {
        // 如果不是尾节点，需要确保后继节点能被唤醒
        int ws;
        if (pred != head &&
            ((ws = pred.waitStatus) == Node.SIGNAL ||
             (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
            pred.thread != null) {
            Node next = node.next;
            if (next != null && next.waitStatus <= 0)
                compareAndSetNext(pred, predNext, next);
        } else {
            unparkSuccessor(node);
        }
        node.next = node; // help GC
    }
}
```

**实际应用示例**

**ReentrantLock 中的状态流转**

1. 线程A获取锁：队列为空，直接获取
2. 线程B尝试获取：
   - 创建节点加入队列，前驱节点(头节点)状态设为SIGNAL
   - 线程B park等待
3. 线程A释放锁：
   - 检查头节点状态为SIGNAL
   - 唤醒线程B
4. 线程B被唤醒：
   - 检查前驱是头节点
   - 尝试获取锁成功
   - 将自己设为新头节点

**Condition 中的状态流转**

1. 线程调用 await()：
   - 节点从同步队列转移到条件队列，状态变为CONDITION
2. 其他线程调用 signal()：
   - 节点从条件队列转移回同步队列，状态重置为0
   - 然后可能被设为SIGNAL

**总结**

AQS 的节点状态机制是其高效实现同步功能的核心：

1. **SIGNAL** 是同步队列中最关键的状态，构建了可靠的等待-通知机制
2. **CANCELLED** 状态处理了各种异常情况下的资源清理
3. **CONDITION** 状态实现了灵活的条件等待机制
4. **PROPAGATE** 状态优化了共享模式的释放传播
5. 状态转换通过 CAS 操作保证原子性

理解这些状态及其转换关系，是掌握 AQS 工作原理的关键。这些状态共同构成了 AQS 高效、灵活的同步基础，支撑了 Java 中各种锁和同步器的实现。



## ReentrantLock 的设计哲学

`ReentrantLock` 是 Java 并发包 (`java.util.concurrent.locks`) 中基于 **AQS（AbstractQueuedSynchronizer）** 实现的可重入互斥锁，其设计哲学可以概括为以下几个核心思想：

------

**1. 显式锁控制（Explicit Locking）**

- **与 `synchronized` 对比**：
  - `synchronized` 是 JVM 内置的隐式锁，自动加锁/解锁。
  - `ReentrantLock` 要求开发者 **手动调用 `lock()` 和 `unlock()`**，强调对锁行为的精确控制。
- **设计意图**：
  - 提供更灵活的锁管理（如可中断、超时、公平性选择）。
  - 避免 `synchronized` 的局限性（如无法中断阻塞线程）。

------

**2. 可重入性（Reentrancy）**

- **允许同一个线程多次获取同一把锁**（通过 `state` 计数器实现）。

  ```
  lock.lock();  // state=1
  lock.lock();  // state=2 (重入)
  lock.unlock(); // state=1
  lock.unlock(); // state=0
  ```

- **设计意图**：

  - 支持递归调用或嵌套同步代码块。
  - 避免线程因重复获取自己的锁而死锁。

------

**3. 公平性选择（Fairness vs. Non-Fairness）**

- **公平锁（Fair Lock）**：

  - 严格按照 CLH 队列顺序分配锁（先到先得）。
  - 通过 `new ReentrantLock(true)` 创建。

- **非公平锁（Non-Fair Lock）**：

  - 允许新请求的线程“插队”直接尝试 CAS 抢锁（默认模式，吞吐量更高）。

  ```
  public ReentrantLock() {
      sync = new NonfairSync(); // 默认非公平
  }
  ```

- **设计意图**：

  - **公平锁**：避免线程饥饿，保证公平性。
  - **非公平锁**：减少线程切换开销，提高性能。

------

**4. 支持条件变量（Condition）**

- **与 `Object.wait()`/`notify()` 对比**：

  - `synchronized` 只能绑定一个隐式条件队列。
  - `ReentrantLock` 可创建多个 `Condition` 对象，实现精细的线程等待/唤醒。

  ```
  Condition notEmpty = lock.newCondition();
  Condition notFull = lock.newCondition();
  ```

- **设计意图**：

  - 解决复杂线程协作问题（如生产者-消费者模型）。
  - 避免“虚假唤醒”（`Condition.await()` 严格绑定锁状态）。

------

**5. 可中断性与超时控制**

- **响应中断**：

  - `lockInterruptibly()` 允许在等待锁时响应中断。

  ```
  try {
      lock.lockInterruptibly(); // 可能抛出 InterruptedException
  } catch (InterruptedException e) {
      // 处理中断
  }
  ```

- **超时获取**：

  - `tryLock(long timeout, TimeUnit unit)` 支持限时等待。

  ```
  if (lock.tryLock(1, TimeUnit.SECONDS)) {
      try { /* 操作 */ }
      finally { lock.unlock(); }
  }
  ```

- **设计意图**：

  - 避免死锁或长时间阻塞。
  - 支持线程取消和资源回收。

------

**6. 基于 AQS 的模板方法模式**

- **AQS 的核心作用**：

  - `ReentrantLock` 将锁的 **竞争逻辑**（如公平/非公平）委托给内部类 `Sync`（继承自 `AbstractQueuedSynchronizer`）。

  - AQS 负责 **队列管理** 和 **阻塞/唤醒机制**，子类只需实现：

    ```
    protected boolean tryAcquire(int arg) { /* 锁获取逻辑 */ }
    protected boolean tryRelease(int arg) { /* 锁释放逻辑 */ }
    ```

- **设计意图**：

  - **分离变与不变**：AQS 处理通用的队列调度，`ReentrantLock` 专注锁策略。
  - **扩展性**：开发者可通过 AQS 自定义同步器（如 `Semaphore`、`CountDownLatch`）。

------

**7. 性能优化**

- **非公平锁的优化**：
  - 通过直接 CAS 尝试减少线程切换（即使队列中有等待线程，新线程仍有机会抢锁）。
- **自旋重试**：
  - 在加入 CLH 队列前短暂自旋，避免立即挂起线程。

------

**8. 与 JVM 的协同**

- **内存可见性**：
  - 通过 `volatile` 变量 `state` 和 AQS 的 CAS 操作保证可见性。
- **与 `synchronized` 的互补**：
  - `ReentrantLock` 提供高级功能，`synchronized` 更简洁（JVM 会优化内置锁）。

------

**总结：ReentrantLock 的设计哲学**

| 原则             | 实现方式                                  |
| :--------------- | :---------------------------------------- |
| **显式控制**     | 强制手动调用 `lock()`/`unlock()`          |
| **可重入**       | 通过 `state` 计数器实现                   |
| **公平性可选**   | 通过 `FairSync`/`NonfairSync` 策略类实现  |
| **条件变量支持** | 基于 `ConditionObject` 实现多条件队列     |
| **可中断与超时** | 提供 `lockInterruptibly()` 和 `tryLock()` |
| **AQS 模板方法** | 分离锁策略与队列管理                      |
| **性能优化**     | 非公平锁减少上下文切换，CAS 减少阻塞      |

`ReentrantLock` 的设计体现了 **灵活性** 和 **可控性** 的平衡，是对 `synchronized` 的补充而非替代，适用于需要精细控制锁行为的场景。

## LockSupport.park vs Object.wait vs Condition.await

**ReentrantLock 中的阻塞机制比较：LockSupport.park vs Object.wait vs Condition.await**

在 Java 并发编程中，`LockSupport.park`、`Object.wait` 和 `Condition.await` 都用于线程阻塞，但它们在实现机制和使用场景上有重要区别。下面我将详细比较这三者。

## 1. LockSupport.park

**特点**：

- 是 Java 并发包中最底层的线程阻塞原语
- 不需要获取任何锁就可以调用
- 与 `unpark` 配对使用，`unpark` 可以先于 `park` 调用
- 不响应中断，但会返回并设置中断状态
- 没有虚假唤醒问题

**在 ReentrantLock 中的应用**：

- AQS (AbstractQueuedSynchronizer) 使用 `LockSupport.park` 实现线程阻塞
- 当线程尝试获取锁失败时，会被放入等待队列并通过 `park` 阻塞

## 2. Object.wait

**特点**：

- 必须在 synchronized 块内使用
- 调用时会释放对象监视器锁
- 需要与 `notify`/`notifyAll` 配对使用
- 可能发生虚假唤醒
- 响应中断，会抛出 InterruptedException

**与 LockSupport.park 的区别**：

- `wait` 是高级 API，`park` 是底层实现
- `wait` 需要先获取锁，`park` 不需要
- `wait` 会释放锁，`park` 不会（因为它不持有锁）

## 3. Condition.await

**特点**：

- 必须在持有锁时调用（通过 Lock 对象）
- 调用时会释放锁
- 需要与 `signal`/`signalAll` 配对使用
- 可能发生虚假唤醒
- 响应中断，会抛出 InterruptedException

**与 Object.wait 的关系**：

- `Condition.await` 是 `Object.wait` 的替代品，专为 Lock 设计
- 一个 Lock 可以创建多个 Condition 对象，实现更精细的等待/通知机制

## 关键区别总结

| 特性           | LockSupport.park     | Object.wait              | Condition.await          |
| :------------- | :------------------- | :----------------------- | :----------------------- |
| 是否需要持有锁 | 不需要               | 需要(synchronized)       | 需要(通过Lock)           |
| 是否会释放锁   | 不适用               | 会释放                   | 会释放                   |
| 中断响应       | 不抛异常，返回状态   | 抛出InterruptedException | 抛出InterruptedException |
| 配对方法       | unpark               | notify/notifyAll         | signal/signalAll         |
| 虚假唤醒       | 无                   | 可能有                   | 可能有                   |
| 调用顺序限制   | unpark可先于park调用 | notify必须在wait后       | signal必须在await后      |

## 实现层次关系

在 ReentrantLock 的实现中：

1. `Condition.await` 最终会调用 `LockSupport.park` 来实现线程阻塞
2. `Object.wait` 是 JVM 层面的原生实现
3. `LockSupport.park` 是最底层的线程阻塞原语，其他高级 API 都是在其基础上构建的

## 使用建议

- 在自定义同步器时使用 `LockSupport.park`
- 在使用 synchronized 时使用 `Object.wait`
- 在使用 Lock 时使用 `Condition.await`
- 避免混用这些机制，保持一致性

理解这些区别有助于在并发编程中选择合适的线程阻塞机制。
