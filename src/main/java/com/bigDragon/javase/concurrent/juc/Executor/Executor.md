# Java 线程池

线程池是Java多线程编程中的一个重要概念，它提供了一种有效管理和复用线程的机制，避免了频繁创建和销毁线程带来的性能开销。

## 为什么需要线程池

1. **降低资源消耗**：减少线程创建和销毁的开销
2. **提高响应速度**：任务到达时可以直接使用已有线程
3. **提高线程可管理性**：统一分配、调优和监控线程

## Java中的线程池实现

Java通过`java.util.concurrent.Executor`框架提供线程池支持，核心实现类是`ThreadPoolExecutor`。

### 主要线程池类型

1. **FixedThreadPool** - 固定大小线程池

   ```
   ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
   //源码
   public static ExecutorService newFixedThreadPool(int nThreads) {
       return new ThreadPoolExecutor(nThreads, nThreads,
                                     0L, TimeUnit.MILLISECONDS,
                                     new LinkedBlockingQueue<Runnable>(),
                                     Executors.defaultThreadFactory(), // 线程工厂
                                     defaultHandler);//拒绝策略
   }
   ```

   说明：

   提交一个任务创建一个线程，直到最大数。 线程池数一旦达到最大值就会保持不变。 若某线程因异常而结束，会有新线程替代。

   **特点**：

   - 固定数量的线程
   - 适用于负载较重的服务器
   - 无界队列（LinkedBlockingQueue）

2. **CachedThreadPool** - 可缓存线程池

   ```
   ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
   //源码
   public static ExecutorService newCachedThreadPool() {
       return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                     60L, TimeUnit.SECONDS,// 空闲线程存活时间// 时间单位
                                     new SynchronousQueue<Runnable>(),
                                     Executors.defaultThreadFactory(), // 线程工厂
                                     defaultHandler);//拒绝策略
   }
   ```

   说明

   若线程池的数量超过了处理任务所需要的线程，就回收部分空闲（默认60秒不执行任务）的线程。   当任务数增加时，此线程池又可以智能的添加新线程来处理任务。

   **特点**：

   - 线程数量根据需求自动调整
   - 空闲线程60秒后回收
   - 适用于执行很多短期异步任务
   - 同步队列（SynchronousQueue）

3. **SingleThreadExecutor** - 单线程线程池

   ```
   ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
   //源码
   public static ExecutorService newSingleThreadExecutor() {
       return new FinalizableDelegatedExecutorService
           (new ThreadPoolExecutor(1, 1,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>()),
                                   Executors.defaultThreadFactory(), // 线程工厂
                                   defaultHandler);//拒绝策略
   }
   ```

   说明：

   只一个线程工作，相当于单线程串行执行。 若此线程因异常而结束，会有新线程替代。 保证任务的执行顺序按任务提交顺序执行。

   **特点**：

   - 只有一个工作线程
   - 保证任务顺序执行
   - 无界队列（LinkedBlockingQueue）
   - 适用于需要保证任务顺序执行的场景

4. **ScheduledThreadPool** - 定时任务线程池

   ```
   ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
   //源码
   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
       return new ThreadPoolExecutor(
       	corePoolSize, 
       	Integer.MAX_VALUE, 
       	0, NANOSECONDS,
           new DelayedWorkQueue(),
           Executors.defaultThreadFactory(), defaultHandler);
   }
   ```

   说明

   支持定时以及周期性执行任务的需求。

   **特点**：

   - 用于定时或周期性任务
   - 可以设置初始延迟和间隔时间
   - 适用于定时任务、周期性任务场景

### 建议直接使用 `ThreadPoolExecutor` 构造线程池

**在生产环境中，建议直接使用 `ThreadPoolExecutor` 构造线程池**

阿里巴巴推荐使用 **ThreadPoolExecutor** 而不是 **Executors**，主要原因包括：

1. **避免 OOM**（无界队列或无限线程问题）16
2. **更灵活的配置**（核心线程数、队列、拒绝策略等）5
3. **更清晰的线程池行为**（开发者需明确参数含义）5
4. **适应不同业务场景**（IO/CPU 密集型任务优化）6

因此，**在生产环境中，建议直接使用 `ThreadPoolExecutor` 构造线程池**，而不是依赖 Executors 的快捷方法。

# ThreadPoolExecutor核心参数

```
public ThreadPoolExecutor(
    int corePoolSize,          // 核心线程数
    int maximumPoolSize,       // 最大线程数
    long keepAliveTime,        // 空闲线程存活时间
    TimeUnit unit,            // 时间单位
    BlockingQueue<Runnable> workQueue, // 工作队列
    ThreadFactory threadFactory,       // 线程工厂
    RejectedExecutionHandler handler   // 拒绝策略
)
```

## 工作流程

1. 提交任务时，如果当前线程数 < corePoolSize，创建新线程执行任务
2. 如果线程数 >= corePoolSize，将任务放入工作队列
3. 如果队列已满且线程数 < maximumPoolSize，创建新线程执行任务
4. 如果队列已满且线程数 = maximumPoolSize，执行拒绝策略

## 工作队列

线程池中的工作队列（`BlockingQueue`）对线程池的行为有重要影响，不同的队列类型会直接影响任务的执行策略和线程池的性能表现。

### 1. **ArrayBlockingQueue**

说明：

基于数组的FIFO队列；有界；创建时必须指定大小；     入队和出队共用一个可重入锁。默认使用非公平锁。

- **特点**：
  - **基于数组**的**有界**阻塞队列
  - 必须指定容量大小
  - 先进先出(FIFO)原则
  - 公平性可选（通过构造函数设置）
- **线程池影响**：
  - 当线程数达到corePoolSize且队列未满时，任务进入队列
  - 队列满且线程数未达maximumPoolSize时，创建新线程
  - 队列满且线程数已达maximumPoolSize时，触发拒绝策略
- **适用场景**：
  - 需要控制任务积压量的场景
  - 对内存使用有严格限制的环境

### 2. **LinkedBlockingQueue**

说明：

 基于链表的FIFO队列；有/无界；默认大小是 Integer.MAX_VALUE（无界），可自定义（有界）；     两个重入锁分别控制元素的入队和出队，用Condition进行线程间的唤醒和等待。     吞吐量通常要高于ArrayBlockingQueue。     默认大小的LinkedBlockingQueue将导致所有 corePoolSize 线程都忙时新任务在队列中等待。这样，创建的线程不会超过 corePoolSize。（因此，maximumPoolSize 的值也就无效了）。当每个任务相互独时，适合使用无界队列；例如， 在 Web 页服务器中。这种排队可用于处理瞬态突发请求，当命令以超过队列所能处理的平均数连续到达时，此策略允许无界线程具有增长的可能性。

- **特点**：

  - **基于链表**的可选有界/无界队列(默认无界)
  - 默认无界（Integer.MAX_VALUE）
  - 吞吐量通常高于ArrayBlockingQueue
  - 没有公平性选项

- **线程池影响**：

  - 无界版本可能导致内存溢出（Executors.newFixedThreadPool使用）
  - 有界版本行为类似ArrayBlockingQueue

- **适用场景**：

  - 任务量波动较大但需要平滑处理的场景
  - 需要高吞吐量的场景

  > **LinkedBlockingQueue与ArrayBlockingQueue主要区别**
  >
  > 1. **容量限制**：
  >    - `ArrayBlockingQueue`：必须有固定容量
  >    - `LinkedBlockingQueue`：默认无界（可指定容量）
  > 2. **实现方式**：
  >    - `ArrayBlockingQueue`：基于数组，预分配固定大小内存
  >    - `LinkedBlockingQueue`：基于链表，动态增长
  > 3. **锁机制**：
  >    - `ArrayBlockingQueue`：使用单个锁（put和take操作互相阻塞）
  >    - `LinkedBlockingQueue`：使用两个分离的锁（putLock和takeLock），吞吐量更高

### 3. **SynchronousQueue**

说明

SynchronousQueue（同步队列）是一种特殊的阻塞队列，它没有容量，每个插入操作必须等待一个对应的移除操作，反之亦然。

> 这里的"同步"不是指`synchronized`关键字那种线程同步，而是指生产者和消费者之间的操作同步

- 特点：
  1. **零容量**：不存储任何元素，只作为线程间传递元素的通道
  2. **直接传递**：生产者线程直接将元素交给消费者线程
  3. **两种模式**：
     - 公平模式（FIFO）：使用队列实现
     - 非公平模式（LIFO）：使用栈实现
  4. **高吞吐量**：由于直接传递，减少了数据拷贝和上下文切换
- 在线程池中的应用：
  - 常用于`Executors.newCachedThreadPool()`中
  - 适合处理大量短生命周期的异步任务
  - 当有新任务到来时，如果有空闲线程则立即执行，否则创建新线程
- **线程池影响**：
  - 任务不会被排队，而是直接尝试交给线程执行
  - 如果没有可用线程且未达maximumPoolSize，则创建新线程
  - 如果已达maximumPoolSize，则触发拒绝策略
  - Executors.newCachedThreadPool使用此队列

### 4. **PriorityBlockingQueue**

说明：

 基于链表的优先级队列；有/无界；默认大小是 Integer.MAX_VALUE，可自定义；     类似于LinkedBlockingQueue，但是其所含对象的排序不是FIFO，而是依据对象的自然顺序或者构造函数的Comparator决定。

- **特点**：
  - 无界优先级队列
  - 元素必须实现Comparable接口或提供Comparator
  - 不保证同优先级元素的顺序
- **线程池影响**：
  - 任务按优先级执行而非FIFO
  - 可能导致低优先级任务长时间得不到执行
- **适用场景**：
  - 需要任务优先级的场景
  - 紧急任务需要优先处理的系统

### 5. **DelayedWorkQueue**（ScheduledThreadPoolExecutor专用）

- **特点**：
  - 内部实现的无界优先级队列
  - 按延迟时间排序
  - 用于ScheduledThreadPoolExecutor
- **线程池影响**：
  - 支持定时和周期性任务
  - 任务按预定时间执行
- **适用场景**：
  - 定时任务调度
  - 周期性执行任务



## 拒绝策略

1. **AbortPolicy** - 默认策略，直接抛出RejectedExecutionException
2. **CallerRunsPolicy** - 由调用线程执行该任务
3. **DiscardPolicy** - 直接丢弃任务
4. **DiscardOldestPolicy** - 丢弃队列中最老的任务，然后重试

## 使用示例

```
// 创建线程池
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    2,  // 核心线程数
    4,  // 最大线程数
    60, // 空闲线程存活时间
    TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(10), // 任务队列容量
    new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
);

// 提交任务
for (int i = 0; i < 15; i++) {
    final int taskId = i;
    executor.execute(() -> {
        System.out.println("执行任务: " + taskId + ", 线程: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });
}

// 关闭线程池
executor.shutdown();
```

## 注意事项

1. 合理设置线程池大小（CPU密集型 vs IO密集型任务）
2. 避免使用无界队列，可能导致内存溢出
3. 正确关闭线程池（shutdown()和shutdownNow()的区别）
4. 考虑使用有界队列和合适的拒绝策略

线程池是Java并发编程中的重要工具，合理使用可以显著提高程序性能。

# 博客

[面试题-Java-阻塞队列(BlockingQueue)的用法(有实例)](https://way2j.com/a/523)

[面试题-Java线程池-种类(Executors的用法)](https://way2j.com/a/516)

[面试题-Java线程池-饱和策略(拒绝策略)的使用(有实例)](https://way2j.com/a/490)