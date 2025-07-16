# ArrayList
JDK8 中的 `ArrayList` 是 Java 集合框架中最常用的动态数组实现。它的底层是一个可以动态扩容的数组，支持快速随机访问。

## 一、类定义

```
java复制编辑public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```

- **继承了 AbstractList**：提供部分 List 实现。
- **实现了 List 接口**：定义了线性顺序集合的行为。
- **实现了 RandomAccess 接口**：支持快速随机访问。
- **实现了 Cloneable 和 Serializable 接口**：支持克隆与序列化。



## 二、关键成员变量

```
java复制编辑private static final int DEFAULT_CAPACITY = 10; // 默认初始容量

private static final Object[] EMPTY_ELEMENTDATA = {}; // 空数组实例
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {}; // 默认容量的空数组

transient Object[] elementData; // 存储元素的数组
private int size; // 当前元素个数
```

- `elementData` 是真正存储元素的数组。
- `size` 表示当前实际存储的元素数量，不是数组长度。

## 三、构造方法

1. 默认构造函数

```
java复制编辑public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

- 初始时数组为空，真正添加元素时才分配容量（懒加载）。

2. 指定初始容量

```
java复制编辑public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
    }
}
```

**通过集合构造**

```
public ArrayList(Collection<? extends E> c) {
    elementData = c.toArray();
    if ((size = elementData.length) != 0) {
        if (elementData.getClass() != Object[].class)
            elementData = Arrays.copyOf(elementData, size, Object[].class);
    } else {
        this.elementData = EMPTY_ELEMENTDATA;
    }
}
```

## 四、核心方法解析

### 1. 添加元素 `add(E e)`

```
public boolean add(E e) {
    ensureCapacityInternal(size + 1);
    elementData[size++] = e;
    return true;
}
```

- **ensureCapacityInternal** 保证容量足够，不够就扩容。

- 添加后 `size++`。

- ```
  //方法调用链
  add() → ensureCapacityInternal() → ensureExplicitCapacity() → grow()
  ```

### 2. 容量扩展机制

```
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) { //如果是默认容量的空数组 
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
```

- ensureCapacityInternal。确定 ArrayList **所需的最小容量**，处理首次添加元素时的特殊情况

  * 检查当前存储数组是否是默认的空数组（`DEFAULTCAPACITY_EMPTY_ELEMENTDATA`），这个空数组是使用无参构造函数创建 ArrayList 时的初始值
  * 如果是空数组，则在默认容量（10）和所需最小容量中取较大值，这确保了首次添加元素时至少分配 DEFAULT_CAPACITY（10）的空间
  * 将计算后的最小容量传递给 ensureExplicitCapacity 方法

- ensureExplicitCapacity。判断是否需要扩容，记录结构修改次数（用于快速失败机制）

  * `modCount++`：增加修改计数器，用于迭代器的快速失败机制

  * 检查所需最小容量是否超过当前数组长度

    * 如果超过，则调用 grow 方法进行扩容

    * 否则不做任何操作

### 3. grow(int minCapacity) —— 扩容逻辑

```
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 扩容1.5倍
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
}

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
```

- 每次扩容为原来的 1.5 倍。

- 如果还是不够就按最小需求分配。

- 最大容量限制为 `Integer.MAX_VALUE - 8`。private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

- ```
  //执行数组拷贝
  elementData = Arrays.copyOf(elementData, newCapacity);
  ```

  - 创建新数组并将旧数组内容拷贝到新数组
  - 这是一个相对耗时的操作，这也是为什么 ArrayList 的扩容会影响性能

------

### 4. 获取元素 `get(int index)`

```
public E get(int index) {
    rangeCheck(index);
    return elementData(index);
}

private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

- 检查越界后返回对应位置的元素。

------

### 5. 删除元素 `remove(int index)`

```
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // clear to let GC do its work
    return oldValue;
}
```

1. `rangeCheck(index);`

```
private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

- 确保要删除的位置 `index` 在 `0 ~ size-1` 之间。
- 防止越界操作，保障程序健壮性。

------

2. `modCount++;`

- `modCount` 是 `AbstractList` 中的字段，用于记录集合结构变化的次数。
- 与 `Iterator` 的 `fail-fast` 机制有关：如果在遍历过程中有结构修改操作（如 `remove()`），就会抛出 `ConcurrentModificationException`。

------

3. `E oldValue = elementData(index);`

```
@SuppressWarnings("unchecked")
E elementData(int index) {
    return (E) elementData[index];
}
```

- 先将被删除的元素取出保存，以便最后返回。

------

4. `int numMoved = size - index - 1;`

- 计算从删除位置之后，有多少个元素需要往前移动。
- 如果删除的是最后一个元素，则 `numMoved == 0`，无需移动。

------

5. `System.arraycopy(...)`

```
System.arraycopy(elementData, index + 1, elementData, index, numMoved);
```

- 把 `index+1` 之后的元素，整体前移一位，覆盖掉要删除的元素。
- 这是 `ArrayList` 删除操作性能不如 `LinkedList` 的原因之一 —— 删除中间元素要移动大量数据。

------

6. `elementData[--size] = null;`

- 执行 `size--`，然后将最后一个元素（现在是多余的副本）设为 `null`。
- **目的：防止内存泄露**。让 JVM GC 能及时回收对象引用。

------

7. `return oldValue;`

- 返回原来被删除的元素。

------

## 五、fail-fast 机制

- 所有操作中都会涉及 `modCount` 变量。
- 在 `Iterator` 遍历时，会比较 `expectedModCount` 和当前 `modCount`，发现不同就抛出 `ConcurrentModificationException`。

------

## 六、总结

| 特性         | 说明                      |
| ------------ | ------------------------- |
| 底层结构     | 动态数组 `Object[]`       |
| 默认容量     | 10（首次添加时分配）      |
| 扩容策略     | 每次扩容为旧容量的 1.5 倍 |
| 查询性能     | O(1)                      |
| 插入/删除    | O(n)（移动元素）          |
| 是否线程安全 | 否，需手动同步            |





在 Java 8 的 ArrayList 中，数组的初始化大小并不是在构造函数中直接分配的，而是在第一次调用 add 方法时通过动态扩容机制完成的。以下是源码解析和关键步骤说明：

1. 构造函数分析
JDK8 的 ArrayList 默认构造函数如下：

java
复制
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA; // 初始化为空数组
}
默认构造函数不会直接初始化数组，而是将内部数组 elementData 赋值为 DEFAULTCAPACITY_EMPTY_ELEMENTDATA（一个空数组）。

2. add() 方法源码解析
当调用 add(E e) 方法时：

java
复制
public boolean add(E e) {
    ensureCapacityInternal(size + 1); // 确保容量足够
    elementData[size++] = e;          // 插入元素
    return true;
}
关键逻辑在 ensureCapacityInternal 方法中。

3. 初始化数组的关键步骤
步骤 1: 计算最小容量
java
复制
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity); // 取默认容量 10 和 minCapacity 的较大值
    }
    ensureExplicitCapacity(minCapacity);
}
如果 elementData 是空数组（即第一次调用 add），则初始化容量为 DEFAULT_CAPACITY（默认值 10）。

步骤 2: 检查是否需要扩容
java
复制
private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    if (minCapacity - elementData.length > 0) {
        grow(minCapacity); // 触发扩容
    }
}
如果所需容量超过当前数组长度，调用 grow() 方法扩容。

步骤 3: 分配初始数组（扩容逻辑）
java
复制
private void grow(int minCapacity) {
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 默认扩容 1.5 倍
    if (newCapacity - minCapacity < 0) {
        newCapacity = minCapacity; // 初始时直接使用 minCapacity（10）
    }
    elementData = Arrays.copyOf(elementData, newCapacity); // 分配新数组
}
第一次调用 grow() 时，oldCapacity 为 0，newCapacity 会被设置为 minCapacity（即 10）。

通过 Arrays.copyOf 创建长度为 10 的新数组，完成初始化。

4. 关键结论
初始化触发时机：
第一次调用 add() 方法时，如果 elementData 是空数组，会触发数组初始化。

初始容量大小：
默认初始容量为 10，由 DEFAULT_CAPACITY 定义。

实际分配代码位置：
在 grow() 方法中通过 Arrays.copyOf 分配大小为 10 的数组。

流程图解
复制
add() 方法调用
    ↓
ensureCapacityInternal(size + 1)
    ↓
如果是第一次添加（elementData 为空）
    ↓
minCapacity = max(10, 1) → 10
    ↓
ensureExplicitCapacity(10)
    ↓
触发 grow(10)
    ↓
分配长度为 10 的新数组
通过这种懒加载（Lazy Initialization）策略，ArrayList 避免了在创建时立即分配内存，优化了空列表的内存占用。
