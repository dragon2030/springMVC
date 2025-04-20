# 1. ReentrantLock 中的 CAS 体现在哪里？
## (1) 锁状态（state）的原子性更新
ReentrantLock 内部使用 AbstractQueuedSynchronizer (AQS)，其核心是通过 CAS 操作 维护一个 volatile int state 变量：

```
    // AQS 中的关键字段
    private volatile int state;  // 锁状态：0=未锁定，>0=被锁定
    
    // 通过CAS修改状态
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
```
当线程尝试获取锁时，首先会尝试用 CAS 将 state 从 0 改为 1（快速路径）

如果成功，则直接获取锁；如果失败（说明锁被占用），才进入排队逻辑

## (2) 非公平锁的抢占机制
非公平锁的实现中，新来的线程会直接尝试 CAS 抢锁，而不关心等待队列：

```
// 非公平锁的获取逻辑（简化版）
final void lock() {
    if (compareAndSetState(0, 1))  // 先尝试CAS抢锁
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);  // 抢锁失败后进入排队逻辑
}
```
## (3) CLH 队列的节点操作
AQS 内部的 CLH 队列（线程等待队列）的节点状态更新也依赖 CAS：

```
// 节点状态通过CAS更新
private final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
    return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
}
```
# 2. 为什么表面看起来像悲观锁？
## (1) API 设计是悲观锁风格
用户通过 lock()/unlock() 显式加锁，行为模式上是悲观锁（假定会发生竞争，先加锁再操作）。

## (2) 但底层实现是乐观锁（CAS）优化
快速路径：先乐观尝试 CAS 获取锁（无竞争时性能接近 CAS）

慢速路径：CAS 失败后降级为排队阻塞（悲观锁行为）

这种设计被称为 "乐观读 + 悲观写" 的混合模式。

# 3. 与纯 CAS 自旋锁的对比
特性	纯 CAS 自旋锁 (如 AtomicInteger)	ReentrantLock
竞争处理	持续自旋消耗 CPU	CAS 失败后排队阻塞（节省 CPU）
公平性	无保证，可能饥饿	可选择公平/非公平模式
可重入性	不支持	支持
功能扩展	简单计数器	支持条件变量、中断等高级特性
适用场景	低竞争、简单操作	高竞争、复杂同步需求
# 4. 如何用 ReentrantLock 实现类似 CAS 的语义？
虽然 ReentrantLock 的 API 是悲观风格，但可以通过 tryLock 模拟乐观行为：

```
// 模拟乐观锁的+1操作
public boolean optimisticIncrement() {
    if (lock.tryLock()) {  // 尝试获取锁（非阻塞）
        try {
            count++;
            return true;
        } finally {
            lock.unlock();
        }
    }
    return false;  // 获取锁失败时不阻塞
}
```
# 5. 设计哲学总结
分层设计：

乐观快速路径（CAS 抢锁）→ 悲观慢速路径（排队阻塞）

平衡了无竞争时的性能和竞争时的公平性

硬件友好：

无竞争时 CAS 操作利用 CPU 的原子指令

有竞争时通过 park() 让出 CPU 资源

功能扩展性：
在 CAS 的基础上构建了可重入、条件变量等高级功能

# 关键结论
✅ CAS 体现在锁的内部状态管理（如 state 的原子更新、队列节点操作），而不是暴露给用户的 API 层面。
✅ ReentrantLock 是 悲观锁 API + 乐观锁优化 的混合实现，比纯悲观锁（如 synchronized）更灵活，比纯 CAS 自旋锁更功能完备。
✅ 选择依据：

低竞争简单操作 → 用 AtomicInteger 等纯 CAS

高竞争或需要高级特性 → 用 ReentrantLock
