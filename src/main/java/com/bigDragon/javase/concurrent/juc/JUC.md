# JUC

**JUC（Java Util Concurrent）** 是 Java 提供的**并发编程工具包**（JDK 1.5 引入），旨在简化多线程开发，提供高性能、线程安全的并发工具类，避免开发者直接使用底层 `synchronized` 和 `wait/notify` 等复杂机制。

------

## **1. JUC 的核心组成**

JUC 主要包含以下模块：

### **（1）线程执行与管理（Executor Framework）**

- **`Executor`**：线程池顶层接口。
- **`ExecutorService`**：扩展 `Executor`，提供任务提交、关闭线程池等功能。
- **`ThreadPoolExecutor`**：标准线程池实现。
- **`ScheduledExecutorService`**：支持定时/周期性任务调度。
- **`ForkJoinPool`**（JDK7+）：分治任务并行计算框架（适合 CPU 密集型任务）。

[线程池](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\juc\Executor\Executor.md)

> 线程池提供了一种有效管理和复用线程的机制，避免了频繁创建和销毁线程带来的性能开销。JUC提供四种常用线程池实现，但生产中会用ThreadPoolExecutor构建线程池，核心参数解析——工作队列和拒绝策略

### **（2）并发集合（Concurrent Collections）**

- **`ConcurrentHashMap`**：高并发版 `HashMap`（分段锁/CAS 优化）。
- **`CopyOnWriteArrayList`**：读多写少的线程安全 `List`（写时复制）。
- **`BlockingQueue`**：阻塞队列（生产者-消费者模型）：
  - `ArrayBlockingQueue`（有界）、`LinkedBlockingQueue`（可选有界）
  - `PriorityBlockingQueue`（优先级队列）、`SynchronousQueue`（直接传递）

### **（3）原子类（Atomic Classes）**

- 基于 **CAS（Compare-And-Swap）** 实现无锁线程安全操作：
  - **基本类型**：`AtomicInteger`、`AtomicLong`、`AtomicBoolean`
  - **数组**：`AtomicIntegerArray`、`AtomicLongArray`
  - **引用类型**：`AtomicReference`、`AtomicStampedReference`（解决 ABA 问题）
  

### **（4）锁与同步工具（Locks & Synchronization Utilities）**

- **`Lock` 接口**：
  - **`ReentrantLock`**：可重入锁（替代 `synchronized`，支持公平/非公平锁）。
  - **`ReentrantReadWriteLock`**：读写分离锁（提高读多写少场景性能）。
- **`Condition`**：替代 `Object.wait()/notify()`，提供更灵活的线程等待/唤醒机制。
- **同步工具类**：
  - **`CountDownLatch`**：倒计时门闩（等待多个任务完成）。
  - **`CyclicBarrier`**：循环栅栏（多线程相互等待）。
  - **`Semaphore`**：信号量（控制并发线程数）。
  - **`Phaser`**（JDK7+）：更灵活的阶段同步器。

### **（5）其他工具**

- **`CompletableFuture`**（JDK8+）：异步编程（类似 Promise）。
- **`ForkJoinTask`**：分治任务（`RecursiveTask`、`RecursiveAction`）。

------

## **2. JUC 的核心优势**

| 特性         | 传统方式（synchronized/wait） | JUC                                      |
| :----------- | :---------------------------- | :--------------------------------------- |
| **锁机制**   | 内置锁（隐式）                | 显式锁（`Lock`）                         |
| **灵活性**   | 固定（非公平）                | 可公平/非公平（`ReentrantLock`）         |
| **性能**     | 悲观锁（重量级）              | CAS + 自旋（轻量级，如 `AtomicInteger`） |
| **功能扩展** | 有限                          | 丰富（读写锁、信号量、屏障等）           |
| **线程协作** | `wait()`/`notify()`           | `Condition`、`CountDownLatch` 等         |

------

## **3. 典型应用场景**

1. **高并发缓存**：`ConcurrentHashMap`
2. **任务调度**：`ScheduledThreadPoolExecutor`
3. **限流/资源控制**：`Semaphore`
4. **并行计算**：`ForkJoinPool`
5. **异步编程**：`CompletableFuture`

## **4. 示例代码**

### **（1）线程池（ExecutorService）**

```
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> System.out.println("Task running"));
executor.shutdown();
```

### **（2）ConcurrentHashMap**

```
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
map.put("key", 1);
map.computeIfAbsent("key", k -> 2); // 线程安全操作
```

### **（3）CountDownLatch（等待多线程完成）**

```
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        System.out.println("子线程执行");
        latch.countDown();
    }).start();
}
latch.await(); // 等待所有线程完成
System.out.println("主线程继续");
```

------

## **5. 总结**

- **JUC 是 Java 并发编程的工业级解决方案**，提供了比传统 `synchronized` 更高效、灵活的工具。

- **核心思想**：减少锁竞争（CAS、分段锁）、提高线程协作效率（`Lock` + `Condition`）。

- **学习建议**：先理解 `synchronized` 和 `volatile`，再逐步掌握 JUC 的各个组件。


掌握 JUC 是 Java 高级开发的必备技能，尤其在分布式、高并发场景下至关重要！ 🚀