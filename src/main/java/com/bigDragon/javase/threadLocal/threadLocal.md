【expungeStaleEntry(i);  // 清理过期条目 没看】
# 一、ThreadLocal的概念

* ThreadLocal 主要是做数据隔离，它是线程的局部变量， 是每一个线程所单独持有的，其他线程不能对其进行访问，相对隔离的。
* 当使用ThreadLocal维护变量的时候为每一个使用该变量的线程提供一个独立的变量副本，即每个线程内部都会有一个该变量，这样同时多个线程访问该变量并不会彼此相互影响，因此他们使用的都是自己从内存中拷贝过来的变量的副本，这样就不存在线程安全问题，也不会影响程序的执行性能。

# 二、ThreadLocal解决了什么问题？

* 解决多线程的并发访问。ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。

# 三、ThreadLocal原理
* 每个Thread对象都有一个ThreadLocalMap，当创建一个ThreadLocal的时候，就会将该ThreadLocal对象添加到该Map中，其中键就是ThreadLocal，值可以是任意类型。

* 以下是比较重要的几个方法
## get
* public T get(）：从线程上下文环境中获取设置的值。
```
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);//获取当前线程和对应的 ThreadLocalMap
    if (map != null) {
        //如果 map 存在：
        //通过 getEntry(this) 查找当前 ThreadLocal 对应的 entry
        //如果找到 entry，返回其 value
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null) {
            @SuppressWarnings("unchecked")
            T result = (T)e.value;
            return result;
        }
    }
     //如果 map 不存在或 entry 为 null：
     //调用 setInitialValue() 进行初始化
     //调用 initialValue() 获取初始值（默认返回 null，可重写）
     //创建 map 或设置初始值
    return setInitialValue();
}
private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        map.set(this, value);
    } else {
        createMap(t, value);
    }
    return value;
}
protected T initialValue() {
    return null;
}
```
* 关键点：
  * 初始值机制：可通过重写 initialValue() 自定义初始值
  * 双重检查：先检查 map 是否存在，再检查具体 entry
  * 延迟初始化：与 set() 方法保持一致的懒加载策略
## set
* public void set(T value)：将值存储到线程上下文环境中，供后续使用。
```
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);//获取该线程的 ThreadLocalMap
    if (map != null) {
        //如果 map 已存在：
        //调用 map.set(this, value) 设置键值对
        //key 是当前 ThreadLocal 实例（this），value 是要存储的值
        map.set(this, value);
    } else {
        //如果 map 不存在：
        //调用 createMap() 初始化 ThreadLocalMap
        //创建时将当前 ThreadLocal 和初始值作为第一个条目
        createMap(t, value);
    }
}
//获取该线程的 ThreadLocalMap
ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
//每个 Thread 对象内部都有 threadLocals 变量（延迟初始化）
public class Thread implements Runnable {
    ThreadLocal.ThreadLocalMap threadLocals = null;
}

void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```
* 关键点：
  * 懒加载机制：ThreadLocalMap 在第一次 set/get 时才创建
  * 键的设计：使用 ThreadLocal 实例作为 key（弱引用）
  * 线程隔离：每个线程操作自己的 ThreadLocalMap
## remove
* public void remove()：清除线程本地上下文环境。
```
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());//获取当前线程的 ThreadLocalMap
    if (m != null) {
        m.remove(this);//如果 map 存在,调用 map.remove(this) 删除条目
    }
}
// ThreadLocalMap 中的 remove 方法
private void remove(ThreadLocal<?> key) {
    Entry[] tab = table;
    int len = tab.length;
    int i = key.threadLocalHashCode & (len-1);//计算初始槽位
    for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
        if (e.get() == key) {
            e.clear();  // 清除弱引用
            expungeStaleEntry(i);  // 清理过期条目
            return;
        }
    }
}
private static int nextIndex(int i, int len) {
    return ((i + 1 < len) ? i + 1 : 0);
}
```
* ThreadLocalMap 的线性探测是通过 循环+条件判断 实现的完整环形查找,for 循环的每次迭代都会：
  * 通过 nextIndex() 获取下一个位置
  * 当到达数组末尾时跳回索引 0
  * 继续查找直到遇到 null 或找到匹配项

## 底层数据结构 ThreadLocalMap
* Entry 定义：
```
static class Entry extends WeakReference<ThreadLocal<?>> {
   Object value;
   Entry(ThreadLocal<?> k, Object v) {
       super(k);  // 关键：key是弱引用
       value = v;
   }
}
```
* 哈希冲突解决：
   使用线性探测法（开放寻址）
   
   探测函数：nextIndex() 和 prevIndex()
   
   自动清理过期条目（key为null的entry）
