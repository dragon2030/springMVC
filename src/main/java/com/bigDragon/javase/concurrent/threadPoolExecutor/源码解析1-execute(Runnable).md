ThreadPoolExecutor.execute(Runnable) 源码逐行解析
以下是 Java 8 中 ThreadPoolExecutor.execute(Runnable) 的源码及解析：
# 源码及解析
```
public void execute(Runnable command) {
    if (command == null)
        throw new NullPointerException();

    // 获取控制状态和线程数的组合值
    int c = ctl.get();
    
    // 第一阶段：检查是否可以用核心线程执行任务
    if (workerCountOf(c) < corePoolSize) {
        if (addWorker(command, true))
            return;
        c = ctl.get();
    }
    
    // 第二阶段：尝试将任务加入工作队列
    if (isRunning(c) && workQueue.offer(command)) {
        int recheck = ctl.get();
        if (!isRunning(recheck) && remove(command))
            reject(command);
        else if (workerCountOf(recheck) == 0)
            addWorker(null, false);
    }
    
    // 第三阶段：尝试创建非核心线程执行任务
    else if (!addWorker(command, false))
        // 如果失败，执行拒绝策略
        reject(command);
}
```
详细解析
参数检查
```
if (command == null)
    throw new NullPointerException();
```

首先检查传入的 Runnable 任务是否为 null，如果是则抛出 NullPointerException

获取线程池控制状态

int c = ctl.get();
ctl 是一个 AtomicInteger，高3位表示线程池运行状态，低29位表示工作线程数

通过 ctl.get() 获取当前线程池状态和线程数的组合值

## 第一阶段：尝试使用核心线程执行

java
if (workerCountOf(c) < corePoolSize) {
if (addWorker(command, true))
return;
c = ctl.get();
}
workerCountOf(c) 提取当前工作线程数

如果当前工作线程数小于核心线程数(corePoolSize)，尝试创建新的核心线程

addWorker(command, true) 表示尝试添加一个核心线程执行任务

如果添加成功，直接返回

如果失败(可能因为线程池状态变化)，重新获取控制状态

## 第二阶段：尝试加入工作队列

```
if (isRunning(c) && workQueue.offer(command)) {
    int recheck = ctl.get();
if (!isRunning(recheck) && remove(command))
    reject(command);
else if (workerCountOf(recheck) == 0)
    addWorker(null, false);
}
```

检查线程池是否仍在运行状态(isRunning(c))

尝试将任务加入工作队列(workQueue.offer(command))

如果入队成功，进行双重检查：

重新检查线程池状态，如果已停止且成功移除任务，执行拒绝策略

如果当前没有工作线程(workerCountOf(recheck) == 0)，添加一个非核心线程(传入null任务)

这种设计确保即使没有核心线程，也有线程处理队列中的任务

第三阶段：尝试创建非核心线程

```
else if (!addWorker(command, false))
    reject(command);
```

如果任务无法加入队列(队列已满)，尝试创建非核心线程(addWorker(command, false))

如果创建失败(通常因为线程数已达最大值或线程池已关闭)，执行拒绝策略

# 成员变量解析
## ctl (AtomicInteger)
作用：线程池的核心控制状态，用一个原子整数同时维护线程池的运行状态和工作线程数量。

结构：

高3位：表示线程池的运行状态 (runState)

低29位：表示有效的工作线程数量 (workerCount)

相关常量：

java
private static final int COUNT_BITS = Integer.SIZE - 3; // 29
private static final int CAPACITY   = (1 << COUNT_BITS) - 1; // 00011111...111 (29个1)

// 运行状态存储在高3位
private static final int RUNNING    = -1 << COUNT_BITS; // 11100000...000
private static final int SHUTDOWN   =  0 << COUNT_BITS; // 00000000...000
private static final int STOP       =  1 << COUNT_BITS; // 00100000...000
private static final int TIDYING    =  2 << COUNT_BITS; // 01000000...000
private static final int TERMINATED =  3 << COUNT_BITS; // 01100000...000
状态说明：

RUNNING：接受新任务并处理排队任务

SHUTDOWN：不接受新任务，但处理排队任务

STOP：不接受新任务，不处理排队任务，中断正在进行的任务

TIDYING：所有任务已终止，workerCount为零，线程转换到TIDYING状态将运行terminated()钩子方法

TERMINATED：terminated()方法已完成
