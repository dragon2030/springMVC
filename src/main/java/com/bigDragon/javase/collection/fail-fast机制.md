## 一、什么是 fail-fast（快速失败）机制？

> **fail-fast 是 Java 集合（如 ArrayList、HashMap 等）在并发修改时的一种错误检测机制。**
>
> 它能在**集合结构被修改但未通过安全方式（如 Iterator）操作时，立刻抛出异常**，防止出现难以发现的 bug。

### 📌 关键词：

- 并发修改：结构性修改（增/删元素）
- 非预期：未通过 `iterator.remove()` 等安全方式
- 抛出异常：`ConcurrentModificationException`

------

## 二、fail-fast 的触发条件

以 `ArrayList` 为例：

### ✅ 正常使用（不会触发）：

```
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("A")) {
        it.remove(); // 安全方式
    }
}
```

> 使用迭代器进行remove删除操作

### ❌ 错误使用（会触发）：

```
java复制编辑Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("A")) {
        list.remove("A"); // 非安全修改方式
    }
}
```

------

> 直接通过集合进行remove删除操作

## 三、源码分析（以 ArrayList 为例）

`ArrayList` 的 fail-fast 是通过 `modCount` 实现的。

### 1. `modCount`：结构性修改计数器

```
protected transient int modCount = 0;
```

- 每次 `add/remove/clear` 操作都会执行 `modCount++`

```
java复制编辑public boolean add(E e) {
    ensureCapacityInternal(size + 1);
    modCount++; // 修改点
    elementData[size++] = e;
    return true;
}
```

------

### 2. 迭代器中的 `expectedModCount`

```
private class Itr implements Iterator<E> {
    int cursor;       // 当前游标
    int lastRet = -1; // 上一次返回元素的索引
    int expectedModCount = modCount; // 创建时记录

    public E next() {
        checkForComodification(); // 每次 next() 前都会检查
        ...
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}
```

- 每次调用 `next()` 或 `remove()` 都会调用 `checkForComodification()`。
- 如果发现 `modCount != expectedModCount`，说明集合被“外部”修改，立即抛出异常。

### 集合 remove()与迭代器 remove()

#### `Iterator` 的 `remove()` 源码（JDK 8）

这是 `ArrayList` 的内部类 `Itr` 实现的 `remove()`：

```
java复制编辑public void remove() {
    if (lastRet < 0)
        throw new IllegalStateException(); // 必须先调用 next()

    checkForComodification(); // 检查 modCount 和 expectedModCount 是否一致

    try {
        ArrayList.this.remove(lastRet); // 删除元素，modCount 会变化
        cursor = lastRet;
        lastRet = -1;
        expectedModCount = modCount; // 同步 expectedModCount，防止 fail-fast
    } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
    }
}
```

------

#### arraylist删除元素 `remove(int index)`

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

[ArrayList](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\collection\list\arrayList.md)

#### 集合 remove()与迭代器 remove()关键差异总结

| 对比项                      | 集合 `remove()` | 迭代器 `remove()`  |
| --------------------------- | --------------- | ------------------ |
| 属于                        | `ArrayList` 类  | `ArrayList.Itr` 类 |
| 是否增加 `modCount`         | ✅ 会            | ✅ 会               |
| 是否更新 `expectedModCount` | ❌ 不会          | ✅ 会               |
| 触发 fail-fast              | ✅ 会            | ❌ 不会             |
| 正确使用场景                | 非遍历期间删除  | 遍历过程中删除     |

## 四、为什么要设计 fail-fast？

### ✅ 优点：

- **及早发现问题**：防止你在遍历过程中不小心修改集合，造成逻辑混乱或数据错误。
- **排查简单**：立刻抛出异常而不是出现“奇怪行为”，有助于快速定位问题。

### ⚠️ 缺点：

- 不是线程安全的方案，只是**检测问题**，**不能解决并发修改问题**。
- 多线程环境中需用 `Collections.synchronizedList()` 或 `CopyOnWriteArrayList` 替代。

------

## 五、实战举例

### ❌ 错误例子（抛出异常）：

```
java复制编辑List<String> list = new ArrayList<>();
list.add("A");
list.add("B");
list.add("C");

for (String s : list) {
    if (s.equals("B")) {
        list.remove(s); // 报错：ConcurrentModificationException
    }
}
```

### ✅ 正确例子（使用迭代器删除）：

```
java复制编辑Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String s = iterator.next();
    if (s.equals("B")) {
        iterator.remove(); // 安全删除
    }
}
```

------

## 六、总结一句话

> Java 的 fail-fast 机制，是通过在迭代器中维护一个 `expectedModCount`，来检测集合在遍历过程中是否被修改的机制。发现结构性变动立即抛异常，以防潜在 bug。
