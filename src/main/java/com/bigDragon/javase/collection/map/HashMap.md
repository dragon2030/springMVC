进度

然后是线程安全相关的东西 

好的写法 1扰动函数 2取余计算算法 3扩容时候链表高低位 负载因子扩容时候容量和阈值

# 定义

HashMap 是 Java 集合框架中最重要的数据结构之一， 是 Java 中基于哈希表实现的 键值对（Key-Value） 映射容器，常用于快速查找、插入和删除操作，基于哈希表实现了 Map 接口。

# 一、HashMap 总体结构

```
java复制编辑public class HashMap<K,V> extends AbstractMap<K,V>
        implements Map<K,V>, Cloneable, Serializable
```

- 继承 `AbstractMap`
- 实现 `Map`：实现键值对存储结构
- 实现 `Cloneable`：可克隆
- 实现 `Serializable`：支持序列化

### 核心成员变量（含注释）

```
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // 16，默认容量
static final int MAXIMUM_CAPACITY = 1 << 30;		//扩容超过此值时，会直接固定1,073,741,824（即 2^30）
static final float DEFAULT_LOAD_FACTOR = 0.75f;     // 默认负载因子
static final int TREEIFY_THRESHOLD = 8;             // 链表转红黑树阈值
static final int UNTREEIFY_THRESHOLD = 6;           // 红黑树退化为链表阈值
static final int MIN_TREEIFY_CAPACITY = 64;         // 数组长度小于64时不会树化

transient Node<K,V>[] table;        // 哈希桶数组
transient int size;                 // 元素数量
int threshold;                      // 扩容阈值
final float loadFactor;             // 负载因子
transient int modCount;            // 修改次数（用于 fail-fast）
 // 没有名为 capacity 的成员变量，由table.length 表示当前 HashMap 的容量（桶的个数）
```

#### 1. 核心存储结构

- **`Node<K,V>[] table`**
  - 哈希桶数组，HashMap 的主存储结构
  - 初始为 null，第一次插入时初始化
  - 长度始终是 2 的幂（16, 32, 64...）
- **`Set<Map.Entry<K,V>> entrySet`**
  - 缓存 entry 的集合视图
  - 用于迭代器遍历等操作

#### 2. 容量相关变量

- **`int size`**
  - 当前存储的键值对数量
  - 直接反映 Map 的实际大小
  
- **`int threshold`**
  - 扩容阈值 = capacity * loadFactor
  - 当 size >= threshold 时触发扩容
  
- **`final float loadFactor`**
  - 负载因子（默认 0.75）
  
  - 计算公式为：阈值=数组容量×负载因子。当哈希表中的元素数量超过阈值时，触发扩容
  
  - 负载因子的核心作用
  
    * 减少哈希冲突，提升查询效率
  
      > 哈希冲突的代价：当多个键被哈希到同一个桶时，需要通过链表或红黑树处理冲突，这会增加查询、插入的时间复杂度（从 O(1) 退化为 O(n) 或 O(log n)）。
      >
      > 负载因子越低，哈希表的填充程度越小，键值对分布的稀疏性越高，哈希冲突的概率越低，操作效率越高。
      >
      > 负载因子越高，空间利用率越高，但哈希冲突的概率上升，性能下降。
  
    * 平衡时间与空间的矛盾
  
      > 若负载因子设为 1.0（元素填满数组才扩容）：
      > 优点：空间利用率最高。
      > 缺点：哈希冲突概率极高，尤其在数据量接近容量时，性能会显著下降。
  
      > 若负载因子设为 0.5（元素数量过半就扩容）：
      > 优点：哈希冲突概率极低，操作效率高。
      > 缺点：空间浪费严重（始终有一半的数组空间未使用）。
  
      > 默认负载因子 0.75 是一个经验值，通过统计学分析和实际测试验证，能够在时间与空间效率之间取得较好的平衡。
  
  * 自定义负载因子的场景
  
    > 在某些特殊情况下，可以调整负载因子：
    >
    > 内存敏感场景：若内存资源紧张，可适当提高负载因子（如0.9），但需容忍性能下降。
    >
    > 查询密集型场景：若对查询性能要求极高，可降低负载因子（如0.5），但需接受更高的内存占用。
  
- **`static final int DEFAULT_INITIAL_CAPACITY = 1 << 4`** (16)
  - 默认初始容量
  
- **`static final int MAXIMUM_CAPACITY = 1 << 30`**
  - 最大容量限制

#### 3. 树化相关变量

- **`static final int TREEIFY_THRESHOLD = 8`**
  - 链表转红黑树的阈值
- **`static final int UNTREEIFY_THRESHOLD = 6`**
  - 红黑树转链表的阈值
- **`static final int MIN_TREEIFY_CAPACITY = 64`**
  - 允许树化的最小表容量

#### 变量间关系分析

1. **容量扩容机制**：负载因子决定了扩容的阈值

   ```
   table.length == capacity
   threshold = capacity * loadFactor
   when size >= threshold → resize()
   ```

2. **树化条件**

   ```
   链表长度 >= TREEIFY_THRESHOLD (8) 
   && 
   table.length >= MIN_TREEIFY_CAPACITY (64)
   → 转换为红黑树
   ```

3. **链化条件**

   ```
   树节点数 <= UNTREEIFY_THRESHOLD (6) 
   → 转换回链表
   ```

4. **扩容与树化的协调**

   - 扩容时可能分散节点，使某些树退化为链表
   - 树化只在表足够大时进行，避免小表上的复杂结构

#### 关键设计思想

1. **空间换时间**

   - 负载因子 <1 保证桶不会全满，减少冲突
   - 树化提升最坏情况下的性能

2. **动态调整**

   - 按需扩容，避免一次性分配过大空间
   - 根据冲突情况自动切换链表/树结构

3. **性能平衡**

   - 默认 0.75 负载因子平衡空间利用率与查询效率
   - 树化阈值 8 基于泊松分布统计优化

   

#  内部类结构

#### Node 节点（链表/数组中的基本单元）

```
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;         // key 的 hash 值
    final K key;			//	存储键对象
    V value;				//	存储值对象
    Node<K,V> next;         // 指向下一个节点（用于链表）
}
```

**特点：**

- 用于处理哈希冲突时形成的链表
- 当链表长度小于8时使用
- 轻量级结构，占用内存较少

#### TreeNode 节点（红黑树中的节点）

```
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent; 	// 	红黑树的父节点引用
    TreeNode<K,V> left;		//	左子节点引用
    TreeNode<K,V> right;	//	右子节点引用
    TreeNode<K,V> prev;		//链表中的前驱节点（维护删除顺序）
    boolean red;			//	节点颜色（true=红色，false=黑色）
}
```

**继承的字段（来自LinkedHashMap.Entry）**：

- `int hash` - 键的哈希值
- `K key` - 键对象
- `V value` - 值对象
- `Node<K,V> next` - 下一个节点（仍然保留链表结构）

**特点**：

- 当链表长度≥8且桶数组长度≥64时，链表会转换为红黑树
- 同时维护了链表结构（通过next和prev）和红黑树结构
- 查找时间复杂度从O(n)提升到O(log n)
- 占用更多内存，但提高了长链表的查询效率

#### 为什么这样设计？

1. **性能平衡**：在大多数情况下使用轻量级的Node，只有在哈希冲突严重时才使用TreeNode
2. **平滑转换**：TreeNode保留了链表结构，便于树化和退化为链表
3. **兼容性**：TreeNode继承自Node，可以统一处理迭代等操作
4. **空间效率**：大多数桶只包含少量元素时，不会因红黑树结构浪费内存

# 构造器

1. 默认无参构造器

```
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // 默认加载因子 0.75
}
```

特点：

- 创建一个初始容量为 16 的空 HashMap
- 使用默认加载因子 0.75
- 实际数组(table)在第一次 put 操作时才初始化（懒加载）

2. **指定初始容量的构造器**

```
public HashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
}
```

特点：

- 创建指定初始容量的 HashMap
- 使用默认加载因子 0.75
- 实际容量会被调整为 2 的幂次方（通过 tableSizeFor 方法）

3. 指定初始容量和加载因子的构造器

```
public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}
```

特点：

- 可以完全自定义初始容量和加载因子
- 对参数进行有效性检查
- 实际容量会被调整为 2 的幂次方

4. 通过 Map 初始化的构造器

```
public HashMap(Map<? extends K, ? extends V> m) {
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    putMapEntries(m, false);
}
```

特点：

- 创建一个包含指定 Map 中所有映射的新 HashMap
- 使用默认加载因子 0.75
- 初始容量足够容纳指定 Map 中的映射

# 关键方法解析

------

### `hash()` 方法 - 哈希扰动函数

```
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

扰动函数是哈希表实现中的一种技术，主要用于优化哈希码的分布，减少哈希冲突。

#### 什么是扰动函数？

扰动函数是指在计算键(key)的哈希码后，对原始哈希码进行额外的处理（扰动），目的是让哈希码的高位和低位信息都能参与到最终的桶(bucket)定位计算中，从而改善哈希分布。

#### 为什么需要扰动函数？

1. **解决哈希冲突**：当不同键的哈希码低位相似时，直接取模会导致大量冲突
2. **利用高位信息**：原始哈希码的高位信息通常被取模运算忽略
3. **改善分布**：使哈希值分布更均匀，提高哈希表性能



#### 为什么取模运算会忽略高位信息？

在哈希表中，我们通常使用取模运算来确定键值对应存储在哪个桶(bucket)中：

```
bucketIndex = hash(key) % tableSize
```

假设：

- 哈希表大小(tableSize)是16（二进制 `10000`，即 `0x10`）
- 我们使用简单的取模运算 `hash % 16`

**示例：两个不同的哈希值**

```
哈希值1: 0x00001234 (十进制 4660)
哈希值2: 0x99991234 (十进制 2576980532)

计算桶索引：
4660 % 16 = 4
2576980532 % 16 = 4
```

**二进制视角**

```
16的二进制: 00000000 00000000 00000000 00010000
取模运算相当于保留最后4位(因为16=2^4)

哈希值1: 00000000 00000000 00010010 00110100 → 最后4位 0100 (4)
哈希值2: 10011001 10011001 00010010 00110100 → 最后4位 0100 (4)
```

**关键发现**

1. **取模运算本质**：当tableSize是2的幂时，`hash % tableSize` 等价于 `hash & (tableSize-1)`，即只保留哈希值的最后几位
2. **高位被忽略**：无论哈希值的高位是什么（如示例中的 `0x0000` vs `0x9999`），只要最后几位相同，结果就相同
3. **信息浪费**：哈希值的高31位（在32位哈希中）完全没有参与桶索引的计算

**这就是为什么需要扰动函数**

扰动函数（如Java的 `hash ^ (hash >>> 16)`）通过将高位信息"混合"到低位中，使得高位信息也能影响最终的桶索引计算，从而：

1. 更充分地利用整个哈希值的信息
2. 减少不同哈希值但低位相同导致的冲突
3. 使哈希分布更加均匀

**简单说，扰动函数解决了"哈希值高位不同但低位相同会被分配到同一个桶"的问题。**



###  `put()` 插入元素逻辑

**用途**：插入或更新键值对，处理哈希冲突，并在必要时扩容或树化链表。

**原理**：

1. 初始化table（懒加载）
2. 计算桶位置，如果为空则直接插入
3. 处理哈希冲突（链表或红黑树）
4. 更新结构修改计数和扩容检查

#### 源码

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

putVal方法：

```java
 /**
	hash  int   键(key)的扰动后哈希值
	key   K  要插入的键对象
	value V  要插入的值对象
	onlyIfAbsent   boolean  如果为true，则只在键不存在时才插入值（不覆盖现有值）。用于实现putIfAbsent()方法
	evict          boolean  如果为false，表示哈希表处于创建模式（用于LinkedHashMap的特定行为，普通HashMap中通常为true）
 */
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
    //Node<K,V>[] tab	Node数组	临时存储哈希表的桶数组引用
	//Node<K,V> p	Node	当前检查的节点(桶中的第一个节点)
	//int n	int	存储哈希表的当前容量(table.length)
	//int i	int	计算出的桶索引位置
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // ----------------------------- 1. 初始化table（懒加载） -----------------------------
    // 检查当前哈希表是否为空或长度为0（首次插入或扩容后未初始化）
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length; // 调用 resize() 懒初始化或扩容数组

    // ----------------------------- 2. 计算桶位置，如果为空则直接插入 -----------------------------
    // 计算桶索引: i = (n-1) & hash
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);// 桶为空，直接插入新节点
    else {
        //Node<K,V> e 临时存储遍历过程中找到的匹配节点
        //K k 临时存储当前检查节点的 key
        Node<K,V> e; K k;
        // ----------------------------- 3. 处理哈希冲突（链表或红黑树） -----------------------------
        // 检查桶的第一个节点是否匹配
        // 1. hash 相同 && key 相同，则覆盖旧值
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)// 桶是红黑树结构
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);// 调用红黑树插入方法
        else {// 否则就是链表结构，遍历链表尾插，并检查是否树化
            for (int binCount = 0; ; ++binCount) {//binCount临时变量 用于检查当前链表长度，判断是否树化
                if ((e = p.next) == null) {// 到达链表尾部
                    p.next = newNode(hash, key, value, null);// 尾部插入新节点
                    if (binCount >= TREEIFY_THRESHOLD - 1)// 链表长度 >= 8
                        treeifyBin(tab, i);// 尝试树化（需检查数组长度是否 >=64）
                    break;
                }
                // 找到相同 key 的节点，终止遍历
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;// 继续遍历下一个节点 此时e = p.next
            }
        }
        // 替换旧值并返回
        if (e != null) {  // 存在相同 key 的节点 否则e==null
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;// 替换旧值
            afterNodeAccess(e);  // 用于 LinkedHashMap 的回调（HashMap 中为空方法）
            return oldValue; // 返回旧值
        }
    }
    // ----------------------------- 4.更新结构修改计数和扩容检查 -----------------------------
    ++modCount;// 修改次数标记（用于快速失败机制）
    if (++size > threshold)// 元素数量超过阈值
        resize();// 触发扩容
    afterNodeInsertion(evict);  // 用于 LinkedHashMap 的回调（HashMap 中为空方法）
    return null;// 插入新节点时返回 null
}
```

------

#### **源码解析**

* 计算桶索引: i = (n-1) & hash（e.hash & (newCap - 1)）

  * **运算原理**：
    按位与操作（&）会将 e.hash 的二进制值与 newCap - 1 的二进制值逐位进行“与”操作。

    结果范围：0 到 newCap - 1，正好对应新数组的索引范围。

    **示例**：
    假设 newCap = 16（二进制 10000），则 newCap - 1 = 15（二进制 01111）：

    若 e.hash = 23（二进制 10111）：

    ```
    10111  // e.hash
    & 
    01111  // newCap - 1
    ------
    00111  = 7  → 索引位置为7
    ```

  * 替代取模运算。这个表达式实际上等价于 `hash % n`（当n是2的幂时），但使用位运算效率更高
  * 位运算(&)比取模运算(%)快得多，特别是在现代CPU上。这是HashMap高性能的关键设计之一。
  * 扰动函数 `hash ^ (hash >>> 16)` 和这个桶索引计算是配合使用的：（上文中“为什么取模运算会忽略高位信息”）
    1. **扰动函数**：将原始哈希码的高位信息混合到低位
    2. **桶索引计算**：使用低位信息确定位置

* 插入新节点:newNode(hash, key, value, null); 

  ```java
  Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
      return new Node<>(hash, key, value, next);
  }
  ```

* e != null

  * 如果第一个节点匹配，e !=null,值替换，否则e==null，第一个值不是插入节点key

  * 如果是链表，e = p.next，当其不等于null，就是从相同 key 的节点，终止遍历break出来的。否则e==null，这种情况下，要不链表末尾添加或者转树木，都不会走最后替换value逻辑

#### 关键点解析

1. **哈希扰动函数**：
   - 使用 `(h = key.hashCode()) ^ (h >>> 16)` 将高位信息混合到低位
   - 目的是减少哈希冲突
2. **懒初始化**：
   - 哈希表在首次put时才会真正创建（`resize()`）
3. **桶定位**：
   - `i = (n - 1) & hash` 高效计算桶位置（等价于取模但更快）
4. **节点处理三种情况**：
   - 桶为空：直接插入新节点
   - 桶为树节点：调用红黑树的插入方法
   - 桶为链表：遍历查找或插入
5. **树化条件**：
   - 链表长度达到 `TREEIFY_THRESLD`（默认8）
   - 且哈希表长度达到 `MIN_TREEIFY_CAPACITY`（默认64）
6. **值更新逻辑**：
   - 当键已存在时，根据 `onlyIfAbsent` 决定是否覆盖旧值
   - 返回旧值（不存在时返回null）
7. **扩容机制**：
   - 当 `size > threshold` 时触发扩容
   - 扩容后重新分布所有节点
8. **并发修改检测**：
   - `modCount` 用于迭代时的快速失败(fail-fast)检测
9. **性能优化点**：
   - 先比较hash值再比较key对象
   - 使用 `==` 快速判断相同引用
   - 最后才用 `equals()` 方法精确比较
10. **内存效率**：
    - 普通情况使用轻量级Node
    - 冲突严重时才转换为TreeNode

###  `resize()` 初始化或扩容数组

主要用途： resize() 初始化或扩容数组

**完整resize()流程**

1. 计算新容量和新阈值
2. 创建新数组
3. 遍历旧数组的每个桶：
   - 3.1如果桶中只有一个节点，直接计算新位置放入
   - 3.2如果是红黑树，执行红黑树的拆分操作
   - 3.3如果是链表，执行高低位链表拆分
     * 3.3.1将低位链表放在新表的原索引位置
     * 3.3.2将高位链表放在新表的原索引+oldCap位置

#### 源码

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;// 哈希桶数组
    int oldCap = (oldTab == null) ? 0 : oldTab.length;  // 旧容量
    int oldThr = threshold;  // 旧阈值
    int newCap, newThr = 0; // 新容量，新阈值

    // ----------------------------- 1.计算新容量和新阈值 -----------------------------
    if (oldCap > 0) {  // 当前数组已初始化过
        if (oldCap >= MAXIMUM_CAPACITY) {  // 超过最大容量（1 << 30）
            threshold = Integer.MAX_VALUE;  // 阈值设为最大值，不再扩容
            return oldTab;
        }
        // 新容量 = 旧容量 * 2，新阈值 = 旧阈值 * 2
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1;  // 新阈值为旧阈值的两倍
    }
    else if (oldThr > 0)  // 初始化时指定了容量（通过构造函数设置）
        newCap = oldThr;  // 新容量 = 初始化时指定的阈值
    else {  // 无参构造函数初始化
        newCap = DEFAULT_INITIAL_CAPACITY;  // 默认容量 16
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);  // 默认阈值 12
    }

    // 处理新阈值为0的情况（例如初始化时指定了容量但未计算阈值）
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;  // 新容量 * 负载因子
        newThr = (newCap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY) ?
                 (int)ft : Integer.MAX_VALUE;
    }
    threshold = newThr;  // 更新全局阈值
    //主要用于抑制编译器产生的两种警告：rawtypes（原始类型警告）和 unchecked（未检查的类型转换警告）。
    //作用是告诉编译器：“此处的类型转换和原始类型使用是开发者有意为之，且逻辑是安全的”。
    @SuppressWarnings({"rawtypes","unchecked"})
    // ----------------------------- 2.创建新数组 -----------------------------
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;  // 更新哈希表引用

    if (oldTab != null) {  // 旧数组不为空（扩容操作）
        // ----------------------------- 3.遍历旧数组的每个桶 -----------------------------
        for (int j = 0; j < oldCap; ++j) {  
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {  // 桶不为空
                oldTab[j] = null;  // 清理旧数组引用
                
                // --------------------- 3.1如果桶中只有一个节点，直接计算新位置放入 ---------------
                if (e.next == null)  
                    newTab[e.hash & (newCap - 1)] = e;  
                // --------------------- 3.2如果是红黑树，执行红黑树的拆分操作 --------------------
                else if (e instanceof TreeNode)  // 桶是红黑树结构
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);  // 拆分红黑树
                // --------------------- 3.3如果是链表，执行高低位链表拆分 ----------------------- 
                else {  // 桶是链表结构
					// 原链表分为两个链表：低位链表和高位链表
                    Node<K,V> loHead = null, loTail = null;  // 低位链表头尾指针
                    Node<K,V> hiHead = null, hiTail = null;  // 高位链表头尾指针
                    Node<K,V> next;
                    do {//每个桶 Node<K,V> e = oldTab[j]，循环拿e = e.next一直到链表末尾
                        next = e.next;
                        // 判断节点属于低位还是高位链表
                        if ((e.hash & oldCap) == 0) {  // 低位链表 节点依次复制到新Node
                            if (loTail == null) loHead = e; //第一个元素 loTail==null
                            else loTail.next = e; // 第二个元素 会依次放入loTail.next
                            loTail = e;
                        } else {  // 高位链表
                            if (hiTail == null) hiHead = e;
                            else hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 将链表分配到新数组
                    // --------------------- 3.3.1将低位链表放在新表的原索引位置 --------------------- 
                    if (loTail != null) {//低位链表 
                        loTail.next = null;
                        newTab[j] = loHead;  // 低位链表位置不变，放入原本的j位置
                    }
                    // --------------------- 3.3.2将高位链表放在新表的原索引+oldCap位置 -------------- 
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;  // 高位链表位置 + 旧容量（新容量的一半）
                    }
                }
            }
        }
    }
    return newTab;
}
```

####  源码解析：

* `(e.hash & oldCap) == 0` 判断高位低位链表

  * **背景：为什么要划分“低位链表”和“高位链表”**

    当 `HashMap` 扩容时，容量变为原来的 2 倍。由于哈希桶数量变了，每个元素的索引 `i`（原来位置）变成了：i 或 i + oldCap。

    > 这取决于元素的哈希值在扩容后新增的一位是 0 还是 1。

    因此，链表中的节点可以分为两类：

    - 低位链表：扩容后仍然在原来的索引位置 `i`
    - 高位链表：扩容后移动到 `i + oldCap` 的位置

  * **举例说明：**

    假设原始容量是 `16`，现在扩容为 `32`。

    一个元素的哈希值是 `h`，我们定位桶的索引用：

    ```
    index = h & (length - 1)
    ```

    扩容后，index 变为：

    ```
    h & (2 * length - 1)
    ```

    此时新增了一位（原来的 mask 是 `000...01111`，现在是 `000...11111`），**只需要看新加的那一位是 0 还是 1**。

  * resize()源码中的实现

    e.hash & oldCap == 0 → 放入低位链表，扩容后索引仍为 j

    否则（e.hash & oldCap == 1） → 放入高位链表，扩容后索引为 `j + oldCap`

  * 这样做的好处是：**扩容过程中无需重新计算哈希值，只需看一个二进制位**。

###  `get()` 获取元素

```
    public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }
```

getNode方法

```
final Node<K,V> getNode(int hash, Object key) {
	//Node<K,V>[] tab 	本地引用 table 数组，提升访问性能
	//Node<K,V> first 	目标桶（索引处）的第一个节点
	//Node<K,V> e 		用于链表或红黑树中继续查找的节点
	//K k				暂存比较过程中的 key
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;

    // -------------------- 基础检查 --------------------
    // 1. 检查哈希表是否为空或键所在的桶是否为空
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        
        // -------------------- 检查首节点 --------------------
        // 2. 检查首节点是否匹配（快速路径）
        if (first.hash == hash &&
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;

        // -------------------- 遍历链表或红黑树 --------------------
        if ((e = first.next) != null) {
            // 3. 如果是红黑树节点，调用 getTreeNode 查找
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);

            // 4. 遍历链表
            do {
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    return e;
            } while ((e = e.next) != null);
        }
    }
    // 5. 未找到返回 null
    return null;
}
```

#### 源码解析

* **hash计算**：`(n - 1) & hash` 通过位运算快速定位桶

* 主要查找逻辑

  若桶为空（first == null），直接返回 null。

  快速检查首节点：

  ​	直接比较首节点的哈希值和键，若匹配则直接返回（避免遍历）。

  处理红黑树：

  ​	若首节点是 TreeNode，调用 getTreeNode 方法（红黑树查找）。

  ​	红黑树查找时间复杂度为 O(log n)。

  遍历链表：

  ​	逐个比较节点的哈希值和键，若匹配则返回。

  ​	链表查找时间复杂度为 O(n)（但在合理哈希分布下接近 O(1)）。

### remove()删除元素

主要逻辑:

* 定位桶位置
* 查找匹配节点
* 节点移除操作
* 后续处理

注意点：

* remove不会进行扩容的反操作
  * 当HashMap中的元素数量超过其容量与负载因子的乘积时，就会进行扩容操作。然而，HashMap在remove操作多之后并不会进行相反的操作来缩小容量。HashMap的容量是固定的，不会因为remove操作的增加而改变。remove操作只会影响元素的删除，而不会影响HashMap的容量设置‌。

```
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}
```

**removeNode方法**

```
//int hash - 要移除的键的哈希值
//Object key - 要移除的键
//Object value - 要匹配的值 (可选)
//	当matchValue为true时，需要同时匹配key和value才会移除节点
//	当matchValue为false时，此参数被忽略
//boolean matchValue - 是否需要进行值匹配
//	true: 必须key和value都匹配才移除
// 	false: 只需key匹配即可移除
//boolean movable - 是否移动其他节点
// 	在红黑树结构调整时使用
//	true表示可以移动其他节点来完成删除操作
//	false表示在删除时不移动其他节点(主要用于迭代器场景)
final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
   	//Node<K,V>[] tab	当前哈希表数组的引用
   	//Node<K,V> p		当前检查的节点指针
   	//int n				哈希表数组的长度
	//int index			计算得到的桶索引位置
    Node<K,V>[] tab; Node<K,V> p; int n, index;

    // -------------------- 基础检查 --------------------
    // 1. 哈希表非空且键所在的桶非空
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (p = tab[index = (n - 1) & hash]) != null) {

        Node<K,V> node = null, e; K k; V v;

        // -------------------- 查找目标节点 --------------------
        // 2. 检查首节点是否匹配
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {
            if (p instanceof TreeNode)// 当前桶是红黑树结构
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {// 当前桶是链表结构
            //遍历链表查找
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;	//e = p.next 以此不断遍历下一个链表的节点
                } while ((e = e.next) != null);
            }
        }
		// --------------- 查找目标节点结束，目标节点值node --------
        // -------------------- 删除节点 --------------------
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            // 5. 处理红黑树删除
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);//其中包含了从红黑树退化到链表的判断
            // 6. 处理链表首节点删除
            else if (node == p)
                tab[index] = node.next;
            // 7. 处理链表中非首节点删除
            else
                p.next = node.next;

            // -------------------- 维护状态 --------------------
            ++modCount; // 修改计数
            --size;     // 更新大小
            afterNodeRemoval(node); // LinkedHashMap 回调（HashMap 为空）
            return node; // 返回被删除节点
        }
    }
    return null; // 未找到节点
}
```

### treeifyBin 链表树化

主要逻辑：

1. **检查是否需要树化**（容量是否足够）。
2. **将普通 `Node` 链表转换成 `TreeNode` 链表**（此时还不是红黑树）。
3. **调用 `treeify()` 方法构建红黑树**。

```
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    // 1. 若数组为空或长度小于64，优先扩容而非树化
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) // MIN_TREEIFY_CAPACITY=64
        resize();
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        // 2. 将链表转换为红黑树
        TreeNode<K,V> hd = null, tl = null; // 头尾指针
        do {
            // 3. 将 Node 替换为 TreeNode
            TreeNode<K,V> p = replacementTreeNode(e, null);
            if (tl == null)
                hd = p; // 初始化头节点
            else {
                p.prev = tl; // 维护双向链表
                tl.next = p;
            }
            tl = p; // 更新尾指针
        } while ((e = e.next) != null);

        // 4. 将 TreeNode 链表转换为红黑树
        if ((tab[index] = hd) != null)
            hd.treeify(tab); // 调用 TreeNode 的树化方法
    }
}
```

关键步骤：
扩容优先：若数组长度 < 64，优先扩容而非树化。

链表转换：

遍历原链表，将每个 Node 替换为 TreeNode。

维护 prev 和 next 指针形成双向链表。【没懂】

树化操作：调用 hd.treeify(tab)，通过红黑树规则调整结构。

具体实现见 TreeNode.treeify()，通过旋转和着色保证平衡。

# 红黑树浅解析

红黑树是一种**自平衡的二叉搜索树（BST）**，它通过特定的规则保持树的平衡，从而确保查找、插入和删除操作的最坏时间复杂度为 **O(log n)**。

【遗留问题 后面看志气啊学习的网课仔细整理一遍这个】

## 二叉搜索树（BST Binary Search Tree）

​	对有序数组排序，进行查找可以以二分法，但对插入删除就O(n)的时间复杂度，如果除了查询需要更多的插入和删除，可以用二叉搜索树。

## AVL平衡二叉树

##  红黑树的定义与性质

    1. 红黑树的五大规则
    规则1：每个节点非红即黑。
    
    规则2：根节点必须是黑。
    
    规则3：红节点的子节点必须为黑（即不能有连续红节点）。
    
    规则4：从任意节点到其所有叶子节点的路径上，黑节点数量相同（即黑高度平衡）。
    
    规则5：叶子节点（NIL空节点）视为黑节点。
    
    视频中的速记：左根右（前提是二叉搜索树） 根叶黑（根和叶null都是黑） 不红红（不存在连续的两个红色结点） 黑路同（任意结点到叶所有路径黑结点数量相同）

## **红黑树 vs AVL 树**

| 特性          | 红黑树                        | AVL 树                     |
| :------------ | :---------------------------- | :------------------------- |
| **平衡标准**  | 宽松（最长路径 ≤ 2×最短路径） | 严格（左右子树高度差 ≤ 1） |
| **查询效率**  | O(log n)（稍慢于 AVL）        | O(log n)（最优）           |
| **插入/删除** | 旋转和变色较少，更快          | 可能需要更多旋转，稍慢     |
| **适用场景**  | 频繁插入/删除（如 `HashMap`） | 查询密集型（如数据库索引） |

[博客](https://blog.csdn.net/cy973071263/article/details/122543826?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522b4bd1aede0a488bd0c441ea2f52a6b8c%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=b4bd1aede0a488bd0c441ea2f52a6b8c&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-122543826-null-null.142)
[视频课程](https://www.bilibili.com/video/BV16m421u7Tb/?spm_id_from=333.1387.homepage.video_card.click)

# 哈希冲突解决

### 1. **链地址法（Separate Chaining）**

- **原理**：
  * 每个哈希桶（槽位）维护一个链表（或红黑树），冲突的元素被存储在同一个桶的链表中。
  * 这种方法保持了简单性和稳定性，适合处理大多数哈希冲突。
- **适用场景**：
  - **Java HashMap**：JDK 1.8 之前使用链表，1.8 之后当链表长度超过 8 且哈希表大小 ≥64 时，链表会转换为红黑树以提高查询效率9。
  - **数据库索引**：某些数据库系统（如Redis的哈希表）采用链地址法处理冲突。

### 2. **开放寻址法（Open Addressing）**

- **原理**：冲突发生时，按照探测策略（线性探测、二次探测、双重哈希）寻找下一个可用槽位13。
- **适用场景**：
  - **ThreadLocalMap（Java）**：采用线性探测法，适用于线程本地存储，减少内存占用。
  - **嵌入式系统**：内存受限的环境下，开放寻址法比链地址法节省空间。

### 3. **再哈希法（Rehashing）**

- **原理**：使用多个哈希函数，若第一个哈希函数冲突，则尝试第二个，直到找到可用槽位。
- **适用场景**：
  - **Bloom Filter（布隆过滤器）**：使用多个哈希函数减少误判率。

### 4. **建立公共溢出区（Overflow Area）**【不用】

- **原理**：哈希表分为基本表和溢出表，冲突元素存入溢出区57。
- **适用场景**：
  - **早期数据库系统**：如IBM的DB2在某些索引结构中采用溢出区。
  - **内存受限的实时系统**：适用于冲突较少但需要快速查找的场景。

### 5. **一致性哈希（Consistent Hashing）**【没懂】

- **原理**：采用环形哈希空间，减少节点增减时的数据迁移。
- **适用场景**：
  - **分布式缓存（如Redis Cluster）**：用于数据分片，避免大规模数据重哈希。
  - **负载均衡（如Nginx、Consul）**：用于请求路由，减少服务器变动的影响。

### 总结

- **链地址法**适用于高冲突、动态扩展的场景（如Java HashMap）。
- **开放寻址法**适用于内存敏感、冲突较少的场景（如ThreadLocalMap）。
- **再哈希法**适用于需要高均匀分布的场景（如Bloom Filter）。
- **公共溢出区**适用于冲突较少但需要简单实现的场景（如某些嵌入式数据库）。
- **一致性哈希**适用于分布式系统（如Redis、Memcached）。
