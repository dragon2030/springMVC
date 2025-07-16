# Semaphore 

美
/ˈseməfɔːr/ （计）信号量

### **Java Semaphore 简单介绍**

`Semaphore`（信号量）是 Java 并发包 (`java.util.concurrent`) 中用于 **控制并发线程数量** 的同步工具，基于 **AQS（AbstractQueuedSynchronizer）** 实现。其核心思想是通过 **许可证（permits）** 限制对共享资源的访问。

------

## **1. 核心概念**

- **许可证（Permits）**：
  - Semaphore 维护一组虚拟的许可证数量。
  - 线程通过 `acquire()` 获取许可证，通过 `release()` 释放许可证。
  - 如果许可证耗尽，后续线程会被阻塞，直到有许可证被释放。
- **主要用途**：
  - 限制同时访问某资源的线程数（如数据库连接池）。
  - 实现生产者-消费者模型。
  - 替代 `synchronized` 的更灵活并发控制。

------

## **2. 基本用法**

### **初始化**

```
Semaphore semaphore = new Semaphore(3); // 允许最多3个线程同时访问
```

### **获取许可证（阻塞）**

```
semaphore.acquire(); // 获取1个许可证（如果无可用许可证，线程阻塞）
try {
    // 临界区代码（受保护的资源访问）
} finally {
    semaphore.release(); // 释放许可证
}
```

### **非阻塞尝试获取**

```
if (semaphore.tryAcquire()) { // 立即返回true/false
    try { /* 操作资源 */ }
    finally { semaphore.release(); }
} else {
    System.out.println("许可证不足，执行其他逻辑");
}
```

### **超时尝试获取**

```
if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) { // 等待1秒
    try { /* 操作资源 */ }
    finally { semaphore.release(); }
}
```

------

## **3. 关键特性**

| 特性         | 说明                                                         |
| :----------- | :----------------------------------------------------------- |
| **公平性**   | 支持公平模式（按申请顺序分配许可证）和非公平模式（默认）。   |
| **可中断**   | `acquire()` 可响应中断（抛出 `InterruptedException`）。      |
| **批量操作** | 支持一次性获取/释放多个许可证（如 `acquire(2)`）。           |
| **动态调整** | 运行时可通过 `reducePermits()` 或 `increasePermits()` 调整许可证数量。 |

------

## **4. 底层实现**

- 基于 **AQS 的共享模式**：
  - 许可证数量 = AQS 的 `state` 值。
  - `acquire()`：通过 CAS 减少 `state`，若不足则加入队列阻塞。
  - `release()`：增加 `state` 并唤醒等待线程。

------

## **5. 代码示例**

### **场景：限制10个线程同时下载文件**

```
Semaphore semaphore = new Semaphore(3); // 模拟3个下载通道

for (int i = 0; i < 10; i++) {
    new Thread(() -> {
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + " 开始下载...");
            Thread.sleep(2000); // 模拟下载耗时
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println(Thread.currentThread().getName() + " 下载完成！");
            semaphore.release();
        }
    }).start();
}
```

**输出**：
每次最多3个线程并行下载，其他线程等待。

------

## **6. 与 ReentrantLock 的区别**

|              | Semaphore                | ReentrantLock              |
| :----------- | :----------------------- | :------------------------- |
| **用途**     | 控制资源访问的线程数量   | 保护临界区（互斥访问）     |
| **许可证**   | 可多线程共享（共享模式） | 独占模式（仅一个线程持有） |
| **重入性**   | 不支持重入               | 支持重入                   |
| **条件变量** | 无                       | 可通过 `Condition` 实现    |

------

## **7. 适用场景**

- **资源池管理**（如数据库连接池、线程池）。
- **限流**（如接口每秒最多100次请求）。
- **多线程协作**（如生产者-消费者模型）。

------

## **总结**

`Semaphore` 是 Java 并发中 **轻量级的流量控制工具**，通过许可证机制灵活限制并发度，适合解决资源竞争和线程协作问题。其设计简单但功能强大，是 `synchronized` 和 `ReentrantLock` 的重要补充。