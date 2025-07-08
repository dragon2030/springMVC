# Java CAS (Compare-And-Swap) 

CAS (Compare-And-Swap) 是Java中一种重要的无锁并发编程技术，它通过硬件级别的原子操作实现线程安全，避免了传统锁机制的开销。

## 什么是CAS

CAS是一种原子操作，包含三个操作数：

1. 内存位置(V)
2. 预期原值(A)
3. 新值(B)

CAS操作逻辑：当且仅当内存位置V的值等于预期原值A时，才将内存位置V的值修改为B，否则不执行任何操作。无论哪种情况，都会返回V的旧值。

**应用场景**

1. 原子类（`AtomicInteger`, `AtomicLong`等）
2. 并发容器（如`ConcurrentHashMap`）
3. 锁的实现（如`AQS`框架）

## Java中的CAS实现

在Java中，CAS操作主要通过`sun.misc.Unsafe`类和`java.util.concurrent.atomic`包中的原子类实现。

### Unsafe类

`Unsafe` 是 Java 中一个特殊的工具类，它提供了一系列**低级别、不安全**的操作，可以绕过 JVM 的安全检查机制直接操作内存和线程。这个类主要被 JDK 内部使用，普通应用程序通常不应该直接使用它。

#### 核心特性

1. 内存操作

- **直接内存分配/释放**：可以像 C 语言一样直接操作内存
- **对象字段偏移量获取**：获取对象字段在内存中的精确位置
- **字段值的直接读写**：绕过 Java 的访问控制机制

```
// 获取字段偏移量
long offset = unsafe.objectFieldOffset(MyClass.class.getDeclaredField("value"));

// 直接修改字段值
unsafe.putInt(obj, offset, 42);  // 相当于 obj.value = 42
```

2. 线程操作

- **线程挂起与恢复**：低级别的线程控制
- **CAS (Compare-And-Swap) 操作**：实现无锁算法的基础

```
// CAS 操作示例
unsafe.compareAndSwapInt(obj, offset, expect, update);
```

3. 数组操作

- **数组元素偏移量计算**：访问数组元素的底层实现
- **数组边界检查绕过**：不安全的数组访问

```
// 获取数组第一个元素的偏移量
int baseOffset = unsafe.arrayBaseOffset(int[].class);

// 获取数组中元素的间隔大小
int indexScale = unsafe.arrayIndexScale(int[].class);
```

4. 类与对象操作

- **类实例化**：绕过构造方法直接创建实例
- **类加载**：低级别的类加载控制

```
// 不调用构造方法创建实例
MyClass obj = (MyClass) unsafe.allocateInstance(MyClass.class);
```

#### 获取 Unsafe 实例

由于 `Unsafe` 的设计意图是仅供 JDK 内部使用，获取其实例需要特殊技巧：

```
// 通过反射获取 Unsafe 实例
Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
theUnsafe.setAccessible(true);
Unsafe unsafe = (Unsafe) theUnsafe.get(null);
```

#### 典型使用场景

1. **原子变量实现**：`AtomicInteger` 等原子类内部使用
2. **并发工具类**：如 `ConcurrentHashMap`、`AQS` 等
3. **直接内存访问**：NIO 的 `DirectByteBuffer` 实现
4. **性能敏感代码**：需要绕过 JVM 安全检查的极端优化场景

#### Java 9+ 的替代方案

由于 `Unsafe` 的不安全特性，Java 9 引入了更安全的替代 API：

1. **VarHandle**：提供类似但更安全的内存访问
2. **MethodHandles**：更安全的反射替代方案

#### 为什么需要 Unsafe

尽管不安全，但 `Unsafe` 对 Java 生态至关重要：

- 为 JDK 提供实现并发工具的低层基础
- 在性能关键的场景提供必要的优化手段
- 支持需要直接内存操作的特殊用例（如序列化、NIO）





### atomic原子类实现

Java中的`java.util.concurrent.atomic`包提供了一系列原子类，这些类是构建无锁并发算法的基础。它们的实现主要基于以下几个关键技术：

#### 核心实现机制

1. CAS (Compare-And-Swap) 操作

2. Unsafe类的使用

原子类内部使用`sun.misc.Unsafe`类（Java 9+使用`VarHandle`）来执行底层硬件级别的原子操作：

```
// AtomicInteger中的典型实现
public final boolean compareAndSet(int expectedValue, int newValue) {
    return unsafe.compareAndSwapInt(this, valueOffset, expectedValue, newValue);
}
```

3. volatile变量保证可见性

原子类内部使用volatile变量存储实际值，保证内存可见性：

```
private volatile int value;  // 在AtomicInteger中
```

#### 主要原子类实现分析

##### 1. 基本类型原子类

```
//-------------------基本用法-------------------
AtomicInteger atomicInteger = new AtomicInteger();
atomicInteger.getAndIncrement();
//-------------------AtomicInteger源码解析-------------------
public class AtomicInteger extends Number {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;  // 字段内存偏移量
    
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    
    private volatile int value;
    
    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }
} 
//-------------------unsafe源码解析-------------------
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);//获取当前值
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));//cas操作 替换更新旧值

    return var5;
}
```

核心方法解读：

* this.compareAndSwapInt(var1, var2, var5, var5 + var4)
  * var1传入值是this，通过对象基地址 + valueOffset（内存偏移量）的方式，直接计算字段的内存地址，比通过引用访问更高效
  * var1:当前对象 var2:要修改的字段在对象内存中的偏移量 var5:预期值 var5+var4 要修改的值

**典型方法实现**：

- `getAndIncrement()`: 原子性i++,返回旧值并进行加一
- `incrementAndGet()`: 原子性++i,进行加一 并返回更新后的值
- `getAndDecrement()`: 原子性i--
- `getAndAdd(int delta)`: 原子性加delta
- `compareAndSet(int expect, int update)`: CAS操作

##### 2. 引用类型原子类

###### AtomicReference

简介：

`AtomicReference` 是 Java 并发包 (`java.util.concurrent.atomic`) 中的一个类，它提供了一种线程安全的引用变量操作方式。

示例：

```
//-------------------基本用法-------------------
// 创建AtomicReference并设置初始值
AtomicReference<String> atomicRef = new AtomicReference<>("initial value");

// 获取当前值
String currentValue = atomicRef.get();
System.out.println("Current Value: " + currentValue); // 输出: initial value

// 设置新值
atomicRef.set("new value");
System.out.println("New Value: " + atomicRef.get()); // 输出: new value

//-------------------原子性比较并交换 (CAS)-------------------
AtomicReference<Integer> atomicRef = new AtomicReference<>(100);

// 如果当前值是100，则原子性地更新为200
boolean success = atomicRef.compareAndSet(100, 200);
System.out.println("Update successful? " + success); // true
System.out.println("Current value: " + atomicRef.get()); // 200

// 再次尝试更新（会失败，因为当前值已经是200）
success = atomicRef.compareAndSet(100, 300);
System.out.println("Update successful? " + success); // false
System.out.println("Current value: " + atomicRef.get()); // 200

//-------------------源码解析-------------------
public class AtomicReference<V> {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    
    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                (AtomicReference.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    
    private volatile V value;
    
    public final boolean compareAndSet(V expect, V update) {
        return unsafe.compareAndSwapObject(this, valueOffset, expect, update);
    }
}
```

注意：`AtomicReference` 进行 CAS（Compare-And-Swap）操作时，比较的是引用变量的内存地址（即对象引用本身），而不是对象的内容（值）

###### AtomicStampedReference (ABA)

`AtomicStampedReference` 是 Java 并发包中解决 **ABA 问题** 的原子类，它在 `AtomicReference` 的基础上增加了一个 **版本号（stamp）**，可以同时原子性地更新引用和版本号。

**ABA 问题是指：**

- 线程1读取值为 A
- 线程2将值从 A 改为 B 又改回 A
- 线程1执行 CAS 时仍然看到 A，误认为没有被修改过

```
//-------------------基本用法-------------------
// 初始值为 "A"，初始版本号为 0
AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 0);

// 获取引用和版本号
int[] stampHolder = new int[1];
String currentRef = asr.get(stampHolder);
int currentStamp = stampHolder[0];

System.out.println("Current Value: " + currentRef + ", Stamp: " + currentStamp);
//-------------------AtomicStampedReference解决ABA问题-------------------
AtomicStampedReference<String> asr = new AtomicStampedReference<>("A", 0);

// 线程1
int[] stampHolder = new int[1];
String initialRef = asr.get(stampHolder);
int initialStamp = stampHolder[0];

// 线程2 修改值 A -> B -> A，但每次修改都增加版本号
asr.compareAndSet("A", "B", 0, 1);  // stamp 0→1
asr.compareAndSet("B", "A", 1, 2);  // stamp 1→2

// 线程1尝试更新
boolean success = asr.compareAndSet(initialRef, "C", initialStamp, initialStamp + 1);

System.out.println("CAS成功了吗? " + success);  // false，因为版本号变了
```

##### 3. 数组原子类

```
//-------------------基本用法-------------------
// 创建长度为5的原子数组，初始值为0
AtomicIntegerArray atomicArray = new AtomicIntegerArray(5);

// 设置元素值
atomicArray.set(0, 10);

// 获取元素值
int value = atomicArray.get(0);
System.out.println("Index 0: " + value); // 输出: 10

// 获取数组长度
int length = atomicArray.length();
System.out.println("Array length: " + length); // 输出: 5
//-------------------原子性更新操作------------------- 
AtomicIntegerArray atomicArray = new AtomicIntegerArray(new int[]{1, 2, 3, 4, 5});

// 原子性递增
int newValue = atomicArray.incrementAndGet(2); // 索引2的元素从3增加到4
System.out.println("New value at index 2: " + newValue);

// 原子性递减
newValue = atomicArray.decrementAndGet(3); // 索引3的元素从4减少到3
System.out.println("New value at index 3: " + newValue);

// 原子性加法
atomicArray.addAndGet(1, 5); // 索引1的元素2 + 5 = 7
System.out.println("After add: " + atomicArray.get(1));

// 原子性比较并交换(CAS)
boolean success = atomicArray.compareAndSet(0, 1, 10); // 如果索引0的值是1，则设置为10
System.out.println("CAS successful: " + success);
```

#### 与锁的对比

| **对比维度**   | **CAS**                             | **同步锁(synchronized/Lock)**  |
| :------------- | :---------------------------------- | :----------------------------- |
| **实现原理**   | 硬件指令支持的原子操作              | JVM/OS级别的线程阻塞机制       |
| **线程状态**   | 始终运行(自旋)                      | 可能进入阻塞状态               |
| **性能特点**   | 低竞争时性能高，高竞争时自旋消耗CPU | 竞争激烈时性能稳定             |
| **死锁风险**   | 无                                  | 存在死锁风险                   |
| **使用粒度**   | 单个变量                            | 代码块/方法                    |
| **ABA问题**    | 存在(需AtomicStampedReference解决)  | 不存在                         |
| **功能扩展性** | 仅支持基本原子操作                  | 支持条件等待、可重入等复杂功能 |
| **适用场景**   | 简单原子操作、计数器等              | 复杂同步逻辑、临界区保护       |
| **开发复杂度** | 较高(需处理ABA等问题)               | 较低(语法简单)                 |
| **内存开销**   | 较小                                | 较大(维护锁对象)               |



# CAS底层实现

在Java中，CAS操作最终会调用本地方法，通过JNI调用CPU的原子指令（如x86架构的`cmpxchg`指令）实现。

## CAS操作的伪代码

```
// 完整模拟CAS操作的伪代码（包含值替换和内存屏障）
boolean atomicCompareAndSwap(
    Object obj,        // 包含目标字段的对象
    long offset,       // 目标字段的内存偏移量
    int expected,      // 期望值
    int newValue       // 新值
) {
    // 1. 获取当前CPU核心的本地缓存行
    CacheLine cache = getCacheLine(obj, offset);
    
    // 2. 内存屏障：保证之前的所有读写操作完成
    memoryBarrier();
    
    // 3. 原子操作开始（CPU硬件保证的不可分割操作）
    synchronized(cache) { // 这里不是Java锁，模拟CPU的缓存锁定
        // 从主内存加载最新值（可能触发缓存一致性协议）,绕过缓存直接读主内存
        int currentValue = ,loadVolatile(obj, offset);
        
        // 比较阶段
        if (currentValue == expected) {
            // 交换阶段,立即刷新到主内存
            storeVolatile(obj, offset, newValue);
            
            // 内存屏障：保证修改立即刷新到主内存
            memoryBarrier();
            
            // 通知其他CPU核心该缓存行失效（MESI协议）
            invalidateOtherCaches(cache);
            
            return true;
        }
        return false;
    }
    // 原子操作结束
}
```

### 硬件实际执行流程（简化）

1. CPU将目标内存地址的值加载到寄存器
2. 比较寄存器值与期望值
3. 如果相等：
   - 将新值写入寄存器
   - 执行原子性的存储指令（如x86的`lock cmpxchg`）
4. 设置标志寄存器表示成功/失败

## `cmpxchg` 指令

 cmpxchg（Compare and Exchange）是 x86/x64 架构 中的一条汇编指令，用于实现原子性的 比较并交换（Compare-and-Swap, CAS） 操作。是 Java 中 Unsafe 类 CAS 操作（如 compareAndSwapInt）的底层硬件支持。

**基本功能**

```
cmpxchg [memory], reg
```

1. **隐式比较**：自动比较 `EAX` 寄存器值与内存地址中的值
2. **条件交换**：若相等则将 `reg` 的值写入内存
3. **状态反馈**：通过 ZF 标志位返回操作结果

**原子性保证**

通过 `lock` 前缀实现：

- 锁定 CPU 总线或缓存行
- 禁止其他核心访问目标内存
- 确保操作不可分割

# 解决高竞争下CAS自旋消耗CPU的方案

在高并发场景下，当多个线程频繁使用`Unsafe`的CAS操作时，自旋会导致CPU资源大量浪费。以下是几种有效的解决方案：

## 一、基础优化方案

### 1. 指数退避策略

指数退避(Exponential Backoff)是一种**冲突解决算法**，当CAS操作失败时，不是立即重试，而是让线程等待一段逐渐增长的时间。

```
int retries = 0;
int maxRetries = 10;
long baseDelay = 1; // 毫秒

while (retries < maxRetries) {
    if (casOperation()) {
        return true; // 成功
    }
    
    // 计算退避时间：2^retries * baseDelay
    long waitTime = (long) Math.min(
        1000, // 最大等待时间上限
        baseDelay * (1 << retries) // 指数增长
    );
    
    Thread.sleep(waitTime);
    retries++;
}
return false; // 失败
```

**关键参数说明**

| 参数       | 作用           | 典型值     |
| :--------- | :------------- | :--------- |
| baseDelay  | 基础等待时间   | 1-10ms     |
| 指数因子   | 每次失败的乘数 | 2          |
| maxRetries | 最大尝试次数   | 5-15       |
| maxDelay   | 最大等待时间   | 100-1000ms |

**实际应用场景**

- **数据库事务冲突**：MySQL的InnoDB引擎使用类似策略处理死锁
- **网络请求重试**：HTTP客户端库的请求重试机制
- **分布式锁**：Redis的RedLock算法

## 二、高级解决方案

### 2. 自适应自旋（JVM风格）

```调整策略对比
class AdaptiveSpinning {
    private volatile int maxSpins = 100; // 当前最大自旋次数
    
    boolean adaptiveCAS(AtomicInteger atomic, int expect, int update) {
        int spins = 0;
        while (spins < maxSpins) {
            if (atomic.compareAndSet(expect, update)) {
                // 成功时放宽限制：增加最大自旋次数
                maxSpins = Math.min(1000, maxSpins + 10);
                return true;
            }
            spins++;
        }
        // 失败时收紧限制：减少最大自旋次数
        maxSpins = Math.max(10, maxSpins / 2);
        return false;
    }
}
```

**调整策略对比**

| 场景     | 调整方向 | 数学表达                       | 效果       |
| :------- | :------- | :----------------------------- | :--------- |
| 连续成功 | 增加上限 | `new = min(old + delta, MAX)`  | 更激进尝试 |
| 连续失败 | 减少上限 | `new = max(old / factor, MIN)` | 更保守退让 |

## 三、架构级解决方案

### 3. 队列化请求（AQS模式）

【深入理解aqs后再补充】

```
class QueuedCAS {
    private final AtomicReference<Thread> owner = new AtomicReference<>();
    private final ConcurrentLinkedQueue<Thread> queue = new ConcurrentLinkedQueue<>();
    
    public void executeCAS() {
        Thread current = Thread.currentThread();
        queue.add(current);
        
        // 自旋检查队首
        while (queue.peek() != current || 
              !owner.compareAndSet(null, current)) {
            Thread.yield();
        }
        
        try {
            // 执行CAS操作
        } finally {
            owner.set(null);
            queue.remove();
        }
    }
}
```