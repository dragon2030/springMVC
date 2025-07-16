# Java ThreadLocal 介绍

ThreadLocal 是 Java 中一个特殊的类，它提供了线程局部变量。这些变量不同于普通的变量，因为每个访问该变量的线程都有自己独立初始化的变量副本，线程之间互不干扰。

> * 为什么叫"线程局部变量"？
>   * 从使用感受看：每个线程看到的是自己独立的变量副本
>
> * "本地线程栈"这个名称其实是对ThreadLocal机制的一种形象化比喻
>   * **真正的线程栈**：存储方法调用栈帧、局部变量（每个线程独立）

## 核心概念

ThreadLocal 的主要特点是：

- 为每个线程提供独立的变量副本
- 避免多线程环境下的共享变量竞争问题

## ThreadLocal解决了什么问题？

* 解决多线程的并发访问。ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。

## 主要方法

ThreadLocal 类提供了以下几个关键方法：

1. `T get()` - 返回当前线程的此线程局部变量的副本中的值

2. `void set(T value)` - 将当前线程的此线程局部变量的副本设置为指定值

3. `void remove()` - 删除当前线程的此线程局部变量的值

4. `protected T initialValue()` - 返回此线程局部变量的当前线程的"初始值"

   > ```
   >     private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
   >         @Override
   >         protected Integer initialValue() {
   >             return 0; // 初始值为0
   >         }
   >     };
   > ```

5. Java 8 引入了 `withInitial()` 静态方法，简化了 ThreadLocal 的初始化：

   > ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);



## 使用示例

```
public class ThreadLocalExample {
    // 创建一个ThreadLocal变量
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0; // 初始值为0
        }
    };

    public static void main(String[] args) {
        // 创建多个线程
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                // 获取当前线程的局部变量值
                int value = threadLocal.get();
                System.out.println(Thread.currentThread().getName() + " 初始值: " + value);
                
                // 修改值
                threadLocal.set(value + (int)(Math.random() * 100));
                System.out.println(Thread.currentThread().getName() + " 修改后: " + threadLocal.get());
                
                // 使用完后清除，防止内存泄漏
                threadLocal.remove();
            }).start();
        }
    }
}
```

## 应用场景

ThreadLocal 常用于以下场景：

1. **数据库连接管理**：为每个线程维护独立的数据库连接

2. **Session管理**：在Web应用中存储用户会话信息

3. **避免参数传递**：在多层调用中传递上下文信息

4. **线程安全工具类**：如SimpleDateFormat的非线程安全类的线程安全使用

   > ``` 
   > private static final ThreadLocal<SimpleDateFormat> dateFormat = 
   >     ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
   >    
   > public String formatDate(Date date) {
   >     try {
   >         return dateFormat.get().format(date);
   >        } finally {
   >         dateFormat.remove();  // 必须清理
   >     }
   > }
   > ```



## 内存泄漏问题（下面有原理分析）

ThreadLocal 可能导致内存泄漏，原因如下：

- ThreadLocal 变量保存在 Thread 的 threadLocals 属性中
- 当线程结束时，如果没有调用 remove() 方法，这些值可能不会被回收
- 特别是使用线程池时，线程会被重用，导致旧的值一直存在

**最佳实践**：

- 总是使用 try-finally 块确保 remove() 被调用

```
try {
    threadLocal.set(someValue);
    // ... 使用threadLocal
} finally {
    threadLocal.remove();
}
```

## ThreadLocal 设计

- 线程安全（无同步开销）
- 内存效率（惰性初始化）
- 使用简便（透明访问）

# Java ThreadLocal 源码解析

## 1. 核心数据结构

ThreadLocal 的实现依赖于两个关键类：

- `ThreadLocal`：对外暴露的API类

- `ThreadLocalMap`：Thread类的内部类，实际存储数据的哈希表

  > ```java
  > public class Thread implements Runnable {
  >     /* ThreadLocal values pertaining to this thread. This map is maintained
  >      * by the ThreadLocal class. */
  >     ThreadLocal.ThreadLocalMap threadLocals = null;
  > }
  > ```
  >
  > 

### ThreadLocalMap 结构

```
static class ThreadLocalMap {
    static class Entry extends WeakReference<ThreadLocal<?>> {
        Object value;
        Entry(ThreadLocal<?> k, Object v) {
            super(k);  // key是弱引用
            value = v;  // value是强引用
        }
    }
    
    private Entry[] table;
    private int size;
    private int threshold;
    // ... 其他方法
}
```

## 2. 关键方法解析

### get() 方法

#### get() 方法 - 获取线程局部变量

```
/**
 * 返回当前线程的线程局部变量副本值
 * 如果当前线程没有对应的值，则调用initialValue()进行初始化
 * 
 * 实现流程：
 * 1. 获取当前线程对象
 * 2. 获取线程的ThreadLocalMap
 * 3. 如果map存在，用当前ThreadLocal对象作为key查找entry
 * 4. 如果找到entry则返回值，否则初始化
 */
public T get() {
    // 获取当前执行线程对象
    Thread t = Thread.currentThread();
    
    // 获取线程的ThreadLocalMap（每个线程独立存储）
    ThreadLocalMap map = getMap(t);
    
    // 如果map已初始化
    if (map != null) {
        // 用当前ThreadLocal实例作为key查找entry，this指当前ThreadLocal对象
        ThreadLocalMap.Entry e = map.getEntry(this);
        
        // 如果找到对应的entry
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;  // 获取存储的值
            return result;
        }
    }
    
    // map不存在或未找到entry时的处理
    return setInitialValue(); // 初始化值
}

/**
 * 初始化当前线程的ThreadLocal值
 * 这是一个private方法，被get()调用
 */
private T setInitialValue() {
    T value = initialValue();  // 调用初始化方法（默认返回null）
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    
    // 如果map已存在则设置值，否则创建map
    if (map != null) {
        map.set(this, value);
    } else {
        createMap(t, value);  // 首次使用时创建map
    }
    return value;
}

/**
 * 创建线程的ThreadLocalMap（惰性初始化）
 * @param t 当前线程
 * @param firstValue 初始值
 */
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

#### get() 方法关键点分析：

1. **线程隔离实现**：通过`Thread.currentThread()`获取当前线程，保证线程安全
2. **延迟初始化**：首次调用时才创建ThreadLocalMap，避免内存浪费
3. **哈希查找**：使用ThreadLocal对象作为key在map中查找值
4. **初始化保护**：当没有值时调用`initialValue()`（可被子类重写）



### set() 方法 

#### set() 方法 - 设置线程局部变量

```
/**
 * 设置当前线程的线程局部变量副本值
 * 
 * 实现流程：
 * 1. 获取当前线程
 * 2. 获取线程的ThreadLocalMap
 * 3. 如果map存在直接设置值，否则创建map
 */
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    
    if (map != null) {
        // 如果map已存在，设置key-value对
        map.set(this, value);
    } else {
        // 首次设置值时创建map
        createMap(t, value);
    }
}

/**
 * ThreadLocalMap的set实现（关键内部方法）
 */
private void set(ThreadLocal<?> key, Object value) {
    Entry[] tab = table;
    int len = tab.length;
    
    // 计算初始哈希位置
    int i = key.threadLocalHashCode & (len-1);
    
    // 线性探测解决哈希冲突
    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();
        
        // 如果找到相同的key
        if (k == key) {
            e.value = value;  // 直接更新值
            return;
        }
        
        // 如果发现过期entry（key被GC回收）
        if (k == null) {
            replaceStaleEntry(key, value, i);  // 替换过期entry
            return;
        }
    }
    
    // 找到空槽位，创建新entry
    tab[i] = new Entry(key, value);
    int sz = ++size;
    
    // 检查是否需要扩容（没有清理出空槽位且超过阈值）
    if (!cleanSomeSlots(i, sz) && sz >= threshold) {
        rehash();  // 扩容并重新哈希
    }
}
```

#### set() 方法关键点分析

1. **惰性初始化**：直到第一次set/get时才创建ThreadLocalMap
2. **哈希冲突解决**：采用线性探测法（开放寻址）
3. **自动清理机制**：在set过程中会检查并清理key为null的过期entry
4. **扩容机制**：当size达到threshold（len*2/3）时会触发rehash
5. `replaceStaleEntry()`:关键清理方法，主要用于处理哈希表中遇到的"陈旧Entry"(stale entry)，即那些 key 已经被 GC 回收的 Entry 对象
   * **替换陈旧Entry**：当发现 key 为 null 的陈旧 Entry 时，用新的 key-value 对替换它
   * **清理相邻陈旧Entry**：在替换过程中会扫描并清理相邻的其他陈旧 Entry
   * **维护哈希表性能**：通过清理无效 Entry 避免哈希表性能退化

6. cleanSomeSlots:优化清理方法，它实现了**启发式清理**机制，在保证性能的同时渐进式地清理哈希表中的陈旧 Entry（stale entries）
   * **部分清理**：不像 `expungeStaleEntry()` 那样全量清理，而是选择性清理部分槽位
   * **性能优化**：通过控制清理数量避免清理操作带来的性能波动
   * **平衡策略**：在内存占用和性能之间取得平衡

### remove() 方法

#### remove() 方法 - 删除线程局部变量

```
/**
 * 删除当前线程的线程局部变量
 * 这是防止内存泄漏的关键方法！
 * 
 * 实现流程：
 * 1. 获取当前线程的ThreadLocalMap
 * 2. 如果map存在，移除当前ThreadLocal对应的entry
 */
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null) {
        m.remove(this);  // 调用ThreadLocalMap的remove
    }
}

/**
 * ThreadLocalMap的remove实现
 */
private void remove(ThreadLocal<?> key) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);
    
    // 线性探测查找entry
    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        if (e.get() == key) {  // 找到匹配的key
            e.clear();  // 清除WeakReference
            expungeStaleEntry(i);  // 彻底清理entry
            return;
        }
    }
}

/**
 * 清理过期entry的核心方法
 * @param staleSlot 开始清理的位置
 * @return 返回下一个可能为null的slot位置
 */
private int expungeStaleEntry(int staleSlot) {
    Entry[] tab = table;
    int len = tab.length;

    // 1. 清理当前槽位的entry
    tab[staleSlot].value = null;  // 释放value引用
    tab[staleSlot] = null;        // 移除entry
    size--;

    // 2. 向后探测继续清理
    Entry e;
    int i;
    for (i = nextIndex(staleSlot, len); (e = tab[i]) != null; i = nextIndex(i, len)) {
        ThreadLocal<?> k = e.get();
        
        // 发现过期entry
        if (k == null) {
            e.value = null;
            tab[i] = null;
            size--;
        } else {
            // 重新计算哈希位置
            int h = k.threadLocalHashCode & (len - 1);
            
            // 如果不在正确位置
            if (h != i) {
                tab[i] = null;  // 先移除当前位置
                
                // 放到正确的哈希位置
                while (tab[h] != null)
                    h = nextIndex(h, len);
                tab[h] = e;
            }
        }
    }
    return i;
}
```

#### remove() 方法关键点分析

1. **内存泄漏防护**：清除entry对value的强引用
2. **彻底清理**：调用expungeStaleEntry()处理后续可能存在的哈希冲突entry
3. **WeakReference处理**：显式清除key的弱引用
4. **线程安全**：只操作当前线程的map，无需同步

## Hash冲突解决

采用**线性探测法**（开放地址法）：

```
private void set(ThreadLocal<?> key, Object value) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1); // 计算初始位置
    
    // 线性探测
    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        ThreadLocal<?> k = e.get();
        if (k == key) { // 已存在则更新
            e.value = value;
            return;
        }
        if (k == null) { // 碰到过期entry触发替换
            replaceStaleEntry(key, value, i);
            return;
        }
    }
    // ... 创建新entry
}
```

## 内存泄漏本质解析

一句话核心原理

**Key是弱引用会被GC自动回收，但Value是强引用会一直存活，导致Entry无法被清除**，最终造成内存泄漏。

## 内存泄漏机制

1. **Entry 的弱引用设计**：

   - ThreadLocalMap 中的 Entry 继承自 WeakReference，其 key（ThreadLocal 对象）是弱引用

     > ```java
     > static class Entry extends WeakReference<ThreadLocal<?>> {
     >     Object value;
     >     Entry(ThreadLocal<?> k, Object v) {
     >         super(k);  // Key是弱引用（继承自WeakReference）
     >         value = v;  // Value是强引用
     >     }
     > }
     > ```

   - 但 Entry 的 value 是强引用

2. **垃圾回收时的行为**：

   - 当 ThreadLocal 外部强引用被置为 null 后，key 会因为弱引用被 GC 回收
   - 但 value 仍然保持强引用，导致无法被回收
   - 产生 key 为 null 但 value 不为 null 的 Entry

3. **长期存活的线程问题**：

   - 如果线程是线程池中的工作线程，通常会长期存活
   - 这些无效 Entry 会一直存在于 ThreadLocalMap 中
   - 导致 value 及其引用的对象无法被回收

### 泄漏链条分析

1. **强引用保持**：当线程来自线程池会长期存活
2. **Key的弱引用**：ThreadLocal对象只被Entry弱引用持有
3. **Value的强引用**：Entry中的value是强引用

```
Thread (强引用) -> ThreadLocalMap → Entry[] -> Entry -> Value (强引用)
                                                 ↑
                            ThreadLocal (弱引用) -┘
```

## 为什么需要 remove()

1. **主动清理**：
   - remove() 方法会显式删除当前线程的 ThreadLocalMap 中对应的 Entry
   - 彻底断开 value 的强引用链
2. **自动清理不完全**：
   - ThreadLocalMap 在 set/get 时会尝试清理无效 Entry（启发式清理）
   - 但这种清理不彻底，不能保证所有无效 Entry 都被清除



### 为什么必须手动remove？

| 操作         | Key（钥匙） | Value（物品） | 结果       |
| :----------- | :---------- | :------------ | :--------- |
| 不remove     | 被GC回收    | 仍然强引用    | 内存泄漏！ |
| 调用remove() | 被清除      | 被清除        | 完全释放   |

### 线程池场景特别危险示例

```
// 线程池线程会长期存活
ExecutorService pool = Executors.newFixedThreadPool(5);
pool.execute(() -> {
    ThreadLocal<BigObject> tl = new ThreadLocal<>();
    tl.set(new BigObject()); // 10MB大对象
    
    // 没有tl.remove() ➜ BigObject永远无法释放！
});
```

## 哈希算法与扩容机制

### 哈希算法

```
// ThreadLocal的哈希码生成器
private final int threadLocalHashCode = nextHashCode();

private static AtomicInteger nextHashCode = new AtomicInteger();

// 黄金分割数(2^32 * (√5-1)/2)
private static final int HASH_INCREMENT = 0x61c88647;

private static int nextHashCode() {
    return nextHashCode.getAndAdd(HASH_INCREMENT);
}
```

- 使用斐波那契散列减少冲突
- 原子计数器保证线程安全

### 扩容机制

```
private void rehash() {
    expungeStaleEntries();  // 先清理所有过期entry
    
    // size >= threshold * 3/4 时扩容
    if (size >= threshold - threshold / 4) {
        resize();
    }
}

private void resize() {
    Entry[] oldTab = table;
    int oldLen = oldTab.length;
    int newLen = oldLen * 2;  // 双倍扩容
    
    Entry[] newTab = new Entry[newLen];
    int count = 0;
    
    // 重新哈希
    for (Entry e : oldTab) {
        if (e != null) {
            ThreadLocal<?> k = e.get();
            if (k == null) {
                e.value = null;  // 清理过期entry
            } else {
                // 计算新位置
                int h = k.threadLocalHashCode & (newLen - 1);
                while (newTab[h] != null)
                    h = nextIndex(h, newLen);
                newTab[h] = e;
                count++;
            }
        }
    }
    
    setThreshold(newLen);
    size = count;
    table = newTab;
}
```

# 其他

## ThreadLocalMap 与 HashMap 结构对比分析

> 核心原因简析：开放寻址法更适合小规模数据、需要与Thread对象深度绑定

ThreadLocalMap 和 HashMap 都是基于哈希表实现的键值对存储结构，但它们在设计目标和具体实现上有显著差异。以下是两者的详细对比：

1. **核心结构对比**

| 特性             | ThreadLocalMap           | HashMap             |
| :--------------- | :----------------------- | :------------------ |
| **存储位置**     | Thread对象内部(线程私有) | 独立对象(线程共享)  |
| **键类型**       | ThreadLocal<?>(弱引用)   | 任意Object(强引用)  |
| **值类型**       | 强引用                   | 强引用              |
| **Entry结构**    | 继承WeakReference        | 普通静态内部类      |
| **哈希冲突解决** | 线性探测(开放寻址)       | 链表+红黑树(Java8+) |
| **初始容量**     | 16                       | 16                  |
| **负载因子**     | 2/3 (隐式)               | 0.75 (显式)         |

**2. 实现细节差异**

**2.1 Entry 设计**

```
// ThreadLocalMap.Entry
static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;  // 强引用
    Entry(ThreadLocal<?> k, Object v) {
        super(k);  // Key是弱引用
        value = v;
    }
}

// HashMap.Node (Java8+)
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;    // 强引用
    V value;        // 强引用
    Node<K,V> next; // 链表结构
}
```

关键区别：

- ThreadLocalMap 的 Key 是弱引用，HashMap 的 Key 是强引用
- ThreadLocalMap.Entry 没有 next 指针（开放寻址法）

**2.2 哈希冲突解决**

```
// ThreadLocalMap (线性探测)
int i = key.threadLocalHashCode & (len-1);
while (tab[i] != null)
    i = nextIndex(i, len);  // 环形后移

// HashMap (链表/红黑树)
int i = (n-1) & hash;
if ((e = tab[i]) != null) {
    if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
        // 处理相同key...
    // 处理链表或树...
}
```

**3. 设计哲学差异**

| 维度         | ThreadLocalMap         | HashMap          |
| :----------- | :--------------------- | :--------------- |
| **设计目标** | 线程局部变量的高效存储 | 通用键值存储     |
| **线程安全** | 天然线程安全(线程隔离) | 需要外部同步     |
| **内存管理** | 弱引用Key+主动清理     | 强引用+自动GC    |
| **性能优化** | 最小化同步开销         | 最大化并发吞吐量 |
| **扩容策略** | 清理优先于扩容         | 直接扩容         |

**4. 典型操作对比**

**4.1 get操作**

```
// ThreadLocalMap
Entry e = tab[i = key.threadLocalHashCode & (len-1)];
if (e != null && e.get() == key) return e.value;
// 线性探测继续查找...

// HashMap
Node<K,V> e = tab[(n-1) & hash];
if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
    return e.value;
// 遍历链表/树...
```

**4.2 set操作**

```
// ThreadLocalMap
if (k == null) { // 发现陈旧Entry
    replaceStaleEntry(key, value, i);
    return;
}
// 线性探测...

// HashMap
if (e != null) { // 已有元素
    if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
        // 替换逻辑...
    // 处理链表/树...
}
```

**5. 为什么ThreadLocalMap不直接用HashMap？**

1. **线程隔离需求**：
   - 需要与Thread对象深度绑定
   - HashMap的线程安全版本(ConcurrentHashMap)开销过大
2. **内存管理特殊要求**：
   - 需要弱引用Key机制
   - 需要精细控制Entry生命周期
3. **性能优化**：
   - 开放寻址法更适合小规模数据(线程局部变量通常不多)
   - 避免链表/树结构的额外内存开销
4. **清理机制**：
   - 需要支持主动清理陈旧Entry
   - HashMap的自动GC机制不能满足需求

**6. 总结**

ThreadLocalMap是一个高度特化的哈希表实现：

- **精简设计**：去除了HashMap中不必要的通用功能
- **内存敏感**：弱引用Key+主动清理机制
- **性能优先**：开放寻址法减少内存访问开销
- **线程隔离**：深度集成到Thread对象中

这种特殊设计使ThreadLocal能在保证线程安全的前提下，提供接近本地变量访问的性能表现，这是使用普通HashMap无法实现的。
