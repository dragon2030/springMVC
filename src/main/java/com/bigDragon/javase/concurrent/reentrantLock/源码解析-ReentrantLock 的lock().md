【acquireQueued 和 addWaiter 只看懂大概,特别是节点状态流转一部分】
【共享模式的aqs有点像同步模型，没看看吐了】

# (1)lock

***ReentrantLock.lock()***

```
public void lock() {
    sync.lock(); // 调用内部同步器的加锁方法
}
```

* private final Sync sync;分别是FairSync和NonfairSync
* ReentrantLock创建时会初始化内部类选择公平/非公平
  ```
  public ReentrantLock() {
    sync = new NonfairSync();
  }
  ```

## (2)lock

**ReentrantLock.lock()->Sync.lock()**
非公平方法（ReentrantLock构造器默认）

```
  final void lock() {
      if (compareAndSetState(0, 1)) { // 尝试获取锁
        setExclusiveOwnerThread(Thread.currentThread()); // 设置当前线程为锁的持有者
      } else {
        acquire(1); // 调用 AQS 的 acquire 方法获取锁
      }
  }
```

公平方法

```
final void lock() {
    acquire(1);
}
```

### (3)acquire

**ReentrantLock.lock()->Sync.lock()->acquire(1)**

```
    public final void acquire(int arg) {
        if (!tryAcquire(arg) && 
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

1. tryAcquire(1) - 尝试直接获取锁

* 参数1 表示请求的许可数量（对于独占锁通常是1）
* 返回值：true 表示获取成功，false 表示失败
* 作用：它会检查锁状态，如果是无锁状态则尝试 CAS 获取锁。如果是重入情况（当前线程已持有锁），则增加重入计数

2. addWaiter(Node.EXCLUSIVE) - 创建节点并加入队列

* 作用：将当前线程包装为独占模式节点加入等待队列
* 参数：Node.EXCLUSIVE 表示独占模式
* 过程：

> 1. 创建新节点（包含当前线程）
> 2. 快速尝试将节点加入队尾（如果队列已存在）
> 3. 如果队列为空或快速尝试失败，则调用完整入队方法

3. acquireQueued(node, arg) - 在队列中等待获取锁

* 作用：让已经在队列中的节点循环尝试获取锁
* 参数：node：刚加入队列的节点 1：请求的许可数量
* 内部逻辑：

> 1. 进入循环不断尝试获取锁
> 2. 每次失败后检查是否需要挂起线程（通过前驱节点的状态）
> 3. 如果被挂起后又被唤醒，继续尝试获取锁
> 4. 如果在等待过程中被中断，返回 true，否则返回 false

4. selfInterrupt() - 自我中断
5. 完整执行流程图

```
开始
  ↓
tryAcquire(1) → 成功? → 获取锁成功
  ↓ 失败
addWaiter(Node.EXCLUSIVE) → 加入等待队列
  ↓
acquireQueued(node, 1) → 在队列中循环尝试获取锁
  ↓
  成功获取锁
  ↓
  检查是否在等待过程中被中断? → 是 → selfInterrupt()
  ↓ 否
结束
```

#### (4)tryAcquire

**ReentrantLock.lock()->Sync.lock()->acquire(1)->tryAcquire**
尝试直接获取锁(在非公平锁执行中是可插队的体现)

```
protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}
// 尝试获取锁
protected boolean nonfairTryAcquire(int arg) {
    // 获取当前状态
    int c = getState();
    Thread current = Thread.currentThread();
    if (c == 0) { // 如果当前状态为0，表示锁是空闲的
        if (
        //!hasQueuedPredecessors() && //在公平锁中存在，是公平性核心，用于检查是否有排队线程
        compareAndSetState(0, arg)
        ) { // 尝试获取锁
            setExclusiveOwnerThread(current); // 设置当前线程为锁的持有者
            return true; // 获取锁成功
        }
    } else if (current == getExclusiveOwnerThread()) { // 如果当前线程已经是锁的持有者，允许重入
        int nextc = c + arg;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
        setState(nextc); // 更新状态
        return true; // 获取锁成功
    }
    return false; // 获取锁失败
}
```

#### (4)addWaiter

**ReentrantLock.lock()->Sync.lock()->acquire(1)->addWaiter**
创建节点并加入队列

* 过程：

> 1. 创建新节点（包含当前线程）
> 2. 快速尝试将节点加入队尾（如果队列已存在）
> 3. 如果队列为空或快速尝试失败，则调用完整入队方法

```
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // 快速尝试入队（优化路径）
    Node pred = tail;
    if (pred != null) {// 队列已初始化
        node.prev = pred;// 1.设置前驱指针,设置新节点的prev指向当前tail
        if (compareAndSetTail(pred, node)) {// 2.CAS更新tail,将tail从pred更新为新节点
            pred.next = node;// 3.设置后继指针,将原tail的next指向新节点
            return node;
        }
    }
    // 完整入队（慢速路径）
    enq(node);
    return node;
}
```

* mode：节点模式，两种取值：
  * Node.EXCLUSIVE：独占模式（用于ReentrantLock）
  * Node.SHARED：共享模式（用于Semaphore/CountDownLatch等）

##### (5)enq(node)

**ReentrantLock.lock()->Sync.lock()->acquire(1)->addWaiter->enq(node)**
完整入队（enq方法）,当快速尝试失败（队列为空或CAS竞争失败）时调用：

```
private Node enq(final Node node) {
    for (;;) { // 自旋直到成功
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

#### (4)acquireQueued

**ReentrantLock.lock()->Sync.lock()->acquire(1)->acquireQueued**
调用 acquireQueued 方法来实现锁的获取。这个方法在 AbstractQueuedSynchronizer（AQS）中定义，负责管理等待队列中的节点，并决定线程如何进行排队等待获取锁。

* 参数：
  node：当前线程对应的队列节点（由addWaiter创建）
  arg：获取锁的参数（通常为1，表示获取一个许可）
* 返回值：
  true：线程在等待过程中被中断
  false：正常获取到锁
* 内部逻辑：

> 1. 进入循环不断尝试获取锁
> 2. 每次失败后检查是否需要挂起线程（通过前驱节点的状态）
> 3. 如果被挂起后又被唤醒，继续尝试获取锁
> 4. 如果在等待过程中被中断，返回 true，否则返回 false

```
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        //自旋尝试获取锁，直到成功获取为止或者线程被中断
        for (;;) {
            //获取当前节点的前驱节点
            final Node p = node.predecessor();
            //检查前驱节点 p 是否是当前队列的头节点 head，并尝试获取锁。
            //意思为：当前节点是队列中的第一个真实等待节点,这个节点有资格尝试获取锁
            if (p == head && tryAcquire(arg)) {
                //头节点 head 在等待队列中起着重要的作用，它通常是已经成功获取锁或者最近一次释放锁的节点。
                setHead(node);//将当前节点 node 设置为新的头节点
                p.next = null; //取消掉前驱节点的引用,以便垃圾回收器能够释放内存空间
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

#### (5)shouldParkAfterFailedAcquire

**ReentrantLock.lock()->Sync.lock()->acquire(1)->acquireQueued->shouldParkAfterFailedAcquire**
用于判断在获取锁失败后，当前线程是否应该被挂起（park）

* 参数说明
  * pred: 当前节点的前驱节点
  * node: 当前节点（当前线程对应的节点）
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
           * waitStatus为0或PROPAGATE(-3)
           * 使用CAS将前驱节点的waitStatus设置为SIGNAL，表示需要通知
           */
          compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
      }
      return false;
  }
```

# waitStatus字段

**AbstractQueuedSynchronizer（AQS）中，节点（Node）的waitStatus 字段**
在 AbstractQueuedSynchronizer（AQS）中，等待队列的每个节点（Node）都有一个 waitStatus 字段，用于表示节点的状态。常见的 waitStatus 值及其含义如下：


| 状态值 | 常量名    | 含义                             |
| -------- | ----------- | ---------------------------------- |
| -3     | PROPAGATE | 共享模式下传播释放状态           |
| -2     | CONDITION | 节点在条件队列中等待             |
| -1     | SIGNAL    | 表示当前节点的后继节点需要被唤醒 |
| 0      | 0         | 初始状态（新创建的节点）         |
| 1      | CANCELLED | 节点因超时或中断被取消           |

## lock/unlock时AQS队列状态变化

**逐节点分析lock/unlock时AQS队列状态变化过程（公平锁举例更直观）**
**一句话总结：节点入队(waitStatus=0) → 前驱节点设为SIGNAL(-1) → 阻塞等待 → 前驱unlock()唤醒 → 被唤醒节点tryAcquire()成功 → 节点变为头节点(waitStatus=0) → 持锁运行 → 释放锁(unlock())，再唤醒下个节点。**
* 假设现在有三个线程依次执行了lock()：
  * 线程A（第一个执行lock）
  * 线程B（第二个执行lock）
  * 线程C（第三个执行lock）
  * 线程A unlock
### 线程A执行lock()：

* 线程A调用tryAcquire()成功获取锁，此时AQS的state从0变为1，持有锁。
* 队列状态：队列为空（只有头节点，头节点代表当前持有锁的线程），头节点waitStatus初始为0，不用修改。

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

## cancel状态的情况说明

* 若某个等待线程中途中断，节点会变成CANCELLED状态：
* 比如C节点因中断从阻塞状态被唤醒，此时C节点的waitStatus被设置为CANCELLED(1)，表示放弃锁的争夺：

```
B(Head, waitStatus=0, 持锁) → C(waitStatus=1, 已取消)
```

* 当B释放锁时，发现后继节点C状态为CANCELLED，AQS会跳过C节点，继续寻找下一个有效节点（如果有）。

# 与AQS核心相关方法

acquire(int arg)方法开始进入AQS核心类AbstractQueuedSynchronizer
后执行AQS核心方法addWaiter()、acquireQueued()

```
ReentrantLock.lock()
  → Sync.acquire(1) (AQS)
    → tryAcquire(1) (子类实现)
    → addWaiter(Node.EXCLUSIVE) (AQS)
    → acquireQueued() (AQS)
```

# 模板方法模式

AQS底层使用了模板方法模式

```
// 需要子类实现
protected boolean tryAcquire(int arg)
protected boolean tryRelease(int arg)

// AQS已实现
public final void acquire(int arg)
public final boolean release(int arg)
```
