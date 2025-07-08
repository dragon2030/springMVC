# 线程不安全的原因分析-HashMap

* HashMap 的线程不安全主要体现在多个线程并发操作时可能导致数据不一致、循环链表等问题。
* HashMap 的线程不安全问题主要集中在 put/resize 过程中的非原子操作

## **1. put 方法核心流程**

1. 初始化table（懒加载）
2. 计算桶位置，如果为空则直接插入
3. 处理哈希冲突（链表或红黑树）
4. 更新结构修改计数和扩容检查

详细见[HashMap](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\collection\map\HashMap.md)



## **2. put方法线程不安全的具体体现**

### **（1）多线程 put 导致数据覆盖**

**场景**：两个线程同时执行 `put`，且 key 的哈希值相同
**问题代码**：

```
if ((p = tab[i = (n - 1) & hash]) == null)
    tab[i] = newNode(hash, key, value, null); // ⚠️ 竞态条件
```

**分析**：

- 线程 A 和线程 B 同时判断 `tab[i] == null` 为 true
- 线程 A 先执行 `tab[i] = newNode(...)`，但线程 B 会覆盖这个值
- **结果**：其中一个线程的 put 操作丢失

------

### **（2）扩容时导致循环链表（JDK7 问题）**【没看懂】

**场景**：多线程同时触发 `resize()`
**问题代码（JDK7 链表头插法）**：

```
void transfer(Entry[] newTable) {
    for (Entry<K,V> e : table) {
        while (e != null) {
            Entry<K,V> next = e.next; // ⚠️ 线程A执行到这里挂起
            e.next = newTable[i];     // 线程B先完成扩容
            newTable[i] = e;          // 导致线程A继续时形成环形引用
            e = next;
        }
    }
}
```

**分析**：

- JDK7 使用头插法扩容，多线程操作可能导致链表成环
- **结果**：后续 `get()` 操作可能陷入死循环（JDK8 改为尾插法修复）

------

### **（3）size 计数不准确**

**场景**：多线程同时修改 size
**问题代码**：

```
if (++size > threshold) // ⚠️ 非原子操作
    resize();
```

**分析**：

- `++size` 非原子操作，可能导致多个线程累加失效
- **结果**：实际元素数量与 `size` 值不一致

------

### **（4）modCount 并发修改异常**

**场景**：迭代时并发 put
**问题代码**：

```
++modCount; // 快速失败机制（fail-fast）的修改计数器
```

**分析**：

- 线程 A 迭代时检查 `modCount`，线程 B 同时 put 修改 `modCount`
- **结果**：抛出 `ConcurrentModificationException`

##  3. resize 方法核心流程

主要用途： resize() 初始化或扩容数组

**完整resize()流程**

1. 计算新容量和新阈值
2. 创建新数组
3. 遍历旧数组的每个桶：
   - 如果桶中只有一个节点，直接计算新位置放入
   - 如果是红黑树，执行红黑树的拆分操作
   - 如果是链表，执行高低位链表拆分
     * 将低位链表放在新表的原索引位置
     * 将高位链表放在新表的原索引+oldCap位置

## 2. resize() 方法中的线程不安全点

### (1) 多线程扩容导致数据丢失

核心问题代码：

```
Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
table = newTab;  // 问题点1：新数组的可见性问题
```

**并发场景**：

- 线程A和线程B同时检测到需要扩容
- 线程A完成扩容并设置新table
- 线程B使用旧的table引用继续操作
- **结果**：线程B的操作会丢失

### (2) 链表拆分的竞态条件

核心问题代码：

```
// 低位链表
Node<K,V> loHead = null, loTail = null;
// 高位链表
Node<K,V> hiHead = null, hiTail = null;
Node<K,V> next;
do {
    next = e.next;
    if ((e.hash & oldCap) == 0) {
        if (loTail == null)
            loHead = e;
        else
            loTail.next = e;
        loTail = e;
    }
    else {
        if (hiTail == null)
            hiHead = e;
        else
            hiTail.next = e;
        hiTail = e;
    }
} while ((e = next) != null);
```

**并发场景**：

- 两个线程同时进行链表拆分
- 由于没有同步控制，可能导致链表节点丢失或重复
- **结果**：扩容后部分数据丢失或链表结构损坏

### (3) 树化操作的并发问题

核心问题代码：

```
if (binCount >= TREEIFY_THRESHOLD - 1)
    treeifyBin(tab, hash);
```

**并发场景**：

- 多个线程同时触发树化操作
- 可能导致重复树化或树结构不一致
- **结果**：红黑树结构可能被破坏

# HashMap线程安全的操作方法

**线程安全Map的三种方法**

| **方法**                        | **示例**                                                     | **原理**                                                     | **性能**                       |
| ------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------ |
| **Hashtable**                   | Map<String, Object> map =   new Hashtable<>();               | synchronized修饰get/put方法。 方法级阻塞，只能同时一个线程操作get或put | 很差。                         |
| **Collections.synchronizedMap** | Map<String, Object> map = Collections.synchronizedMap( new HashMap<String, Object>()); | 所有方法都使用synchronized修饰。                             | 很差。 和**Hashtable差不多。** |
| **JUC中的ConcurrentHashMap**    | Map<String, Object> map =     new ConcurrentHashMap<>();     | 每次只给一个桶（数组项）加锁。                               | 很好。                         |

## Hashtable

所有方法都用了synchronized修饰 

```
public synchronized V get(Object key) {    // ... } 
public synchronized V put(K key, V value) {    // ... } 
```

## Collections.synchronizedMap

**Collections.synchronizedMap线程安全的原理**

此方法返回的是：Collections的静态内部类SynchronizedMap

内部有个mutex对象，就是构造器传入参数map，以此作为同步监视器加锁。

```
private static class SynchronizedMap<K,V> implements Map<K,V>, Serializable {
	private final Map<K,V> m;     // Backing Map
	final Object      mutex;        // Object on which to synchronize

    SynchronizedMap(Map<K,V> m) {
        this.m = Objects.requireNonNull(m);
        mutex = this;
    }
    public V get(Object key) {
        synchronized (mutex) {return m.get(key);}
    }
    public V put(K key, V value) {
        synchronized (mutex) {return m.put(key, value);}
    }
    public V remove(Object key) {
        synchronized (mutex) {return m.remove(key);}
    }
```

## JUC中的ConcurrentHashMap

### ConcurrentHashMap 的线程安全设计

JDK8 的 ConcurrentHashMap 放弃了 JDK7 的分段锁设计，转而采用更高效的 **CAS + synchronized** 组合方案6。其核心思想是：

1. **减小锁粒度**：只锁住单个桶（链表头节点或树根节点），而非整个表
2. **无锁化读取**：get 操作完全不加锁，依赖 volatile 和 final 保证可见性
3. **CAS 乐观锁**：在可能的情况下使用 CAS 替代重量级锁
4. **并发扩容**：支持多线程协同扩容，提高扩容效率

> 分段锁:将整个哈希表划分为多个独立段（Segment），每个段拥有自己的锁

### 1. 数据结构设计

```
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val;  // volatile 保证可见性
    volatile Node<K,V> next; // volatile 保证可见性
    // ...
}
```

Node 节点的 val 和 next 字段都用 volatile 修饰，保证了多线程下的内存可见性6。这是 get 操作无需加锁的基础。

### 2. put 操作的线程安全实现

put 操作的核心方法是 `putVal()`，其线程安全主要通过以下机制保证：

#### (1) 初始化表的线程安全【没懂】

```
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        if ((sc = sizeCtl) < 0)
            Thread.yield(); // 其他线程正在初始化，让出CPU
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) { // CAS 抢锁
            try {
                // 初始化逻辑...
            } finally {
                sizeCtl = sc;
            }
        }
    }
    return tab;
}
```

通过 **CAS 操作** 保证只有一个线程能执行初始化，其他线程要么等待要么协助。

#### (2) 空桶插入的线程安全

```
else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
    if (casTabAt(tab, i, null, new Node<K,V>(hash, key, value, null)))
        break; // CAS 成功则插入完成
}
```

对于空桶，使用 `casTabAt()`（底层是 CAS）尝试插入新节点，避免锁开销。

#### (3) 非空桶的线程安全

```
else {
    V oldVal = null;
    synchronized (f) { // 锁住桶的头节点
        if (tabAt(tab, i) == f) { // 再次验证
            if (fh >= 0) { // 链表
                // 遍历链表插入/更新
            } 
            else if (f instanceof TreeBin) { // 红黑树
                // 红黑树插入
            }
        }
    }
    // 判断是否需要树化
}
```

对非空桶的操作使用 **synchronized 锁住头节点**，锁粒度极小。

### 3. get 操作的线程安全实现

```
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode());
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {
        if ((eh = e.hash) == h) {
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        else if (eh < 0) // 特殊节点（扩容或树节点）
            return (p = e.find(h, key)) != null ? p.val : null;
        while ((e = e.next) != null) {
            // 遍历链表
        }
    }
    return null;
}
```

get 操作完全**无锁**，依赖以下保证线程安全：

1. `table` 本身是 volatile 的
2. Node 的 val 和 next 是 volatile 的
3. 节点的 key 和 hash 是 final 的

### 4. 扩容的线程安全实现【没懂】

ConcurrentHashMap 的扩容（`transfer()` 方法）支持多线程协同：

1. 通过 `sizeCtl` 标志位控制扩容状态
2. 线程发现正在扩容会协助迁移数据
3. 使用 `ForwardingNode` 标记已迁移的桶9

```
if ((fh = f.hash) == MOVED) // MOVED = -1
    tab = helpTransfer(tab, f); // 协助扩容
```

这种设计避免了 HashMap 扩容时的死循环问题，同时提高了扩容效率。

## 三、与 Hashtable 的对比

ConcurrentHashMap 相比 Hashtable 的优势：

| 特性   | Hashtable | ConcurrentHashMap |
| :----- | :-------- | :---------------- |
| 锁粒度 | 整个表    | 单个桶            |
| 并发度 | 1         | 理论上是桶数量    |
| get 锁 | 需要      | 不需要            |
| 迭代器 | 强一致性  | 弱一致性          |
| 性能   | 低        | 高                |

Hashtable 通过在方法上加 synchronized 实现线程安全，导致并发度极低。而 ConcurrentHashMap 的并发度理论上等于桶的数量。

## 四、总结

ConcurrentHashMap 通过以下设计解决 HashMap 的线程安全问题：

1. **CAS + synchronized 组合锁**：空桶用 CAS，非空桶用 synchronized，平衡性能与安全性69
2. **volatile 变量**：保证内存可见性，实现无锁读取
3. **细粒度锁**：只锁单个桶，极大提高并发度
4. **并发扩容**：多线程协同扩容，避免阻塞
5. **不可变设计**：key 和 hash 的 final 保证

这些设计使得 ConcurrentHashMap 在高并发场景下既能保证线程安全，又能保持较高的性能，是 Java 并发编程中最常用的并发容器之一。

# 参考博客

[Java-HashMap保证线程安全的方法](https://way2j.com/a/396)