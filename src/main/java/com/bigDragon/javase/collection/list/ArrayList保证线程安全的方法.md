# 简介

本文介绍Java中的ArrayList、LinkedList如何进行线程安全的操作、为什么ArrayList不是线程安全的。

这几个问题也是Java后端面试中经常问到的问题。

# 线程不安全的原因分析-ArrayList

**ArrayList源码**

[ArrayList](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\collection\list\arrayList.md)

主要原因：
```
public boolean add(E e) {
    ensureCapacityInternal(size + 1);
    elementData[size++] = e;
    return true;
}
```
可以认为有三步骤，因为这三步不能保证原子性，所以有了线程安全问题
* 判断是否越界
* elementData[size] = e
* size++（本身都不线程安全）

> fail-fast机制也会出现问题

## 情景1：增加元素

增加元素过程中较为容易出现问题的地方是elementData[size++] = e;。赋值的过程可以分为两个步骤elementData[size] = e; size++;。

例如size为1，有两个线程，分别加入字符串“a”与字符串“b”：

[![img](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607033853628.png)](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607033853628.png)

如果四条语句按照：1,2,3,4执行，那么没有问题。

如果按照1,3,2,4来执行，就会出错。以下步骤按时间先后排序：

1. 线程1 赋值 element[1] = “a”; 随后因为时间片用完而中断;
2. 线程2 赋值 element[1] = “b; 随后因为时间片用完而中断;
     此处导致了之前所说的一个问题(有的线程没有输出); 因为后续的线程将前面的线程的值覆盖了。
3. 线程1 自增 size++; (size=2)
4. 线程2 自增 size++; (size=3)
     **此处导致了某些值为null的问题**。因为原来size=1, 但是因为线程1与线程2都将值赋值给了element[1]，导致了element[2]内没有值，被跳过了。此时指针index指向了3，所以导致了值为null的情况。

## **情景2：数组越界**

例如：size为2，数组长度限制为2，有两个线程，分别加入字符串“a”与字符串“b”：

[![img](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607033906340.png)](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607033906340.png)

 如果四条语句按照：1,2,3,4,5,6执行，那么没有问题。

前提条件: 当前size=2 数组长度限制为2。

如果按照1,3,2,4来执行，就会出错。以下步骤按时间先后排序：

1. 语句1：线程1 判断数组是否越界。因为size=2 长度为2，没有越界，将进行赋值操作。但是因为时间片用完而中断。
2. 语句4：线程2 判断数组是否越界。因为size=2 长度为2，没有越界，将进行赋值操作。但是因为时间片用完而中断。
3. 语句2,3：线程1重新获取到时间片，上文判断了数组不会越界，所以进行赋值操作element[size]=“a”，并且size++
4. 语句5,6：线程2重新获取到时间片，上文判断了数组不会越界，所以进行赋值操作。但是此时的size=3了，再执行element[3]=”b”导致了**数组越界**。

由此处可以看出因为数组的当前指向size并未进行加锁的操作，导致了数组越界的情况出现。

[![img](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607034030143.png)](https://static.way2j.com/way2j/html/wp-content/uploads/2023/06/20230607034030143.png)

# 线程安全的操作方法

## **ArrayList**

| **方法**                                    | **示例**                                                     | **原理**                                                     |
| ------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **Vector**                                  | List list = new ArrayList(); 替换为List arrayList = new Vector<>(); | 使用了synchronized关键字                                     |
| **Collections** **.synchronizedList(list)** | List<String> list = Collections.synchronizedList(new ArrayList<>()); 操作外部list，实际上修改的是原来list的数据。 注意：**需注意手动同步迭代器**，因为数据没用volatile，所以用迭代器的地方需要加锁，间接用到迭代器的地方也要加锁，比如：toString、equals、hashCode、containsAll等。 | 方法都加了synchronized修饰。加锁的对象是当前SynchronizedCollection实例。 |
| **JUC中的** **CopyOnWriteArrayList**        | CopyOnWriteArrayList<String> list =    new CopyOnWriteArrayList<String>(); 注意：**适用于读多写少**的并发场景。 | 读数据时不用加锁，因为它里边保存了数据快照。Write的时候总是要Copy（将原来array复制到新的array，修改后，将引用指向新数组）。任何可变的操作（add、set、remove等）都通过ReentrantLock 控制并发。 |

### Vector

```
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}
```

### Collections.synchronizedList(list)

方法执行链

```
public static <T> List<T> synchronizedList(List<T> list) {
    return (list instanceof RandomAccess ?
            new SynchronizedRandomAccessList<>(list) :
            new SynchronizedList<>(list));
}
```

构造器执行

```
SynchronizedList(List<E> list) {
    super(list);
    this.list = list;
}
```

加锁的对象是当前SynchronizedCollection实例

```
static class SynchronizedCollection<E> implements Collection<E>, Serializable {
    final Object mutex;     // Object on which to synchronize

    SynchronizedCollection(Collection<E> c) {
        this.c = Objects.requireNonNull(c);
        mutex = this;
    }
```

**正确的迭代器遍历方式**

为了线程安全地遍历同步列表，你需要在遍历时手动同步整个列表对象：

```
List<String> list = Collections.synchronizedList(new ArrayList<>());

// 添加元素的代码(自动线程安全)
list.add("item1");
list.add("item2");

// 遍历时需要手动同步
synchronized (list) {
    Iterator<String> iterator = list.iterator();
    while (iterator.hasNext()) {
        String item = iterator.next();
        System.out.println(item);
        // 处理item
    }
}
```

### JUC中的CopyOnWriteArrayList

CopyOnWriteArrayList 是 Java 并发包中提供的线程安全的 List 实现，它通过"写时复制"机制解决了 ArrayList 在多线程环境下的并发问题。

#### 核心原理

CopyOnWriteArrayList 的基本思想是：

- **读取操作**：直接读取当前数组，不需要加锁
- **写入操作**：先复制整个底层数组，在副本上进行修改，最后将副本替换原数组

这种"写时复制"(Copy-On-Write)的策略使得读操作完全无锁，写操作通过复制数组来保证线程安全。

#### 与 ArrayList 的对比

ArrayList 在多线程环境下主要存在两个问题：

1. 并发修改导致的结构破坏
2. 快速失败(fail-fast)迭代器

CopyOnWriteArrayList 通过以下方式解决这些问题：

1. 写操作加锁并复制数组，保证结构不被破坏
2. 迭代器基于创建时的数组快照，不会抛出 ConcurrentModificationException

#### 简单源码分析

```
public class CopyOnWriteArrayList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    // 使用 volatile 保证数组引用的可见性。主要因为读操作是无锁 
    private transient volatile Object[] array;
    final transient ReentrantLock lock = new ReentrantLock();

    // 获取当前数组
    final Object[] getArray() {
        return array;
    }

    // 设置新数组
    final void setArray(Object[] a) {
        array = a;
    }

    // 添加元素
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock(); // 加锁
        try {
            Object[] elements = getArray();
            int len = elements.length;
            // 复制新数组，长度+1
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e; // 在新数组上添加元素
            setArray(newElements); // 替换原数组
            return true;
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    // 获取元素 - 无锁操作
    public E get(int index) {
        return (E) getArray()[index];
    }

    // 迭代器基于创建时的数组快照
    public Iterator<E> iterator() {
        return new COWIterator<E>(getArray(), 0);
    }
    
    static final class COWIterator<E> implements ListIterator<E> {
        /** Snapshot of the array */
        private final Object[] snapshot;
        /** Index of element to be returned by subsequent call to next.  */
        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (! hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }
    }
}
```

#### 优缺点

**优点**：

1. 读操作完全无锁，性能极高
2. 迭代器不会抛出 ConcurrentModificationException
3. 实现简单，易于理解

**缺点**：

1. 写操作需要复制整个数组，内存消耗大
2. 数据一致性弱 - 读取可能不是最新数据
3. 不适合频繁修改的场景

#### 适用场景

CopyOnWriteArrayList 最适合读多写少的并发场景，如：

- 事件监听器列表
- 配置信息的存储
- 很少修改的缓存数据

这种设计体现了并发编程的一个重要原则：**当不可变对象被正确发布后，它就是线程安全的**。

## **LinkedList**

| **方法**                               | **示例**                                                     | **原理**                                                     |
| -------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| **Collections.synchronizedList(List)** | public static List linkedList = Collections.synchronizedList(new LinkedList()); | 所有方法都加了synchronized修饰。加锁的对象是当前SynchronizedCollection实例。 |
| **JUC中的ConcurrentLinkedQueue**       | ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue(); |                                                              |

> 先只记第一个

# 参考博客

[Java-ArrayList保证线程安全的方法](https://way2j.com/a/363)
