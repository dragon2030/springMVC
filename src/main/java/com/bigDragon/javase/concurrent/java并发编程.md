# Java 并发编程详解

## 一、并发编程基础

### 1. 进程与线程

- **进程**：操作系统资源分配的基本单位，拥有独立的内存空间
- **线程**：CPU调度的基本单位，共享进程内存空间
- Java中每个线程有自己的程序计数器、虚拟机栈和本地方法栈

### 2. Java线程实现方式

#### (1) 继承Thread类

```
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
}
// 使用
new MyThread().start();
```

#### (2) 实现Runnable接口

```
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}
// 使用
new Thread(new MyRunnable()).start();
```

#### (3) 实现Callable接口（可返回结果）

```
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "Callable result";
    }
}
// 使用
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(new MyCallable());
String result = future.get(); // 阻塞获取结果
executor.shutdown();
```

#### (4) Lambda表达式（Java8+）

```
new Thread(() -> System.out.println("Lambda thread")).start();
```

## 二、线程生命周期与管理

### 1. 线程状态

- **NEW**：新建未启动
- **RUNNABLE**：可运行（包括就绪和运行中）
- **BLOCKED**：等待监视器锁
- **WAITING**：无限期等待
- **TIMED_WAITING**：有限期等待
- **TERMINATED**：终止

### 2. 线程控制方法

- `start()`：启动线程

- `sleep(long millis)`：线程休眠

- `yield()`：让出CPU时间

- `join()`：等待线程终止

  > 会使主线程(或者说调用t.join()的线程)进入等待池并等待t线程执行完毕后才会被唤醒。

- `interrupt()`：中断线程

- `setDaemon(true)`：设置为守护线程

  > [Java 守护线程（Daemon Thread）详解](#Java 守护线程（Daemon Thread）详解)

## 三、线程同步机制

### 1. synchronized关键字

```
// 同步方法
public synchronized void syncMethod() { /*...*/ }

// 同步代码块
public void syncBlock() {
    synchronized(this) { /*...*/ }
}

// 静态同步方法（类锁）
public static synchronized void staticSync() { /*...*/ }
```

> [synchronized](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\synchronized1/synchronized.md)
>
> > `synchronized` 是 Java 中实现线程同步的关键机制，它能够保证多线程环境下的可见性、原子性和有序性。对象头Mark Word记录锁信息。Monitor 重量级锁，锁的升级膨胀，synchronized等待唤醒机制，synchronized可重入性，synchronized非公平性
>
> [虚假唤醒](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\createThread\虚假唤醒.md)
>
> > 虚假唤醒是指线程在没有收到明确的唤醒通知(如notify/notifyAll)的情况下，从wait()状态中被唤醒的现象。需要使用while循环而不是if语句检查条件
>
> java中 wait和sleep的区别
>
> [wait和sleep](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\createThread/wait和sleep.md)
>
> [Java 线程中断机制](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\createThread\interrupt\线程中断.md)
>
> > Java 中的线程中断是一种协作机制，用于通知线程应该停止当前正在做的事情。它不是强制立即终止线程，而是通过设置中断标志来请求线程自行停止。线程阻塞时中断抛出异常，并清除中断状态；线程运行时，需要通过检查中断状态感应是否中断。非强制中断可以提供灵活的控制终端方式

### 2. Lock接口

```
Lock lock = new ReentrantLock();
lock.lock();
try {
    // 临界区代码
} finally {
    lock.unlock();
}

// 条件变量
Condition condition = lock.newCondition();
condition.await(); // 等待
condition.signal(); // 唤醒
```

> [ReentrantLock](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\reentrantLock\ReentrantLock.md)
>
> > `ReentrantLock` 是 Java 并发包中的一个可重入互斥锁实现，它提供了比内置 `synchronized` 关键字更灵活的锁操作。特性：可重入性、可中断、公平性选择、条件变量支持、原子性、可见性、有序性保证，核心方法lock、unlock。AQS是ReentrantLock基类，提供独占模式模板方法
>
> [AQS](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\reentrantLock\AQS.md)
>
> > AQS (AbstractQueuedSynchronizer) 是 Java 并发包中一个核心的同步器框架，位于 `java.util.concurrent.locks` 包下。它为构建锁和同步器提供了基础框架，许多重要的并发工具如 ReentrantLock、Semaphore、CountDownLatch 等都是基于 AQS 实现的。主要特点:同步状态管理、等待队列与节点、模板方法模式。独占模式核心方法：acquire获取独占锁、release释放独占锁、tryAcquire尝试获取独占锁（需重写）、tryRelease尝试释放独占锁（需重写）
>

### 3. volatile关键字

保证变量可见性，但不保证原子性

```
private volatile boolean flag = true;
```

[volatile](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\volatile\volatile.md)

> volatile是Java中的一个关键字，用于修饰变量，主要解决多线程环境下的可见性和有序性问题，但是不保证原子。需要注意：volatile与synchronized的区别。volatile的底层实现主要通过内存屏障和缓存一致性协议

[JMM Java内存模型](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\jmm\JMM.md)

> Java内存模型是Java虚拟机对多线程程序中的内存访问和操作进行规范的一种抽象，并不真实存在。它定义了线程如何与主存（共享内存）和工作内存（线程私有内存）进行交互，以及如何同步和互斥地访问共享数据。三大特性:原子性、可见性、有序性。内存结构：主内存、工作内存。四种抽象的内存屏障，用于控制指令重排序和内存可见性。happens-before，定义了操作之间的偏序关系，确保一个操作的结果对另一个操作可见。

### 4. 原子类

```
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet(); // 原子自增
counter.compareAndSet(expect, update); // CAS操作
```

> [CAS](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\juc\cas\cas.md)
>
> > CAS (Compare-And-Swap) 是Java中一种重要的无锁并发编程技术，它通过硬件级别的原子操作实现线程安全，避免了传统锁机制的开销。Java中的CAS实现：Unsafe类和atomic原子类实现。CAS底层实现。解决高竞争下CAS自旋消耗CPU的方案

## 四、并发工具类

JUC

> [JUC](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\juc\JUC.md)

### 1. Executor框架

```
// 创建线程池
ExecutorService executor = Executors.newFixedThreadPool(5);

// 提交任务
executor.execute(() -> {...}); // Runnable
Future<String> future = executor.submit(() -> "result"); // Callable

// 关闭线程池
executor.shutdown();
```

> [Java线程池](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\threadPoolExecutor\Java线程池.md)
>
> 线程池是Java多线程编程中的一个重要概念，它提供了一种高效管理和复用线程的机制，避免了频繁创建和销毁线程带来的性能开销。核心优势：降低资源消耗、提高响应速度、提高线程可管理性。核心类：Executors工厂类和ThreadPoolExecutor类。需要掌握工作流程，拒绝策略，任务队列

### 2. 同步辅助类

#### CountDownLatch

```
CountDownLatch latch = new CountDownLatch(3);
// 工作线程
new Thread(() -> {
    // 工作...
    latch.countDown();
}).start();
// 主线程等待
latch.await();
```

> [CountDownLatch](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\aqs\CountDownLatch.md)
>
> > `CountDownLatch` （Latch /lætʃ/门闩） 是 Java 并发包 (`java.util.concurrent`) 中的 **同步辅助工具**，用于让 **一个或多个线程等待其他线程完成操作**。其核心思想是通过一个 **计数器** 实现线程间的协调。主要用途:主线程等待多个子线程完成任务后再继续/多个子线程等待同一个启动信号后并行执行。和CompletableFuture 功能相似，CountDownLatch更轻量级，适合简单同步场景

#### CyclicBarrier

```
CyclicBarrier barrier = new CyclicBarrier(3, () -> 
    System.out.println("所有线程到达屏障"));
// 工作线程
new Thread(() -> {
    // 工作...
    barrier.await();
}).start();
```

> [CyclicBarrier](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\aqs\CyclicBarrier.md)
>
> `CyclicBarrier` （cyclic  美 /ˈsaɪklɪk/ 循环的 Barrier 美 /ˈbæriər/ 屏障）是 Java 并发包 (`java.util.concurrent`) 中的一个同步辅助类，它允许一组线程互相等待，直到所有线程都到达某个公共屏障点（barrier point）后再继续执行。主要方法：构造器设定屏障值CyclicBarrier(int parties)，await()等待直到所有线程都到达屏障点

#### Semaphore

```
Semaphore semaphore = new Semaphore(5); // 5个许可
semaphore.acquire(); // 获取许可
try {
    // 访问资源
} finally {
    semaphore.release(); // 释放许可
}
```

> [Semaphore](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\aqs\Semaphore.md)
>
> `Semaphore`（信号量）是 Java 并发包 (`java.util.concurrent`) 中用于 **控制并发线程数量** 的同步工具，基于 **AQS（AbstractQueuedSynchronizer）** 实现。其核心思想是通过 **许可证（permits）** 限制对共享资源的访问。线程通过 `acquire()` 获取许可证，通过 `release()` 释放许可证。如果许可证耗尽，后续线程会被阻塞，直到有许可证被释放。主要用途:限制同时访问某资源的线程数（如数据库连接池）

### 3. 并发集合

- `ConcurrentHashMap`：线程安全的HashMap
- `CopyOnWriteArrayList`：线程安全的ArrayList
- `BlockingQueue`：阻塞队列
  - `ArrayBlockingQueue`
  - `LinkedBlockingQueue`
  - `PriorityBlockingQueue`
  - `SynchronousQueue`

## 五、高级并发特性

### 1. CompletableFuture (Java8+)

```
CompletableFuture.supplyAsync(() -> "Hello")
    .thenApplyAsync(s -> s + " World")
    .thenAcceptAsync(System.out::println)
    .exceptionally(ex -> {
        System.out.println("Error: " + ex);
        return null;
    });
```

> [CompletableFuture](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\future\completableFuture\completableFuture.md)
>
> CompletableFuture  是 Java 8 引入的一个强大的异步编程工具，它是 Future 接口的增强实现，提供了更丰富的功能来处理异步计算和组合多个异步操作。CompletableFuture处理任务之间的逻辑关系,编排好任务的执行方式后，任务会按照规划好的方式一步一步执行，不需要让业务线程去频繁的等待。允许串行、并行、任一的指定执行方式，有全局结果和异常处理
>
> [回调地狱](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\future\completableFuture\callbackHell.md)
>
> 回调地狱（Callback Hell）是指在异步编程中，由于多层嵌套的回调函数导致的代码难以理解和维护的情况。回调函数:当一个函数作为参数传入另一个参数中，并且它不会立即执行，只有当满足一定条件后该函数才可以执行。异步任务：不进入主线程，而是进入异步队列，前一个任务是否执行完毕不影响下一个任务的执行。回调地狱的问题：代码可读性差、错误处理困难、维护困难、调试困难。Java 8引入CompletableFuture优雅异步编程方式解决回调地狱

### 2. Fork/Join框架

```
class FibonacciTask extends RecursiveTask<Integer> {
    final int n;
    FibonacciTask(int n) { this.n = n; }
    
    protected Integer compute() {
        if (n <= 1) return n;
        FibonacciTask f1 = new FibonacciTask(n - 1);
        f1.fork();
        FibonacciTask f2 = new FibonacciTask(n - 2);
        return f2.compute() + f1.join();
    }
}

ForkJoinPool pool = new ForkJoinPool();
int result = pool.invoke(new FibonacciTask(10));
```

>[Fork/Join框架](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\juc\ForkJoin\ForkJoin框架.md)
>
>Fork/Join框架是Java 7引入的一个用于并行执行任务的框架，Java并发编程中的一个强大工具，特别适合处理可分解的CPU密集型任务.核心概念:分而治之：将大任务拆分成小任务，直到任务足够小可以直接解决,工作窃取：空闲线程可以从其他线程的任务队列中"窃取"任务执行，提高CPU利用率

### 3. ThreadLocal

```
ThreadLocal<SimpleDateFormat> dateFormat = 
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
String date = dateFormat.get().format(new Date());
```

> [ThreadLocal](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\threadLocal\threadLocal.md)
>
> ThreadLocal 是 Java 中一个特殊的类，它提供了线程局部变量。这些变量不同于普通的变量，因为每个访问该变量的线程都有自己独立初始化的变量副本，线程之间互不干扰,从而隔离了多个线程对数据的访问冲突，避免多线程环境下的共享变量竞争问题。Hash冲突解决：线性探测法。内存泄漏本质：**Key是弱引用会被GC自动回收，但Value是强引用会一直存活，导致Entry无法被清除**，最终造成内存泄漏，所以需要手动remove。

## 六、并发编程最佳实践

1. **优先使用线程池**而非直接创建线程
2. **减少锁粒度**，避免大范围同步
3. **使用不可变对象**简化线程安全
4. **避免死锁**：按固定顺序获取多个锁
5. **注意资源竞争**，使用适当的同步机制
6. **考虑使用并发集合**而非同步包装类
7. **合理设置线程池参数**（核心线程数、最大线程数、队列容量等）
8. **避免线程泄漏**，确保线程能够正常结束
9. **使用volatile或原子变量**替代简单同步
10. **考虑使用更高层次的并发抽象**（如Fork/Join、CompletableFuture）

## 七、常见并发问题

1. **竞态条件**：多个线程访问共享数据时结果依赖于执行时序
2. **死锁**：多个线程互相持有对方需要的锁
3. **活锁**：线程不断重试失败的操作
4. **线程饥饿**：某些线程长期得不到执行机会
5. **内存可见性问题**：一个线程的修改对另一个线程不可见

Java并发编程是构建高性能应用的重要技能，合理使用可以显著提高程序效率，但需要深入理解各种并发机制的特性与适用场景。



# 其他

# Java 守护线程（Daemon Thread）详解

定义

守护线程是一种特殊的线程，它在后台运行，为其他线程（用户线程）提供服务。当所有用户线程结束时，无论守护线程是否执行完毕，JVM都会自动退出。

核心特点

1. **服务性质**：通常用于执行后台支持任务（如垃圾回收、内存管理等）
2. **自动终止**：当所有用户线程结束时，守护线程会被强制终止
3. **低优先级**：通常比用户线程的优先级低

守护线程 vs 用户线程

| 特性         | 守护线程                   | 用户线程                   |
| :----------- | :------------------------- | :------------------------- |
| JVM退出影响  | 不会阻止JVM退出            | 会阻止JVM退出              |
| 默认类型     | 否（默认都是用户线程）     | 是                         |
| 用途         | 后台服务                   | 程序主要业务逻辑           |
| 子线程继承性 | 子线程继承父线程的守护状态 | 子线程继承父线程的守护状态 |

使用方法

1. 设置守护线程

```
Thread daemonThread = new Thread(() -> {
    while (true) {
        System.out.println("守护线程运行中...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
});

// 必须在start()前设置为守护线程
daemonThread.setDaemon(true); 
daemonThread.start();
```

2. 检查线程类型

```
System.out.println("是否是守护线程: " + Thread.currentThread().isDaemon());
```

典型应用场景

1. **垃圾回收线程**：JVM的GC线程就是守护线程
2. **心跳检测**：定期检查系统状态的线程
3. **日志记录**：后台异步记录日志
4. **监控线程**：系统性能监控
5. **自动保存**：定期保存数据的线程

注意事项

1. **设置时机**：必须在调用`start()`方法前设置`setDaemon(true)`，否则会抛出`IllegalThreadStateException`
2. **finally块**：守护线程中的finally块不一定能执行（因为JVM可能直接退出）
3. **资源释放**：不适合用于需要确保资源释放的场景
4. **I/O操作**：守护线程进行I/O操作时可能被突然终止
5. **子线程继承**：由守护线程创建的线程默认也是守护线程