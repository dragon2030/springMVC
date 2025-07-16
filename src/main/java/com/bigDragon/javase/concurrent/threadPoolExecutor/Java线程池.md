# Java线程池介绍

【源码解析看runWorker 看不下去了 以后有精力再看吧】

线程池是Java多线程编程中的一个重要概念，它提供了一种高效管理和复用线程的机制，避免了频繁创建和销毁线程带来的性能开销。

## 线程池的核心优势

1. **降低资源消耗**：通过复用已创建的线程，减少线程创建和销毁的开销
2. **提高响应速度**：任务到达时可以直接使用已有线程，无需等待线程创建
3. **提高线程可管理性**：可以统一分配、调优和监控线程资源

## Java线程池核心类

Java通过`java.util.concurrent.Executors`工厂类和`ThreadPoolExecutor`类提供了线程池支持。

### 主要接口和类

- `Executor`：线程池的顶级接口
- `ExecutorService`：扩展了Executor，增加了管理线程池生命周期的方法
- `ThreadPoolExecutor`：最常用的线程池实现类
- `ScheduledThreadPoolExecutor`：支持定时和周期性任务执行的线程池

## 线程池的创建

Java提供了几种常见的线程池创建方式：

### 1. 通过Executors工厂类创建

```
// 固定大小的线程池
ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

// 单线程线程池
ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

// 可缓存的线程池（根据需要创建新线程）
ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

// 定时任务线程池
ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
```

### 2. 直接创建ThreadPoolExecutor

```
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    corePoolSize,  // 核心线程数
    maximumPoolSize,  // 最大线程数
    keepAliveTime,  // 非核心线程空闲存活时间
    TimeUnit.MILLISECONDS,  // 时间单位
    new LinkedBlockingQueue<Runnable>()  // 任务队列
);
```

## 线程池关键参数

`ThreadPoolExecutor`的核心构造参数：

1. **corePoolSize**：核心线程数，即使空闲也会保留的线程数量
2. **maximumPoolSize**：线程池允许的最大线程数
3. **keepAliveTime**：非核心线程空闲时的存活时间
4. **unit**：keepAliveTime的时间单位
5. **workQueue**：用于保存等待执行的任务的阻塞队列
6. **threadFactory**：用于创建新线程的工厂
7. **handler**：当线程池和队列都满时的拒绝策略

## 线程池工作流程

1. 提交任务时，如果当前线程数 < corePoolSize，则创建新线程执行任务
2. 如果线程数 ≥ corePoolSize，则将任务放入工作队列
3. 如果队列已满且线程数 < maximumPoolSize，则创建新线程执行任务
4. 如果队列已满且线程数 = maximumPoolSize，则执行拒绝策略

## 拒绝策略

当线程池无法处理新任务时，会触发拒绝策略：

1. `ThreadPoolExecutor.AbortPolicy`：默认策略，抛出RejectedExecutionException
2. `ThreadPoolExecutor.CallerRunsPolicy`：由调用线程执行该任务
3. `ThreadPoolExecutor.DiscardPolicy`：直接丢弃任务，不抛异常
4. `ThreadPoolExecutor.DiscardOldestPolicy`：丢弃队列中最老的任务，然后重试

## 任务队列

Java线程池中的任务队列（workQueue）是决定线程池行为特性的关键参数之一。不同的队列实现会直接影响线程池的任务调度策略和性能表现。下面我将详细比较各种队列实现的特性及适用场景。

## 队列类型总览

Java线程池常用的阻塞队列主要有以下几种：

| 队列实现类            | 特性描述               | 有界性 | 适用场景         |
| :-------------------- | :--------------------- | :----- | :--------------- |
| LinkedBlockingQueue   | 链表结构阻塞队列       | 可选   | 通用场景         |
| ArrayBlockingQueue    | 数组结构阻塞队列       | 有界   | 固定大小队列需求 |
| SynchronousQueue      | 不存储元素的阻塞队列   | 特殊   | 高吞吐量场景     |
| PriorityBlockingQueue | 带优先级的无界阻塞队列 | 无界   | 任务优先级调度   |
| DelayedWorkQueue      | 延迟队列（内部实现）   | 无界   | 定时线程池专用   |

> [Java线程池任务队列选择对比分析](# Java线程池任务队列选择对比分析)

## 使用示例

```
public class ThreadPoolExample {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // 提交任务
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("执行任务 " + taskId + "，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        // 关闭线程池
        executor.shutdown();
    }
}
```

## 线程池关闭

- `shutdown()`：平滑关闭，不再接受新任务，但会执行完已提交的任务
- `shutdownNow()`：立即关闭，尝试停止所有正在执行的任务，返回等待执行的任务列表

## 最佳实践

1. 根据任务类型选择合适的线程池
2. 避免使用无界队列，防止内存溢出
3. 合理设置线程池大小（CPU密集型任务：N+1；IO密集型任务：2N）
4. 为线程池设置有意义的名称，便于排查问题
5. 考虑使用自定义的ThreadFactory设置线程优先级和名称

# Java线程池原理解析

Java线程池是Java并发编程中的核心组件，其内部工作机制值得深入理解。下面我将从设计思想、核心实现和运行机制三个方面进行详细解析。

## 线程池的设计思想

线程池基于"池化"思想，主要解决两个问题：

1. **线程生命周期开销**：频繁创建/销毁线程代价高
2. **资源管理问题**：无限制创建线程可能导致系统资源耗尽

线程池通过"线程复用+任务队列"的方式实现高效的任务执行。

## 核心字段解析

```java
// 控制字段：高3位表示线程池状态，低29位表示工作线程数
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

// 工作线程集合，只有在持有mainLock时才能访问
private final HashSet<Worker> workers = new HashSet<Worker>();

// 任务队列
private final BlockingQueue<Runnable> workQueue;

// 全局锁，用于控制workers集合的访问
private final ReentrantLock mainLock = new ReentrantLock();

// 用于支持awaitTermination的条件队列
private final Condition termination = mainLock.newCondition();

// 记录线程池达到的最大线程数
private int largestPoolSize;

// 已完成任务计数器
private long completedTaskCount;

// 线程工厂
private volatile ThreadFactory threadFactory;

// 拒绝策略处理器
private volatile RejectedExecutionHandler handler;

// 非核心线程空闲存活时间
private volatile long keepAliveTime;

// 是否允许核心线程超时
private volatile boolean allowCoreThreadTimeOut;

// 核心线程数
private volatile int corePoolSize;

// 最大线程数
private volatile int maximumPoolSize;
```

### 线程池状态管理

线程池使用一个AtomicInteger变量(ctl)同时维护两个信息：

- **workerCount**：当前有效线程数（低29位）
- **runState**：线程池运行状态（高3位）

```
private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
```

> [线程池状态编码原理深度解析](#线程池状态编码原理深度解析)

线程池的5种状态：

- **RUNNING**：-1 << 29,接受新任务并处理队列任务

- **SHUTDOWN**：0 << 29,不接受新任务，但处理队列任务

- **STOP**：1 << 29,不接受新任务，不处理队列任务，中断进行中的任务

- **TIDYING**：2 << 29,所有任务已终止，workerCount=0

  > Tidying 美  /ˈtaɪdiɪŋ/

- **TERMINATED**：3 << 29,terminated()方法已执行

状态转换：
RUNNING -> SHUTDOWN -> STOP -> TIDYING -> TERMINATED

## ThreadPoolExecutor构造器解析

ThreadPoolExecutor提供了4个构造器，最终都会调用最完整的7参数构造器：

```
public ThreadPoolExecutor(int corePoolSize,
                         int maximumPoolSize,
                         long keepAliveTime,
                         TimeUnit unit,
                         BlockingQueue<Runnable> workQueue,
                         ThreadFactory threadFactory,
                         RejectedExecutionHandler handler) {
    // 参数校验
    if (corePoolSize < 0 ||
        maximumPoolSize <= 0 ||
        maximumPoolSize < corePoolSize ||
        keepAliveTime < 0)
        throw new IllegalArgumentException();
    
    // 空指针检查
    if (workQueue == null || threadFactory == null || handler == null)
        throw new NullPointerException();
    
    // 初始化核心字段
    this.corePoolSize = corePoolSize;
    this.maximumPoolSize = maximumPoolSize;
    this.workQueue = workQueue;
    this.keepAliveTime = unit.toNanos(keepAliveTime);
    this.threadFactory = threadFactory;
    this.handler = handler;
}
```

**关键参数说明：**

1. **corePoolSize**：核心线程数，即使空闲也会保留的线程数量
2. **maximumPoolSize**：线程池允许的最大线程数
3. **keepAliveTime**：非核心线程空闲时的存活时间
4. **unit**：keepAliveTime的时间单位
5. **workQueue**：任务队列，保存等待执行的任务
6. **threadFactory**：用于创建新线程的工厂
7. **handler**：拒绝策略处理器

## 二、核心方法解析

### 1. execute() 方法

```java
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();
    
    /*
     * 执行流程分为三步：
     * 1. 如果运行的线程少于corePoolSize，尝试创建新线程
     * 2. 如果任务可以成功入队，仍需要双重检查是否应该创建新线程
     * 3. 如果无法入队，尝试创建新线程，如果失败则执行拒绝策略
     */
    
    int c = ctl.get();
    
    // 阶段1：当前线程数 < corePoolSize
    if (workerCountOf(c) < corePoolSize) {
        // 尝试创建新线程（核心线程）
        if (addWorker(command, true))
            return;
        // 如果创建失败，重新获取控制状态
        c = ctl.get();
    }
    
    // 阶段2：线程数 >= corePoolSize，尝试入队
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        // 双重检查：如果线程池已停止，回滚入队操作
        if (! isRunning(recheck) && remove(command))
            reject(command);
        // 如果工作线程数为0，创建新线程（应对所有核心线程被回收的情况）
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    // 阶段3：队列已满，尝试创建非核心线程
    else if (!addWorker(command, false))
        // 创建失败（达到maximumPoolSize），执行拒绝策略
        reject(command);
}
```

#### 2. addWorker() 方法

`addWorker`方法主要完成两个核心功能：

1. **创建工作线程**：创建一个新的`Worker`对象（包含一个Thread实例）
2. **启动工作线程**：启动Worker内部的线程开始执行任务

同时它还负责：

- 线程池状态的检查
- 工作线程数的控制（通过CAS保证原子性）
- 线程工厂的使用
- 异常情况下的回滚处理

参数说明：

- `firstTask`：新线程应该首先执行的任务（可能为null）
- `core`：是否创建核心线程（决定使用corePoolSize还是maximumPoolSize作为限制）

返回值：

- true：worker创建并启动成功
- false：创建失败（由于线程池状态或容量限制）

```java
private boolean addWorker(Runnable firstTask, boolean core) {
	//------------阶段1：状态和线程数检查（retry循环）------------
    retry:
    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);
        
        /*
         * 检查线程池状态是否允许创建worker：
         * 1. 如果状态 >= SHUTDOWN（非RUNNING）
         * 2. 且不满足：状态==SHUTDOWN && firstTask==null && 队列非空
         * 则不允许创建worker
         */
        if (rs >= SHUTDOWN &&
            ! (rs == SHUTDOWN && firstTask == null && ! workQueue.isEmpty()))
            return false;
            
        for (;;) {
            int wc = workerCountOf(c);
            // 检查线程数是否超过容量限制
            if (wc >= CAPACITY ||
                wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
            // CAS增加worker计数
            if (compareAndIncrementWorkerCount(c))
                break retry;
            // 如果CAS失败，重新检查状态
            c = ctl.get();
            if (runStateOf(c) != rs)
                continue retry;
        }
    }
    
    //------------阶段2：创建和启动Worker------------
    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
        // 1. 创建Worker对象（包含Thread实例）
        w = new Worker(firstTask);
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                // 2. 再次检查线程池状态
                int rs = runStateOf(ctl.get());
                if (rs < SHUTDOWN ||
                    (rs == SHUTDOWN && firstTask == null)) {
                    if (t.isAlive()) // 预检查线程是否已启动
                        throw new IllegalThreadStateException();
                    // 3. 将worker加入集合
                    workers.add(w);
                    workerAdded = true;
                    // 4. 更新最大线程数统计
                    int s = workers.size();
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
            	// 5. 启动线程
                t.start(); 
                workerStarted = true;
            }
        }
    } finally {
        // 6. 失败回滚
        if (! workerStarted)
            addWorkerFailed(w); 
    }
    return workerStarted;
}
```

**阶段1：状态和线程数检查（retry循环）关键点**：

- 状态检查的三重条件：
  - 状态≥SHUTDOWN（非RUNNING）
  - 且不满足：状态正好是SHUTDOWN + firstTask为null + 队列非空
  - 这个条件允许在SHUTDOWN状态下继续处理队列中的任务
- 线程数检查：
  - 不能超过总容量CAPACITY（低29位最大值）
  - 根据core参数决定使用corePoolSize还是maximumPoolSize作为上限
- 通过CAS保证workerCount的原子性增加

 **阶段2：创建和启动Worker关键点**：

1. Worker构造：
   - 封装firstTask和新建Thread
   - Thread通过线程工厂创建
2. 双重检查：
   - 加锁后再次检查线程池状态
   - 防止状态在检查后发生变化
3. workers集合：
   - 使用mainLock保证线程安全
   - 只有成功加入集合才算添加成功
4. 统计信息：
   - 维护largestPoolSize记录峰值线程数
5. 线程启动：
   - 调用Thread.start()实际启动线程
   - 线程执行Worker.run()方法
6. 失败处理：
   - 回滚worker计数
   - 从workers集合移除
   - 尝试终止线程池

##### 3. Worker类解析

Worker类是ThreadPoolExecutor的内部类

```
private final class Worker
    extends AbstractQueuedSynchronizer
    implements Runnable
```

```
private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
    // 实际执行任务的线程
    final Thread thread;
    // 初始任务（可能为null）
    Runnable firstTask;
    // 当前worker完成的任务数
    volatile long completedTasks;
    
    Worker(Runnable firstTask) {
        // 禁止中断直到runWorker
        setState(-1);
        this.firstTask = firstTask;
        // 通过线程工厂创建新线程
        this.thread = getThreadFactory().newThread(this);
    }
    
    public void run() {
        runWorker(this);
    }
    
    // 以下实现简单的不可重入锁
    protected boolean isHeldExclusively() {
        return getState() != 0;
    }
    
    protected boolean tryAcquire(int unused) {
        if (compareAndSetState(0, 1)) {
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }
    
    protected boolean tryRelease(int unused) {
        setExclusiveOwnerThread(null);
        setState(0);
        return true;
    }
    
    public void lock()        { acquire(1); }
    public boolean tryLock()  { return tryAcquire(1); }
    public void unlock()      { release(1); }
    public boolean isLocked() { return isHeldExclusively(); }
    
    // 中断worker线程（如果正在运行）
    void interruptIfStarted() {
        Thread t;
        if (getState() >= 0 && (t = thread) != null && !t.isInterrupted()) {
            try {
                t.interrupt();
            } catch (SecurityException ignore) {
            }
        }
    }
}
```

##### 4. runWorker() 方法

```
final void runWorker(Worker w) {
    Thread wt = Thread.currentThread(); // 获取当前工作线程引用
    Runnable task = w.firstTask;
    w.firstTask = null;
    w.unlock(); // 调用unlock()将AQS状态从-1改为0，允许中断
    boolean completedAbruptly = true;
    try {
        // 循环获取任务（从队列获取或使用初始任务）
        while (task != null || (task = getTask()) != null) {
            w.lock();
            /*
             * 如果线程池正在停止，确保线程被中断
             * 如果没有停止，确保线程没有被中断
             * 这需要在clearInterruptsForTaskRun检查中重新检查
             */
            if ((runStateAtLeast(ctl.get(), STOP) ||
                 (Thread.interrupted() &&
                  runStateAtLeast(ctl.get(), STOP))) &&
                !wt.isInterrupted())
                wt.interrupt();
            try {
                beforeExecute(wt, task); // 前置钩子
                Throwable thrown = null;
                try {
                    task.run(); // 实际执行任务
                } catch (RuntimeException x) {
                    thrown = x; throw x;
                } catch (Error x) {
                    thrown = x; throw x;
                } catch (Throwable x) {
                    thrown = x; throw new Error(x);
                } finally {
                    afterExecute(task, thrown); // 后置钩子
                }
            } finally {
                task = null;
                w.completedTasks++;
                w.unlock();
            }
        }
        completedAbruptly = false;
    } finally {
        // 处理worker退出（清理、统计等）
        processWorkerExit(w, completedAbruptly);
    }
}
```

### 5. getTask() 方法

```
private Runnable getTask() {
    boolean timedOut = false; // 上次poll是否超时
    
    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);
        
        /*
         * 检查线程池状态：
         * 1. 如果状态 >= STOP（不处理任务）
         * 2. 或者状态 == SHUTDOWN且队列为空
         * 则减少worker计数并返回null
         */
        if (rs >= SHUTDOWN && (rs >= STOP || workQueue.isEmpty())) {
            decrementWorkerCount();
            return null;
        }
        
        int wc = workerCountOf(c);
        
        // 是否允许超时（非核心线程或允许核心线程超时）
        boolean timed = allowCoreThreadTimeOut || wc > corePoolSize;
        
        /*
         * 检查是否需要回收线程：
         * 1. worker数超过maximumPoolSize（可能由于setMaximumPoolSize被调小）
         * 2. 或者允许超时且已经超时
         * 3. 且（worker数>1 或 队列为空）
         */
        if ((wc > maximumPoolSize || (timed && timedOut))
            && (wc > 1 || workQueue.isEmpty())) {
            if (compareAndDecrementWorkerCount(c))
                return null;
            continue;
        }
        
        try {
            // 根据timed决定使用poll（超时获取）还是take（阻塞获取）
            Runnable r = timed ?
                workQueue.poll(keepAliveTime, TimeUnit.NANOSECONDS) :
                workQueue.take();
            if (r != null)
                return r;
            timedOut = true; // 获取超时标记
        } catch (InterruptedException retry) {
            timedOut = false;
        }
    }
}
```

## 三、关闭相关方法

### 1. shutdown()

```
public void shutdown() {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        // 权限检查
        checkShutdownAccess();
        // 推进线程池状态到SHUTDOWN
        advanceRunState(SHUTDOWN);
        // 中断所有空闲worker
        interruptIdleWorkers();
        // 钩子方法，子类可扩展
        onShutdown();
    } finally {
        mainLock.unlock();
    }
    // 尝试终止线程池
    tryTerminate();
}
```

### 2. shutdownNow()

```
public List<Runnable> shutdownNow() {
    List<Runnable> tasks;
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        checkShutdownAccess();
        // 推进线程池状态到STOP
        advanceRunState(STOP);
        // 中断所有worker（包括正在运行的）
        interruptWorkers();
        // 排出队列中的任务
        tasks = drainQueue();
    } finally {
        mainLock.unlock();
    }
    tryTerminate();
    return tasks;
}
```

### 3. tryTerminate()

```
final void tryTerminate() {
    for (;;) {
        int c = ctl.get();
        /*
         * 以下情况不会终止：
         * 1. 线程池还在运行
         * 2. 状态是TIDYING或TERMINATED
         * 3. SHUTDOWN状态但队列不为空
         */
        if (isRunning(c) ||
            runStateAtLeast(c, TIDYING) ||
            (runStateOf(c) == SHUTDOWN && ! workQueue.isEmpty()))
            return;
        
        // 如果worker数不为0，中断一个空闲worker
        if (workerCountOf(c) != 0) {
            interruptIdleWorkers(ONLY_ONE);
            return;
        }
        
        final ReentrantLock mainLock = this.mainLock;
        mainLock.lock();
        try {
            // CAS设置状态为TIDYING
            if (ctl.compareAndSet(c, ctlOf(TIDYING, 0))) {
                try {
                    terminated(); // 子类可实现的钩子方法
                } finally {
                    // 最终状态设为TERMINATED
                    ctl.set(ctlOf(TERMINATED, 0));
                    // 唤醒所有等待termination的线程
                    termination.signalAll();
                }
                return;
            }
        } finally {
            mainLock.unlock();
        }
    }
}
```

### 精巧设计

通过以上详细注释的源码分析，我们可以看到Java线程池的精巧设计：

1. 使用ctl变量高效管理状态和线程数
2. Worker继承AQS实现简单锁机制
3. 任务执行流程中的各种边界条件处理
4. 关闭时的状态流转和资源清理



## 三、关键机制详解

### 1. 线程创建与回收

- **线程创建**：通过`addWorker()`方法创建新Worker线程
- **线程回收**：
  - 非核心线程在空闲超过keepAliveTime后被回收
  - 核心线程默认不回收，除非设置allowCoreThreadTimeOut=true

### 2. 任务拒绝策略

当线程池无法接受新任务时（队列满且线程数=max），会调用拒绝策略：

java



复制



下载

```
final void reject(Runnable command) {
    handler.rejectedExecution(command, this);
}
```

四种内置策略的实现原理各不相同，例如：

- AbortPolicy直接抛出异常
- CallerRunsPolicy会调用`command.run()`在调用者线程执行

### 3. 线程池动态调整

ThreadPoolExecutor提供了一些动态调整方法：

java



复制



下载

```
public void setCorePoolSize(int corePoolSize);
public void setMaximumPoolSize(int maximumPoolSize);
public void setKeepAliveTime(long time, TimeUnit unit);
public void allowCoreThreadTimeOut(boolean value);
```

这些方法会触发线程数的动态调整，内部通过`interruptIdleWorkers()`等方法实现。

## 四、性能优化要点

1. **队列选择**：
   - `LinkedBlockingQueue`：无界队列，可能导致OOM
   - `ArrayBlockingQueue`：有界队列，需要合理设置大小
   - `SynchronousQueue`：直接交接，适合高吞吐场景
2. **线程工厂**：自定义ThreadFactory可以：
   - 设置更有意义的线程名称
   - 设置线程优先级
   - 设置为守护线程
3. **监控指标**：
   - `getPoolSize()`：当前线程数
   - `getActiveCount()`：活动线程数
   - `getCompletedTaskCount()`：已完成任务数
   - `getTaskCount()`：总任务数

## 五、常见问题解析

1. **线程池为什么使用BlockingQueue？**
   - 提供任务缓冲能力
   - 支持阻塞/超时获取操作
   - 线程安全
2. **为什么先入队而不是直接创建线程？**
   - 避免创建过多线程
   - 核心线程优先处理队列任务
   - 符合"核心线程常驻"的设计理念
3. **Worker为什么继承AQS？**
   - 实现简单的不可重入锁
   - 控制线程中断状态
   - 防止任务执行期间被重复中断

理解线程池的内部原理有助于在实际开发中更好地配置和使用线程池，避免常见的并发问题，并能在出现问题时快速定位原因。

# 其他

## 线程池状态编码原理深度解析

线程池使用一个AtomicInteger变量`ctl`同时维护线程池状态和工作线程数，这种设计是Java线程池实现中最精妙的部分之一。下面我将从设计目的、实现原理到具体操作进行完整解析。

**一、为什么要这样设计？**

**设计背景**

1. **性能考虑**：需要频繁检查/修改线程池状态和线程数
2. **原子性要求**：状态和线程数的修改必须保持原子性
3. **空间效率**：避免使用多个变量造成内存浪费和缓存失效

**解决方案**

将两个信息（状态+线程数）合并到一个32位整数中：

- 高3位表示线程池状态（5种状态只需3位）
- 低29位表示工作线程数（最多约5亿线程，完全够用）

**二、具体编码实现**

**1. 常量定义**

```
// Integer.SIZE = 32
private static final int COUNT_BITS = Integer.SIZE - 3;  // 29

// 线程池状态（高3位）
private static final int RUNNING    = -1 << COUNT_BITS;  // 11100000...0000
private static final int SHUTDOWN   =  0 << COUNT_BITS;  // 00000000...0000
private static final int STOP       =  1 << COUNT_BITS;  // 00100000...0000
private static final int TIDYING    =  2 << COUNT_BITS;  // 01000000...0000
private static final int TERMINATED =  3 << COUNT_BITS;  // 01100000...0000

// 工作线程数上限（低29位）
private static final int CAPACITY   = (1 << COUNT_BITS) - 1;  // 00011111...1111
```

**2. 数值计算示例**

假设当前：

- 状态：RUNNING（111）
- 工作线程数：5（000...0101）

合并后的ctl值：

```
111 00000000000000000000000000101
```

换算成十进制：-536870907

**3. 关键操作方法**

状态提取（取高3位）

```
private static int runStateOf(int c) {
    return c & ~CAPACITY;  // 高3位掩码操作
    // 等价于 c & (11100000...0000)
}
```

线程数提取（取低29位）

```
private static int workerCountOf(int c) {
    return c & CAPACITY;  // 低29位掩码操作
    // 等价于 c & (00011111...1111)
}
```

合并状态和线程数

```
private static int ctlOf(int rs, int wc) {
    return rs | wc;  // 按位或运算合并
}
```

**三、状态转换示例**

**1. 初始状态**

```
// RUNNING状态 | 0个工作线程
ctl = ctlOf(RUNNING, 0); 
// RUNNING = -1 << 29 = 11100000...0000
// 结果：11100000...0000
```

**2. 增加工作线程**

当新增一个工作线程时：

```
// CAS操作增加worker计数
compareAndIncrementWorkerCount(c) {
    // c = 当前ctl值
    // expect = c
    // update = c + 1
    // 只影响低29位
}
```

**3. 状态转换**

当调用shutdown()时：

```
// 从RUNNING(111)变为SHUTDOWN(000)
advanceRunState(SHUTDOWN) {
    for (;;) {
        int c = ctl.get();
        if (runStateAtLeast(c, SHUTDOWN) ||
            ctl.compareAndSet(c, ctlOf(SHUTDOWN, workerCountOf(c))))
            break;
    }
}
```

**四、为什么使用高3位表示状态？**

**状态值设计**

| 状态       | 二进制 | 十进制 | 含义                         |
| :--------- | :----- | :----- | :--------------------------- |
| RUNNING    | 111    | -1<<29 | 接收新任务并处理队列任务     |
| SHUTDOWN   | 000    | 0      | 不接收新任务但处理队列任务   |
| STOP       | 001    | 1<<29  | 不接收新任务，不处理队列任务 |
| TIDYING    | 010    | 2<<29  | 所有任务已终止，线程数=0     |
| TERMINATED | 011    | 3<<29  | terminated()方法已执行完成   |

**状态比较优势**

通过这种编码设计，可以直接用整数比较来判断状态：

```
// 判断状态是否至少是STOP
private static boolean runStateAtLeast(int c, int s) {
    return c >= s;  // 因为状态编码值是有序的
}
```

**五、实际操作案例**

案例1：检查线程池是否运行中

```
boolean isRunning(int c) {
    return c < SHUTDOWN;  // RUNNING是负数，SHUTDOWN=0
}
```

案例2：添加工作线程时的状态检查

```
if (rs >= SHUTDOWN &&
    !(rs == SHUTDOWN && firstTask == null && !workQueue.isEmpty()))
    return false;
```

解析：

- 如果状态≥SHUTDOWN（非RUNNING）
- 且不满足：状态正好是SHUTDOWN + 传入任务为null + 队列非空
- 则不允许添加worker

**六、设计精妙之处**

1. **原子性保证**：通过单个AtomicInteger保证状态和线程数修改的原子性
2. **高效判断**：状态值设计使得可以直接用整数比较判断状态
3. **空间节约**：单个变量存储两个关键信息
4. **并发安全**：所有修改都通过CAS操作完成

这种状态编码设计是Java并发编程中的经典模式，在资源受限环境下实现了高效的状态管理，非常值得学习借鉴。理解这个设计后，就能真正掌握线程池状态流转的核心机制。

# Java线程池任务队列选择对比分析

Java线程池中的任务队列（workQueue）是决定线程池行为特性的关键参数之一。不同的队列实现会直接影响线程池的任务调度策略和性能表现。下面我将详细比较各种队列实现的特性及适用场景。

## 一、队列类型总览

Java线程池常用的阻塞队列主要有以下几种：

| 队列实现类            | 特性描述               | 有界性 | 适用场景         |
| :-------------------- | :--------------------- | :----- | :--------------- |
| LinkedBlockingQueue   | 链表结构阻塞队列       | 可选   | 通用场景         |
| ArrayBlockingQueue    | 数组结构阻塞队列       | 有界   | 固定大小队列需求 |
| SynchronousQueue      | 不存储元素的阻塞队列   | 特殊   | 高吞吐量场景     |
| PriorityBlockingQueue | 带优先级的无界阻塞队列 | 无界   | 任务优先级调度   |
| DelayedWorkQueue      | 延迟队列（内部实现）   | 无界   | 定时线程池专用   |

## 二、核心队列详细对比

### 1. LinkedBlockingQueue

**特点**：

- 基于链表的可选有界阻塞队列
- 默认无界（Integer.MAX_VALUE）
- 采用两把锁（putLock/takeLock）分离读写操作

**线程池行为**：

java



复制



下载

```
new ThreadPoolExecutor(
    corePoolSize, 
    maximumPoolSize,
    keepAliveTime,
    TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue<Runnable>()  // 无界队列
);
```

- 当线程数达到corePoolSize后，新任务入队
- maximumPoolSize参数失效（因为队列无界）
- 可能导致OOM（任务无限堆积）

**变体用法**：

java



复制



下载

```
new LinkedBlockingQueue<Runnable>(100)  // 创建有界队列
```

- 队列满时行为取决于拒绝策略

**适用场景**：

- 需要排队处理的大量任务
- 任务到达速率波动较大
- 能确保不会无限堆积任务的场景

### 2. ArrayBlockingQueue

**特点**：

- 基于数组的有界阻塞队列
- 必须指定容量
- 单锁实现（ReentrantLock）

**线程池行为**：

```
new ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<Runnable>(100)  // 固定容量100
);
```

- 队列满且线程数<maxPoolSize时，创建新线程
- 队列满且线程数=maxPoolSize时，触发拒绝策略

**优势**：

- 内存使用更可控（预分配数组）
- FIFO特性更严格

**适用场景**：

- 需要严格控制队列长度的场景
- 内存受限环境
- 需要严格任务顺序的场景

### **3. SynchronousQueue**

**特点**：

- 不存储元素的阻塞队列
- 每个插入操作必须等待对应的移除操作

**线程池行为**：

```
new ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    TimeUnit.MILLISECONDS,
    new SynchronousQueue<Runnable>()
);
```

- 任务直接交给线程执行（没有缓冲）
- 如果没有可用线程，立即尝试创建新线程
- 达到maxPoolSize后触发拒绝策略

**性能特点**：

- 高吞吐量（任务直接交接，不经过队列）
- 低延迟

**适用场景**：

- 高并发短任务处理
- 要求快速响应的场景
- 线程池规模较大（可以处理瞬时高峰）

**4. PriorityBlockingQueue**

**特点**：

- 无界优先级阻塞队列
- 元素必须实现Comparable或提供Comparator

**线程池行为**：

```
new ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    keepAliveTime,
    TimeUnit.MILLISECONDS,
    new PriorityBlockingQueue<Runnable>(100, comparator)
);
```

- 按优先级处理任务（非FIFO）
- 同样存在OOM风险（无界）

**适用场景**：

- 需要优先级调度的任务
- 紧急任务需要优先处理的情况

## **三、关键选择维度对比**

| 维度             | LinkedBlockingQueue | ArrayBlockingQueue | SynchronousQueue | PriorityBlockingQueue |
| :--------------- | :------------------ | :----------------- | :--------------- | :-------------------- |
| **有界/无界**    | 可选                | 有界               | 特殊（容量0）    | 无界                  |
| **内存影响**     | 可能OOM（无界时）   | 可控               | 最小             | 可能OOM               |
| **吞吐量**       | 高                  | 中                 | 最高             | 中                    |
| **任务顺序**     | FIFO                | 严格FIFO           | -                | 按优先级              |
| **线程创建策略** | 先核心->队列        | 先核心->队列->扩容 | 直接扩容         | 先核心->队列          |
| **适用场景**     | 通用                | 流量控制           | 高吞吐短任务     | 优先级任务            |

## **四、典型配置案例**

**案例1：Web服务请求处理**

```
// 适合突发流量的Web服务
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    10,  // corePoolSize
    100, // maximumPoolSize
    60,  // keepAliveTime
    TimeUnit.SECONDS,
    new SynchronousQueue<>(),
    new ThreadPoolExecutor.CallerRunsPolicy()
);
```

**特点**：

- 快速响应请求
- 高峰时自动扩容
- 超出能力后由调用线程处理（降级）

**案例2：后台批处理任务**

```
// 适合稳定的后台任务处理
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    5,   // corePoolSize
    5,   // maximumPoolSize (固定大小)
    0,   // keepAliveTime
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(1000), // 缓冲队列
    new ThreadPoolExecutor.AbortPolicy()
);
```

**特点**：

- 稳定处理速度
- 队列缓冲突发任务
- 过载时直接拒绝

**案例3：定时任务调度**

```
// 适合执行定时任务
ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
    5,
    new DelayedWorkQueue()  // 内部专用延迟队列
);
```

**特点**：

- 专门优化延迟/周期任务
- 任务按触发时间排序

## **五、选择建议**

1. **需要流量控制**：
   - 选择`ArrayBlockingQueue`（明确边界）
   - 配合合理的拒绝策略
2. **追求高吞吐**：
   - 选择`SynchronousQueue`
   - 设置较大的maxPoolSize
3. **需要任务优先级**：
   - 选择`PriorityBlockingQueue`
   - 注意OOM风险
4. **通用场景**：
   - 选择有界`LinkedBlockingQueue`
   - 平衡吞吐和内存使用
5. **定时/延迟任务**：
   - 使用`ScheduledThreadPoolExecutor`
   - 其内置`DelayedWorkQueue`已优化

## **六、注意事项**

1. **无界队列风险**：
   - 可能导致OOM
   - 使maximumPoolSize参数失效
2. **有界队列策略**：
   - 合理设置队列大小
   - 必须配置拒绝策略
3. **SynchronousQueue特性**：
   - 适合可以快速处理的任务
   - 长时间任务可能导致线程数暴涨
4. **优先级队列**：
   - 可能引起任务饥饿（低优先级任务长期得不到执行）
   - 需要谨慎设计比较逻辑

通过合理选择任务队列，可以优化线程池在不同场景下的表现，平衡吞吐量、延迟和资源消耗之间的关系。实际应用中建议通过压力测试确定最佳配置。