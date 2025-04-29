unlock() 是释放锁的核心方法，其实现涉及锁状态管理、线程唤醒和队列操作等多个关键并发机制。

# (1)unlock

**ReentrantLock.unlock()**

```
public void unlock() {
    sync.release(1); // 委托给Sync内部类
}
```

## (2)release

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

### (3)tryRelease

**ReentrantLock.unlock()->sync.release(1)->tryRelease**
尝试释放锁

```
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
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

### (3)unparkSuccessor

**ReentrantLock.unlock()->sync.release(1)->unparkSuccessor**
唤醒后继节点

```
private void unparkSuccessor(Node node) {
    int ws = node.waitStatus;
    if (ws < 0) //将头节点状态从SIGNAL(-1)重置为0
        compareAndSetWaitStatus(node, ws, 0); // 清除信号状态

    Node s = node.next;
    if (s == null || s.waitStatus > 0) { // 后继节点无效（取消或null）
        s = null;
        //等待队列先进先出（FIFO）的原则,从尾部开始遍历可以优先处理最早进入队列的节点
        for (Node t = tail; t != null && t != node; t = t.prev)
            if (t.waitStatus <= 0)      // 从尾向前找有效节点
                s = t;
    }
    if (s != null)
        LockSupport.unpark(s.thread);   // 唤醒线程
}
```

# 与AQS核心相关方法

release()方法开始进入AQS核心类AbstractQueuedSynchronizer

```
ReentrantLock.unlock()
  → Sync.release(1) (AQS)
    → tryRelease(1) (Sync实现)
    → unparkSuccessor() (AQS)
```
