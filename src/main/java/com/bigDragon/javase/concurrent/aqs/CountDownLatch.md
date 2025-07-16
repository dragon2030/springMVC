# CountDownLatch

Latch /lætʃ/门闩

### **Java CountDownLatch 简单介绍**

`CountDownLatch` 是 Java 并发包 (`java.util.concurrent`) 中的 **同步辅助工具**，用于让 **一个或多个线程等待其他线程完成操作**。其核心思想是通过一个 **计数器** 实现线程间的协调。

------

## **1. 核心概念**

- **计数器（Count）**：
  - 初始化时指定一个正整数（如 `new CountDownLatch(3)`）。
  - 线程调用 `countDown()` 时，计数器减 1。
  - 当计数器归零时，所有等待的线程（调用 `await()` 的线程）被唤醒。
- **主要用途**：
  - 主线程等待多个子线程完成任务后再继续。
  - 多个子线程等待同一个启动信号后并行执行。

------

## **2. 基本用法**

### **初始化**

```
CountDownLatch latch = new CountDownLatch(3); // 计数器初始值为3
```

### **子线程完成任务后递减计数器**

```
new Thread(() -> {
    try {
        // 模拟任务执行
        Thread.sleep(1000);
        System.out.println("子线程完成任务");
    } finally {
        latch.countDown(); // 计数器减1
    }
}).start();
```

### **主线程等待所有子线程完成**

```
latch.await(); // 阻塞，直到计数器归零
System.out.println("所有子线程已完成，主线程继续执行");
```

------

## **3. 关键特性**

| 方法/特性                  | 说明                                                         |
| :------------------------- | :----------------------------------------------------------- |
| **`await()`**              | 阻塞当前线程，直到计数器归零。                               |
| **`await(timeout, unit)`** | 支持超时等待（若超时后计数器未归零，线程继续执行）。         |
| **`countDown()`**          | 递减计数器（线程安全，无需加锁）。                           |
| **一次性使用**             | 计数器归零后不可重置（如需重复使用，改用 `CyclicBarrier`）。 |

------

## **4. 底层实现**

- 基于 **AQS（AbstractQueuedSynchronizer）** 的共享模式：
  - 计数器值 = AQS 的 `state`。
  - `await()`：检查 `state` 是否为 0，否则加入队列阻塞。
  - `countDown()`：通过 CAS 减少 `state`，若归零则唤醒所有等待线程。

------

## **5. 代码示例**

### **场景1：主线程等待多个子线程**

```
CountDownLatch latch = new CountDownLatch(3);

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            System.out.println(Thread.currentThread().getName() + " 运行完成");
        } finally {
            latch.countDown();
        }
    }).start();
}

latch.await(); // 主线程等待
System.out.println("所有线程执行完毕");
```

### **场景2：子线程等待统一启动信号**

```
CountDownLatch startSignal = new CountDownLatch(1);

for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        try {
            startSignal.await(); // 等待主线程发出启动信号
            System.out.println(Thread.currentThread().getName() + " 开始执行");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }).start();
}

Thread.sleep(1000);
startSignal.countDown(); // 主线程释放启动信号
```

------

## **6. 与其他工具对比**

|                | CountDownLatch   | CyclicBarrier        | Semaphore          |
| :------------- | :--------------- | :------------------- | :----------------- |
| **用途**       | 等待多个任务完成 | 多个线程互相等待     | 限制资源并发访问数 |
| **可重置**     | 否               | 是                   | 是                 |
| **计数器方向** | 递减（归零触发） | 递增（达到阈值触发） | 增减（许可证管理） |

------

## **7. 适用场景**

- **批量任务并行处理**：主线程等待所有子任务完成后再汇总结果。
- **服务启动依赖**：确保所有服务初始化完成后再对外提供服务。
- **测试工具**：模拟高并发场景下线程的同步。

------

## **总结**

`CountDownLatch` 是 Java 并发编程中 **简洁高效的线程协调工具**，适用于“一等多”或“多等一”的场景。其设计轻量且线程安全，但需注意 **一次性使用** 的限制。对于需要重复同步的场景，可改用 `CyclicBarrier`。

# CountDownLatch 和 CompletableFuture 比较

`CountDownLatch` 和 `CompletableFuture` 虽然都能实现 **线程等待** 的功能，但它们的 **设计目的、使用场景和灵活性** 有显著差异。以下是详细对比：

------

### **1. 核心区别**

| **特性**     | **CountDownLatch**         | **CompletableFuture**                     |
| :----------- | :------------------------- | :---------------------------------------- |
| **设计目标** | 简单的线程等待/通知机制    | 异步任务编排（链式调用、组合、异常处理）  |
| **使用次数** | 一次性（计数器归零后失效） | 可重复使用（支持多次任务串联）            |
| **线程控制** | 需手动创建和管理线程       | 自动与线程池（如 `ForkJoinPool`）集成     |
| **结果传递** | 无返回值（仅通知完成）     | 可传递任务结果（通过 `get()` 或回调）     |
| **组合能力** | 不支持                     | 支持 `thenApply`/`thenCombine` 等复杂组合 |
| **异常处理** | 需自行捕获异常             | 内置 `exceptionally`/`handle` 方法        |

### **2. 适用场景对比**

#### **(1) 适合用 `CountDownLatch` 的情况**

- **简单同步**：等待多个独立任务完成，无需关心结果。

  ```
  // 主线程等待3个子线程完成
  CountDownLatch latch = new CountDownLatch(3);
  for (int i = 0; i < 3; i++) {
      new Thread(() -> {
          doWork();
          latch.countDown();
      }).start();
  }
  latch.await(); // 阻塞直到所有线程完成
  ```

#### **(2) 适合用 `CompletableFuture` 的情况**

- **复杂异步流水线**：需要组合多个任务的结果或处理异常。

  ```
  CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
  CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");
  
  // 合并两个任务的结果
  CompletableFuture<String> combined = future1.thenCombine(future2, (s1, s2) -> s1 + " " + s2);
  System.out.println(combined.get()); // 输出 "Hello World"
  ```

###  3.**为什么选择 `CompletableFuture`？**

- **功能强大**：支持任务串联、结果转换、异常处理。
- **代码简洁**：避免回调地狱（Callback Hell）。
- **线程池集成**：自动利用 `ForkJoinPool.commonPool()` 或自定义线程池。

------

### **4. 为什么选择 `CountDownLatch`？**

- **轻量级**：无额外开销，适合简单同步场景。
- **明确语义**：直观表达“等待N个事件完成”的逻辑。
- **低级别控制**：适用于需要手动管理线程的遗留代码。