# synchronized

`synchronized` 是 Java 中用于实现线程同步的关键字，它提供了一种简单的机制来确保线程安全，防止多个线程同时访问共享资源导致的数据不一致问题。

synchronized 美/ˈsɪŋkrənaɪzd/

## 基本概念

`synchronized` 实现了互斥锁（mutex）机制，确保同一时刻只有一个线程可以执行特定的代码段或方法。

## 三种使用方式

### 1. 同步实例方法

```
public synchronized void method() {
    // 同步代码
}
```

- 锁对象是当前实例对象（this）
- 同一实例的同步方法在同一时间只能被一个线程访问

### 2. 同步静态方法

```
public static synchronized void staticMethod() {
    // 同步代码
}
```

- 锁对象是当前类的 Class 对象（如 MyClass.class）
- 所有实例的同步静态方法在同一时间只能被一个线程访问

### 3. 同步代码块

```
public void method() {
    // 非同步代码
    
    synchronized(lockObject) {
        // 同步代码块
    }
    
    // 非同步代码
}
```

- 可以指定任意对象作为锁
- 更细粒度的控制，减少同步范围提高性能

## 工作原理

1. 当线程进入 synchronized 方法/代码块时，会自动获取锁
2. 如果锁已被其他线程持有，当前线程会被阻塞，进入等待状态
3. 当锁被释放时，等待的线程会竞争获取锁
4. 执行完同步代码或方法抛出异常时，锁会自动释放

## 锁的特性

1. **可重入性**：同一个线程可以多次获取同一个锁
2. **互斥性**：同一时间只有一个线程能持有锁
3. **可见性**：解锁前对共享变量的修改对后续加锁的线程可见

## 与 Lock 接口对比

| 特性             | synchronized | Lock 接口           |
| :--------------- | :----------- | :------------------ |
| 获取方式         | 自动         | 手动调用 lock()     |
| 释放方式         | 自动         | 手动调用 unlock()   |
| 尝试非阻塞获取锁 | 不支持       | tryLock() 支持      |
| 可中断等待       | 不支持       | lockInterruptibly() |
| 公平锁           | 非公平       | 可配置公平/非公平   |
| 条件变量         | wait/notify  | 支持 Condition      |

`synchronized` 在大多数情况下已经足够，但在需要更灵活的锁控制时，可以考虑使用 `java.util.concurrent.locks.Lock` 接口及其实现类。









## synchronized主要特性

`synchronized` 是 Java 中实现线程同步的关键机制，它能够保证多线程环境下的可见性、原子性和有序性。

### 1. 可见性

**可见性**指的是当一个线程修改了共享变量的值，其他线程能够立即看到修改后的值。

**synchronized 的实现**：

1. 线程获取锁时，会清空工作内存中的共享变量，从主内存重新加载
2. 线程释放锁时，会把工作内存中的共享变量刷新回主内存
3. 遵循 happens-before 原则：解锁操作 happens-before 后续的加锁操作

**原理分析**：
`synchronized`关键字和锁机制通过以下方式保证可见性：

1. **进入同步块/获取锁时**：
   - 线程会清空工作内存中该锁保护的所有共享变量的副本
   - 从主内存重新加载这些变量的最新值
2. **退出同步块/释放锁时**：
   - 线程会将工作内存中修改过的共享变量强制刷新到主内存
   - 确保其他线程能看到最新的修改结果

**内存屏障实现**：
JVM会在`synchronized`块前后插入内存屏障指令：

- 进入同步块前：LoadLoad + LoadStore屏障
- 退出同步块后：StoreStore + StoreLoad屏障

**示例说明**：

```
public class SyncVisibility {
    private int sharedValue = 0;
    
    public synchronized void increment() {
        sharedValue++;  // 1. 获取锁时会从主内存读取最新值
                        // 2. 修改后的值在释放锁时会写回主内存
    }
}
```

### 2.原子性

**原子性**指的是一个操作是不可中断的，要么全部执行成功，要么全部不执行。

**synchronized 的实现**：

- 通过监视器锁(monitor)机制实现，同一时刻只有一个线程能够获取对象的监视器锁
- 进入 synchronized 代码块的线程会获取锁，其他线程必须等待该线程释放锁后才能进入
- 基于monitorenter和monitorexit指令实现

### 3.有序性

**有序性**指的是程序执行的顺序按照代码的先后顺序执行（在单线程视角下），防止指令重排序。

**有序性保证**

1. **进入同步块前的操作不会重排序到同步块内**
   - JVM 会确保在获取锁之前的所有操作都在获取锁之前完成
2. **同步块内的操作不会重排序到同步块外**
   - 同步块内的操作会作为一个整体执行，不会被拆散
3. **退出同步块后的操作不会重排序到同步块内**
   - 释放锁之后的操作都会在释放锁之后执行

**happens-before 关系**

`synchronized` 建立以下 happens-before 关系：

- 同一个锁的解锁操作 happens-before 后续对这个锁的加锁操作
- 这意味着前一个线程在同步块中的所有操作对后一个进入同步块的线程都是可见的

**与 volatile 的区别**

虽然 `synchronized` 和 `volatile` 都提供有序性保证，但：

- `volatile` 只保证单个变量的读写有序性
- `synchronized` 保证整个同步块内的所有操作的有序性

# synchronized实现原理

## Java对象头
在JVM中，对象在内存中的布局分为三块区域：对象头、实例数据、填充数据
* **对象头（Header）**

  对象头包含JVM管理对象所需的元数据，具体分为：

  - **Mark Word**（标记字段）：
    - 存储对象的运行时数据：哈希码（HashCode）、GC分代年龄、锁状态标志（偏向锁/轻量级锁/重量级锁）、线程持有的锁、偏向线程ID等。
    - 在32位系统占4字节，64位系统占8字节（开启压缩指针后可能为4字节）。
  - **Klass Pointer**（类型指针）：
    - 指向方法区中对象的类元数据（Klass），用于确定对象的类型。
    - 通常占4字节（64位系统开启压缩指针后）或8字节（未压缩）。
  - **数组长度**（仅数组对象）：
    - 如果是数组对象，对象头会额外包含4字节的数组长度字段。

* **实例数据（Instance Data）**		

  存储对象实际的有效信息，即代码中定义的各种字段内容：

  - **规则**：
    - 基本类型按大小存储（如`int`占4字节，`long`占8字节）。
    - 引用类型通常占4字节（64位系统开启压缩指针）或8字节。
    - 字段的排列顺序受JVM优化策略影响（如默认按字段宽度从大到小排列，或按定义顺序）。
  - **继承的字段**：子类的实例数据会包含父类的所有字段。

* 对齐填充（Padding）

  - **作用**：JVM要求对象起始地址必须是8字节的整数倍（64位系统），填充数据用于补全对象总大小以满足对齐要求。
  - **示例**：若对象头+实例数据占18字节，JVM会填充6字节，使总大小为24字节。

![](https://img-blog.csdn.net/20170603163237166?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamF2YXplamlhbg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

## 对象头中Mark Word

其中Mark Word在默认情况下存储着对象的HashCode、分代年龄、锁标记位等以下是32位JVM的Mark Word默认存储结构

| 锁状态   | 25bit        | 4bit         | 1bit是否是偏向锁 | 2bit 锁标志位 |
| -------- | ------------ | ------------ | ---------------- | ------------- |
| 无锁状态 | 对象HashCode | 对象分代年龄 | 0                | 01            |

由于对象头的信息是与对象自身定义的数据没有关系的额外存储成本，因此考虑到JVM的空间效率，Mark Word 被设计成为一个非固定的数据结构，以便存储更多有效的数据，它会根据对象本身的状态复用自己的存储空间，如32位JVM下，除了上述列出的Mark Word默认存储结构外，还有如下可能变化的结构：

![](https://img-blog.csdn.net/20170603172215966?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamF2YXplamlhbg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

其中轻量级锁和偏向锁是Java 6 对 synchronized 锁进行优化后新增加的。

## Monitor 重量级锁

主要分析一下重量级锁也就是通常说synchronized的对象锁，锁标识位为10，其中指针指向的是monitor对象（也称为管程或监视器锁）的起始地址。每个对象都存在着一个 monitor 与之关联，对象与其 monitor 之间的关系有存在多种实现方式，如monitor可以与对象一起创建销毁或当线程试图获取对象锁时自动生成，但当一个 monitor 被某个线程持有后，它便处于锁定状态。在Java虚拟机(HotSpot)中，monitor是由ObjectMonitor实现的，其主要数据结构如下（位于HotSpot虚拟机源码ObjectMonitor.hpp文件，C++实现的）

### 主要数据结构

```c++
ObjectMonitor() {
    _header       = NULL;
    _count        = 0; //计数器
    _waiters      = 0,
    _recursions   = 0;
    _object       = NULL;
    _owner        = NULL; // 指向持有ObjectMonitor对象的线程
    _WaitSet      = NULL; // 处于wait状态的线程，会被加入到_WaitSet
    _WaitSetLock  = 0 ;
    _Responsible  = NULL ;
    _succ         = NULL ;
    _cxq          = NULL ; // 竞争锁失败的线程首先进入这个单向链表（Contention List），后进先出（LIFO）
    FreeNext      = NULL ;
    _EntryList    = NULL ; // 处于等待锁block状态的线程，会被加入到该列表(双向链表-理论上维护FIFO顺序)
    _SpinFreq     = 0 ;
    _SpinClock    = 0 ;
    OwnerIsThread = 0 ;
  }
```

**核心属性**

ObjectMonitor中有两个队列，_WaitSet 和 _EntryList，用来保存ObjectWaiter对象列表( 每个等待锁的线程都会被封装成ObjectWaiter对象)_

owner指向持有ObjectMonitor对象的线程

_cxq (Contention Queue)

- 数据结构：单向链表（LIFO 顺序）
- 特点：
  - 新来的竞争线程首先会被放入这个队列
  - 采用"后进先出"（LIFO）的顺序管理
  - 在多线程激烈竞争时，这个队列会快速增长

_EntryList

- 数据结构：双向链表（理论上维护FIFO顺序但不严格保证绝对的公平性）
- 特点：
  - 存放有资格竞争锁的线程
  - 当锁释放时，优先从这里选取线程唤醒
  - 在非公平模式下，这个队列的顺序并不严格保证公平性

### 重量级锁执行过程

**1. 线程尝试获取锁**

- 通过 **CAS 操作**尝试直接获取锁。
- 若获取失败，触发**锁膨胀**过程，升级为重量级锁。

**2. 锁膨胀过程**

- JVM 为锁对象分配并初始化 **ObjectMonitor**（Monitor 对象）。
- 修改对象头，使其指向关联的 Monitor 对象。

**3. 竞争锁的线程管理**

- **成功获取锁**：

  - 线程进入 Monitor 的 **_Owner** 区域，并将 `owner` 变量设为当前线程。
  - Monitor 的计数器 `count` 加 1，表示锁的重入次数。
  - 线程执行同步代码块。

- **竞争失败**：

  - 线程进入 Monitor 的 **Entry Set**（阻塞队列），状态变为 **BLOCKED**。

    > 并不是直接进入Entry Set，先插入到[`_cxq`具体逻辑](# synchronized非公平)

  - 线程被挂起（通过操作系统互斥量实现），等待唤醒。

**4. 锁的释放与线程唤醒**

- **主动释放锁**：
  - 持有锁的线程执行完同步代码后，释放 Monitor：
    - 将 `owner` 置为 `null`，`count` 减 1。
    - 从 **Entry Set** 中唤醒一个线程（通过操作系统调度）。
  - 被唤醒的线程重新尝试获取锁。
- **调用 `wait()` 方法**：
  - 持有锁的线程调用 `wait()` 时：
    - 释放 Monitor（`owner=null`，`count` 减 1）。
    - 线程移入 **Wait Set**（等待队列），状态变为 **WAITING**。
  - 其他线程调用 `notify()`/`notifyAll()` 时，从 **Wait Set** 移出线程到 **Entry Set**，等待重新竞争锁。

![](https://img-blog.csdn.net/20170604114223462?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvamF2YXplamlhbg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



由此看来，monitor对象存在于每个Java对象的对象头中(存储的指针的指向)，synchronized锁便是通过这种方式获取锁的，也是为什么Java中任意对象可以作为锁的原因，同时也是notify/notifyAll/wait等方法存在于顶级对象Object中的原因

### 字节码层面

**synchronized底层原理javap反编译**

  * synchronized代码块底层原理：monitorenter 和 monitorexit
  * synchronized方法底层原理：ACC_SYNCHRONIZED

#### synchronized代码块底层原理

现在我们重新定义一个synchronized修饰的同步代码块，在代码块中操作共享变量i，如下

```
public class SyncCodeBlock {

   public int i;

   public void syncTask(){
       //同步代码库
       synchronized (this){
           i++;
       }
   }
}
```

编译上述代码并使用javap反编译后得到字节码如下

```
 public void syncTask();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=3, args_size=1
         0: aload_0
         1: dup
         2: astore_1
         3: monitorenter  //注意此处，进入同步方法
         4: aload_0
         5: dup
         6: getfield      #2             // Field i:I
         9: iconst_1
        10: iadd
        11: putfield      #2            // Field i:I
        14: aload_1
        15: monitorexit   //注意此处，退出同步方法
        16: goto          24
        19: astore_2
        20: aload_1
        21: monitorexit //注意此处，退出同步方法(异常退出同步块)
        22: aload_2
        23: athrow
        24: return
      Exception table:
```
从字节码中可知同步语句块的实现使用的是monitorenter 和 monitorexit 指令

其中monitorenter指令指向同步代码块的开始位置，monitorexit指令则指明同步代码块的结束位置，当执行monitorenter指令时，当前线程将试图获取 objectref(即对象锁) 所对应的 monitor 的持有权，当 objectref 的 monitor 的进入计数器为 0，那线程可以成功取得 monitor，并将计数器值设置为 1，取锁成功。如果当前线程已经拥有 objectref 的 monitor 的持有权，那它可以重入这个 monitor，重入时计数器的值也会加 1。倘若其他线程已经拥有 objectref 的 monitor 的所有权，那当前线程将被阻塞，直到正在执行线程执行完毕，即monitorexit指令被执行，执行线程将释放 monitor(锁)并设置计数器值为0 ，其他线程将有机会持有 monitor 。

值得注意的是编译器将会确保无论方法通过何种方式完成，方法中调用过的每条 monitorenter 指令都有执行其对应 monitorexit 指令，而无论这个方法是正常结束还是异常结束。为了保证在方法异常完成时 monitorenter 和 monitorexit 指令依然可以正确配对执行，编译器会自动产生一个异常处理器，这个异常处理器声明可处理所有的异常，它的目的就是用来执行 monitorexit 指令。从字节码中也可以看出多了一个monitorexit指令，它就是异常结束时被执行的释放monitor 的指令。

#### synchronized方法底层原理

方法级的同步是隐式，即无需通过字节码指令来控制的，它实现在方法调用和返回操作之中。JVM可以从方法常量池中的方法表结构(method_info Structure) 中的 ACC_SYNCHRONIZED 访问标志区分一个方法是否同步方法。当方法调用时，调用指令将会 检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，执行线程将先持有monitor（虚拟机规范中用的是管程一词）， 然后再执行方法，最后再方法完成(无论是正常完成还是非正常完成)时释放monitor。在方法执行期间，执行线程持有了monitor，其他任何线程都无法再获得同一个monitor。如果一个同步方法执行期间抛 出了异常，并且在方法内部无法处理此异常，那这个同步方法所持有的monitor将在异常抛到同步方法之外时自动释放。下面我们看看字节码层面如何实现：
```
public class SyncMethod {

   public int i;

   public synchronized void syncTask(){
           i++;
   }
}
```

使用javap反编译后的字节码如下：

```java
public synchronized void syncTask();
    descriptor: ()V
    //方法标识ACC_PUBLIC代表public修饰，ACC_SYNCHRONIZED指明该方法为同步方法
    flags: ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
      stack=3, locals=1, args_size=1
         0: aload_0
         1: dup
         2: getfield      #2                  // Field i:I
         5: iconst_1
         6: iadd
         7: putfield      #2                  // Field i:I
        10: return
```

从字节码中可以看出，**synchronized修饰的方法并没有monitorenter指令和monitorexit指令，取得代之的确实是ACC_SYNCHRONIZED标识**，该标识指明了该方法是一个同步方法，**JVM通过该ACC_SYNCHRONIZED访问标志来辨别一个方法是否声明为同步方法**，从而执行相应的同步调用。这便是synchronized锁在同步代码块和同步方法上实现的基本原理。

## Java虚拟机对synchronized的优化

在Java早期版本中，synchronized属于重量级锁，效率低下，因为监视器锁（monitor）是依赖于底层的操作系统的Mutex Lock来实现的，而操作系统实现线程之间的切换时需要从用户态转换到核心态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高，这也是为什么早期的synchronized效率低的原因。庆幸的是在Java 6之后Java官方对从JVM层面对synchronized较大优化，所以现在的synchronized锁效率也优化得很不错了，Java 6之后，为了减少获得锁和释放锁所带来的性能消耗，引入了轻量级锁和偏向锁，接下来我们将简单了解一下Java官方在JVM层面对synchronized锁的优化。

**锁的升级膨胀**

锁的状态总共有四种，无锁状态、偏向锁、轻量级锁和重量级锁。随着锁的竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，但是锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级

### 无锁状态

对象刚创建时的初始状态

### 偏向锁(Biased Locking)

【存储的对象结构以后有空再学习】

偏向锁是Java 6之后加入的新锁，它是一种针对加锁操作的优化手段，经过研究发现，在大多数情况下，**锁不仅不存在多线程竞争，而且总是由同一线程多次获得**，因此为了减少同一线程获取锁(会涉及到一些CAS操作,耗时)的代价而引入偏向锁。偏向锁的核心思想是，如果一个线程获得了锁，那么锁就进入偏向模式，此时Mark Word 的结构也变为偏向锁结构，当这个线程再次请求锁时，无需再做任何同步操作，即获取锁的过程，这样就省去了大量有关锁申请的操作，从而也就提供程序的性能。所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，毕竟极有可能连续多次是同一个线程申请相同的锁。但是对于锁竞争比较激烈的场合，偏向锁就失效了，因为这样场合极有可能每次申请锁的线程都是不相同的，因此这种场合下不应该使用偏向锁，否则会得不偿失，需要注意的是，偏向锁失败后，并不会立即膨胀为重量级锁，而是先升级为轻量级锁。下面我们接着了解轻量级锁。

### 轻量级锁

【存储的对象结构以后有空再学习】

倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段(1.6之后加入的)，此时Mark Word 的结构也变为轻量级锁的结构。轻量级锁能够提升程序性能的依据是“**对绝大部分的锁，在整个同步周期内都不存在竞争**”，注意这是经验数据。需要了解的是，轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁。

#### 自旋锁-优化手段

**轻量级锁失败后升级为重量级锁前**，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为**自旋锁的优化手段**。这是基于在大多数情况下，线程持有锁的时间都不会太长，如果直接挂起操作系统层面的线程可能会得不偿失，毕竟操作系统实现线程之间的切换时需要从用户态转换到核心态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高，因此自旋锁会假设在不久将来，当前的线程可以获得锁，因此虚拟机会让当前想要获取锁的线程做几个空循环(这也是称为自旋的原因)，一般不会太久，可能是50个循环或100循环，在经过若干次循环后，如果得到锁，就顺利进入临界区。如果还不能获得锁，那就会将线程在操作系统层面挂起，这就是自旋锁的优化方式，这种方式确实也是可以提升效率的。最后没办法也就只能升级为重量级锁了。

### 锁消除(Lock Elision)

> [锁消除（Lock Elision）和偏向锁（Biased Locking）对比详解](# 锁消除（Lock Elision）和偏向锁（Biased Locking）对比详解)

消除锁是虚拟机另外一种锁的优化，这种优化更彻底，Java虚拟机在JIT编译时(可以简单理解为当某段代码即将第一次被执行时进行编译，又称即时编译)，通过对运行上下文的扫描，去除不可能存在共享资源竞争的锁，通过这种方式消除没有必要的锁，可以节省毫无意义的请求锁时间，如下StringBuffer的append是一个同步方法，但是在add方法中的StringBuffer属于一个局部变量，并且不会被其他线程所使用，因此StringBuffer不可能存在共享资源竞争的情景，JVM会自动将其锁消除。

### 锁膨胀

`synchronized`关键字的锁机制有一个从轻量级到重量级的升级过程，这个过程被称为"锁膨胀"或"锁升级"。锁膨胀是指随着竞争情况的变化，锁从偏向锁→轻量级锁→重量级锁的升级过程。

**锁膨胀的四个阶段**

1. **无锁状态**：对象刚创建时的初始状态
2. **偏向锁**：
   - 适用于只有一个线程访问同步块的场景
   - 通过在对象头中记录线程ID来实现
   - 优点：加锁和解锁不需要额外操作，性能消耗极低
3. **轻量级锁（自旋锁）**：
   - 当有第二个线程尝试获取锁时，偏向锁升级为轻量级锁
   - 线程通过CAS操作尝试获取锁，不会立即阻塞
   - 适用于锁占用时间短、竞争不激烈的场景
4. **重量级锁**：
   - 当自旋超过一定次数（默认10次）或等待线程数超过CPU核数的一半
   - 轻量级锁升级为重量级锁
   - 线程会被挂起，进入阻塞状态，依赖操作系统互斥量实现
   - 优点：减少CPU空转，缺点：线程切换开销大

**锁膨胀过程详解**

1. **初始状态**：对象刚创建时是无锁状态
2. **第一个线程访问**：
   - JVM启用偏向锁模式（默认延迟4秒启用）
   - 通过CAS将对象头中的Mark Word替换为偏向线程ID
3. **第二个线程访问**：
   - 发现对象已偏向第一个线程
   - 撤销偏向锁，升级为轻量级锁
   - 各线程在自己的栈帧中创建锁记录(Lock Record)
   - 通过CAS竞争将对象头指向自己栈帧中的锁记录
4. **竞争加剧**：
   - 如果CAS失败次数达到阈值（自旋次数）
   - 升级为重量级锁
   - 向操作系统申请互斥量(mutex)
   - 未获取锁的线程进入阻塞状态

**为什么需要锁膨胀？**

锁膨胀机制是为了在无竞争或低竞争情况下减少同步开销，而在高竞争情况下保证稳定性：

- **偏向锁**：消除无竞争情况下的同步开销
- **轻量级锁**：减少短时间竞争下的线程阻塞
- **重量级锁**：避免长时间自旋消耗CPU资源

**锁膨胀流程图**

```
[无锁状态]
│
↓ (第一个线程访问)
[偏向锁]
│
↓ (第二个线程访问)
[撤销偏向锁] → [轻量级锁]
│
↓ (CAS失败/多线程竞争)
[重量级锁]
```



## synchronized等待唤醒机制

Java中的`synchronized`关键字配合`wait()`, `notify()`和`notifyAll()`方法实现了线程间的等待唤醒机制，这是Java多线程通信的基础方式之一。

### 核心方法wait/notifynotifyAll

1. **wait()**:
   - 使当前线程进入等待状态，并释放对象锁
   - 必须放在synchronized代码块或方法中
   - 调用后线程会进入该对象的等待池
2. **notify()**:
   - 随机唤醒一个在该对象上等待的线程
   - 被唤醒的线程会尝试重新获取对象锁
   - 必须在synchronized代码块或方法中调用
3. **notifyAll()**:
   - 唤醒所有在该对象上等待的线程
   - 这些线程会竞争对象锁
   - 同样必须在synchronized代码块或方法中调用

```java
synchronized (obj) {
       obj.wait();
       obj.notify();
       obj.notifyAll();         
 }
```

wait/notify示例代码

**基本使用模式**

```
synchronized (lockObject) {
    while (条件不满足) {
        lockObject.wait(); // 释放锁并等待
    }
    // 条件满足，执行相应操作
}

// 另一个线程中
synchronized (lockObject) {
    // 改变条件
    lockObject.notify(); // 或 notifyAll()
}
```

**生产示例**

```
public class WaitNotifyExample {
    private final Object lock = new Object();
    private boolean condition = false;

    public void doWait() throws InterruptedException {
        synchronized (lock) {
            while (!condition) {//防止虚假唤醒
                System.out.println("线程等待中...");
                lock.wait(); // 释放锁并等待
            }
            System.out.println("线程被唤醒，继续执行");
            // 执行后续操作
        }
    }

    public void doNotify() {
        synchronized (lock) {
            condition = true;
            lock.notify(); // 唤醒一个等待线程
            // lock.notifyAll(); // 唤醒所有等待线程
            System.out.println("已发出通知");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        WaitNotifyExample example = new WaitNotifyExample();
        
        Thread t1 = new Thread(() -> {
            try {
                example.doWait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread t2 = new Thread(() -> {
            example.doNotify();
        });
        
        t1.start();
        Thread.sleep(1000); // 确保t1先执行
        t2.start();
    }
}
```

**注意事项**

1. **虚假唤醒**：线程可能会在没有调用notify/notifyAll的情况下被唤醒，因此条件检查应该放在while循环中而不是if语句中

   > [虚假唤醒](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\createThread\虚假唤醒.md)

1. **锁对象**：wait()和notify()必须使用同一个对象锁
2. **异常处理**：wait()方法会抛出InterruptedException，需要进行处理
3. **notify vs notifyAll**：
   - notify()随机唤醒一个线程，效率较高
   - notifyAll()唤醒所有线程，更安全但性能较低
4. **Java 5+的替代方案**：考虑使用java.util.concurrent包中的更高级同步工具，如Condition、Lock等

这种等待唤醒机制是生产者-消费者模式等经典多线程问题的基础实现方式。

### 必须处于synchronized代码块或者synchronized方法中

所谓等待唤醒机制本篇主要指的是notify/notifyAll和wait方法，在使用这3个方法时，必须处于synchronized代码块或者synchronized方法中，否则就会抛出IllegalMonitorStateException异常，这是因为调用这几个方法前必须拿到当前对象的监视器monitor对象，也就是说notify/notifyAll和wait方法依赖于monitor对象，在前面的分析中，我们知道monitor 存在于对象头的Mark Word 中(存储monitor引用指针)，而synchronized关键字可以获取 monitor ，这也就是为什么notify/notifyAll和wait方法必须在synchronized代码块或者synchronized方法调用的原因。

### sleep比较wait

需要特别理解的一点是，与sleep方法不同的是wait方法调用完成后，线程将被暂停，但wait方法将会释放当前持有的监视器锁(monitor)，直到有线程调用notify/notifyAll方法后方能继续执行，而sleep方法只让线程休眠并不释放锁。同时notify/notifyAll方法调用后，并不会马上释放监视器锁，而是在相应的synchronized(){}/synchronized方法执行结束后才自动释放锁。

> [wait和sleep](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\createThread\wait和sleep.md)

### 等待唤醒机制对象头Monitor底层原理

**1. wait()调用流程**

1. 线程调用对象的wait()方法
2. JVM检查当前线程是否持有该对象的Monitor锁
   - 未持有则抛出IllegalMonitorStateException
3. 将线程封装成WaitObject并加入Monitor的WaitSet队列
4. 释放持有的Monitor锁
5. 将线程状态改为WAITING并挂起

**2. notify()/notifyAll()调用流程**

1. 线程调用对象的notify()方法
2. JVM检查当前线程是否持有该对象的Monitor锁
   - 未持有则抛出IllegalMonitorStateException
3. 对于notify()：
   - 从WaitSet中随机选择一个线程移动到EntryList
4. 对于notifyAll()：
   - 将WaitSet中所有线程移动到EntryList
5. 被移动的线程状态改为BLOCKED，等待获取锁

**3. 唤醒后的执行流程**

1. 当Monitor锁被释放时
2. EntryList中的线程竞争获取锁
3. 获取到锁的线程：
   - 从wait()调用处恢复执行
   - 重新检查等待条件(防范虚假唤醒)

## synchronized可重入性

在Java中，`synchronized`关键字实现的可重入锁（Reentrant Lock）是Java内置的锁机制的一个重要特性。

### 什么是可重入性

可重入性指的是同一个线程可以多次获得同一个锁而不会导致死锁。也就是说，如果一个线程已经持有了某个对象的锁，它可以再次请求获取这个锁而不会被阻塞。

### synchronized的可重入性表现

```
public class ReentrantExample {
    public synchronized void outer() {
        System.out.println("外层方法");
        inner(); // 调用另一个synchronized方法
    }
    
    public synchronized void inner() {
        System.out.println("内层方法");
    }
    
    public static void main(String[] args) {
        ReentrantExample example = new ReentrantExample();
        example.outer();
    }
}
```

在这个例子中：

1. 线程进入`outer()`方法时获取对象锁
2. 在`outer()`方法内部调用`inner()`方法时，线程再次尝试获取同一个对象的锁
3. 由于`synchronized`是可重入的，线程可以成功获取锁，不会阻塞

### 可重入性的重要性

可重入锁的设计解决了以下问题：

1. **避免死锁**：允许线程在持有锁的情况下调用其他需要同一锁的方法
2. **支持递归调用**：递归方法可以安全地使用synchronized
3. **提高灵活性**：简化了面向对象编程中方法调用的锁管理

### 可重入锁的工作流程

**第一次获取锁**

```
synchronized(obj) {
    // 第一次进入同步块
}
```

1. 检查锁标志位是否为无锁状态
2. 将对象头的 Mark Word 替换为指向 Monitor 的指针
3. 设置 Monitor 的 owner 为当前线程
4. 将锁计数器置为 1

**重入获取锁**

```
synchronized(obj) {
    synchronized(obj) {
        // 重入同步块
    }
}
```

1. 检查当前线程是否是 Monitor 的 owner
2. 如果是 owner，直接将锁计数器加 1
3. 不需要任何阻塞操作

**释放锁**

```
synchronized(obj) {
    // 同步代码
} // 退出同步块
```

1. 将锁计数器减 1
2. 如果计数器变为 0，表示完全释放锁
3. 清除 owner 字段，恢复对象头

## synchronized非公平性

`synchronized`关键字在Java中实现的锁本质上是**非公平锁**，这是其重要的特性之一。

### 一、什么是非公平锁

非公平锁是指当锁可用时，所有等待线程都有机会竞争锁，而不考虑它们等待的时间长短。新请求锁的线程可能会比等待队列中已等待的线程先获得锁。

### 二、synchronized的非公平性表现

1. **新请求可以插队**：当持有锁的线程释放锁时，新到达的线程可以与等待队列中的线程一起竞争锁（"插队"现象）
2. **不保证FIFO顺序**：等待时间长的线程不一定比等待时间短的线程先获取锁
3. **竞争机制**：所有线程通过竞争获取锁，而不是按顺序获取

### 三、实现原理

**1. 对象监视器(Monitor)结构**

```
class ObjectMonitor {
    Thread* volatile _owner;       // 当前持有锁的线程
    volatile intptr_t _recursions; // 重入次数
    ObjectWaiter* volatile _EntryList; // 等待队列
    ObjectWaiter* volatile _cxq;     // 竞争队列(后进先出)
    // ...
};
```

**2. 非公平获取流程**

1. 当锁释放时(_owner=null)，会同时唤醒_EntryList和_cxq中的线程

   > 注意：以下具体步骤不一定严格遵守
   >
   > JVM 实现并不严格保证绝对的公平性。不同的 JVM 实现可能有不同的行为。
   >
   > 具体选择哪个线程由 JVM 实现决定，可能涉及启发式算法
   >
   > 完全公平的锁实现通常会导致性能下降，因此 HotSpot JVM 在公平性和性能之间做了权衡。

2. 新来的线程可以直接尝试通过CAS获取锁

3. 被唤醒的线程和新来的线程一起竞争锁

4. 没有严格的排队顺序保证

**3. 具体步骤**

1. 新线程尝试通过CAS快速获取锁

2. 首次竞争失败：

   - 当线程第一次获取锁失败时，会被加入到 `_cxq` 队列的头部
   - 这个操作是原子的，使用CAS保证线程安全

3. 锁释放时的迁移：

   ```
   // 伪代码表示释放流程
   void exit() {
       if (_EntryList == null) {
           // 将_cxq中的线程转移到_EntryList
           _EntryList = reverse(_cxq);
           _cxq = null;
       }
       // 唤醒_EntryList中的第一个线程
       wakeup(_EntryList.head);
   }
   ```

   - 释放锁时，如果 `_EntryList` 为空，会把整个 `_cxq` 队列反转后放入 `_EntryList`
   - 反转操作将 LIFO 的 `_cxq` 转换为近似 FIFO 的顺序

**4. 两个队列的协作关系**

``` Mermaid
flowchart TD
    A[线程尝试获取锁] --> B{成功?}
    B -->|是| C[进入临界区]
    B -->|否| D[加入_cxq头部]
    E[锁释放] --> F{_EntryList为空?}
    F -->|是| G[反转_cxq到_EntryList]
    F -->|否| H[直接唤醒_EntryList线程]
    G --> I[唤醒_EntryList头部线程]
    H --> I
```



<svg role="graphics-document document" viewBox="0 0 903.7874755859375 515.5249938964844" class="flowchart mermaid-svg" xmlns="http://www.w3.org/2000/svg" width="100%" id="mermaid-svg-69" height="100%" style="max-width: 100%; transform-origin: 0px 0px; user-select: none; transform: translate(10.366px, 0px) scale(0.972431);"><g><marker orient="auto" markerHeight="8" markerWidth="8" markerUnits="userSpaceOnUse" refY="5" refX="5" viewBox="0 0 10 10" class="marker flowchart-v2" id="mermaid-svg-69_flowchart-v2-pointEnd"><path style="stroke-width: 1; stroke-dasharray: 1, 0;" class="arrowMarkerPath" d="M 0 0 L 10 5 L 0 10 z"></path></marker><marker orient="auto" markerHeight="8" markerWidth="8" markerUnits="userSpaceOnUse" refY="5" refX="4.5" viewBox="0 0 10 10" class="marker flowchart-v2" id="mermaid-svg-69_flowchart-v2-pointStart"><path style="stroke-width: 1; stroke-dasharray: 1, 0;" class="arrowMarkerPath" d="M 0 5 L 10 10 L 10 0 z"></path></marker><marker orient="auto" markerHeight="11" markerWidth="11" markerUnits="userSpaceOnUse" refY="5" refX="11" viewBox="0 0 10 10" class="marker flowchart-v2" id="mermaid-svg-69_flowchart-v2-circleEnd"><circle style="stroke-width: 1; stroke-dasharray: 1, 0;" class="arrowMarkerPath" r="5" cy="5" cx="5"></circle></marker><marker orient="auto" markerHeight="11" markerWidth="11" markerUnits="userSpaceOnUse" refY="5" refX="-1" viewBox="0 0 10 10" class="marker flowchart-v2" id="mermaid-svg-69_flowchart-v2-circleStart"><circle style="stroke-width: 1; stroke-dasharray: 1, 0;" class="arrowMarkerPath" r="5" cy="5" cx="5"></circle></marker><marker orient="auto" markerHeight="11" markerWidth="11" markerUnits="userSpaceOnUse" refY="5.2" refX="12" viewBox="0 0 11 11" class="marker cross flowchart-v2" id="mermaid-svg-69_flowchart-v2-crossEnd"><path style="stroke-width: 2; stroke-dasharray: 1, 0;" class="arrowMarkerPath" d="M 1,1 l 9,9 M 10,1 l -9,9"></path></marker><marker orient="auto" markerHeight="11" markerWidth="11" markerUnits="userSpaceOnUse" refY="5.2" refX="-1" viewBox="0 0 11 11" class="marker cross flowchart-v2" id="mermaid-svg-69_flowchart-v2-crossStart"><path style="stroke-width: 2; stroke-dasharray: 1, 0;" class="arrowMarkerPath" d="M 1,1 l 9,9 M 10,1 l -9,9"></path></marker><g class="root"><g class="clusters"></g><g class="edgePaths"><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_A_B_0" d="M177.313,62L177.313,66.167C177.313,70.333,177.313,78.667,177.39,92.388C177.468,106.108,177.624,125.217,177.702,134.771L177.78,144.325"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_B_C_0" d="M156.892,219.28L143.744,234.821C130.595,250.362,104.297,281.443,91.149,302.484C78,323.525,78,334.525,78,340.025L78,345.525"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_B_D_0" d="M198.733,219.28L211.715,234.821C224.697,250.362,250.661,281.443,263.643,302.484C276.625,323.525,276.625,334.525,276.625,340.025L276.625,345.525"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_E_F_0" d="M646.828,62L646.828,66.167C646.828,70.333,646.828,78.667,646.898,86.417C646.969,94.167,647.109,101.334,647.179,104.917L647.25,108.501"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_F_G_0" d="M603.811,232.508L588.458,245.844C573.105,259.18,542.399,285.853,527.047,304.689C511.694,323.525,511.694,334.525,511.694,340.025L511.694,345.525"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_F_H_0" d="M690.845,232.508L706.032,245.844C721.218,259.18,751.59,285.853,766.776,304.689C781.962,323.525,781.962,334.525,781.962,340.025L781.962,345.525"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_G_I_0" d="M511.694,403.525L511.694,407.692C511.694,411.858,511.694,420.192,521.9,428.286C532.106,436.379,552.517,444.234,562.723,448.161L572.929,452.088"></path><path marker-end="url(#mermaid-svg-69_flowchart-v2-pointEnd)" style="" class="edge-thickness-normal edge-pattern-solid edge-thickness-normal edge-pattern-solid flowchart-link" id="L_H_I_0" d="M781.962,403.525L781.962,407.692C781.962,411.858,781.962,420.192,771.757,428.286C761.551,436.379,741.139,444.234,730.933,448.161L720.727,452.088"></path></g><g class="edgeLabels"><g class="edgeLabel"><g transform="translate(0, 0)" class="label"><foreignObject height="0" width="0"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"></span></div></foreignObject></g></g><g transform="translate(78, 312.5249938964844)" class="edgeLabel"><g transform="translate(-8, -12)" class="label"><foreignObject height="24" width="16"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"><p style="margin: 0px; background-color: rgba(232, 232, 232, 0.8);">是</p></span></div></foreignObject></g></g><g transform="translate(276.625, 312.5249938964844)" class="edgeLabel"><g transform="translate(-8, -12)" class="label"><foreignObject height="24" width="16"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"><p style="margin: 0px; background-color: rgba(232, 232, 232, 0.8);">否</p></span></div></foreignObject></g></g><g class="edgeLabel"><g transform="translate(0, 0)" class="label"><foreignObject height="0" width="0"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"></span></div></foreignObject></g></g><g transform="translate(511.6937484741211, 312.5249938964844)" class="edgeLabel"><g transform="translate(-8, -12)" class="label"><foreignObject height="24" width="16"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"><p style="margin: 0px; background-color: rgba(232, 232, 232, 0.8);">是</p></span></div></foreignObject></g></g><g transform="translate(781.9624938964844, 312.5249938964844)" class="edgeLabel"><g transform="translate(-8, -12)" class="label"><foreignObject height="24" width="16"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"><p style="margin: 0px; background-color: rgba(232, 232, 232, 0.8);">否</p></span></div></foreignObject></g></g><g class="edgeLabel"><g transform="translate(0, 0)" class="label"><foreignObject height="0" width="0"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"></span></div></foreignObject></g></g><g class="edgeLabel"><g transform="translate(0, 0)" class="label"><foreignObject height="0" width="0"><div class="labelBkg" xmlns="http://www.w3.org/1999/xhtml" style="background-color: rgba(232, 232, 232, 0.5); display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="edgeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51); background-color: rgba(232, 232, 232, 0.8); text-align: center;"></span></div></foreignObject></g></g></g><g class="nodes"><g transform="translate(177.3125, 35)" id="flowchart-A-0" class="node default"><rect height="54" width="172" y="-27" x="-86" style="" class="basic label-container"></rect><g transform="translate(-56, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="112"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">线程尝试获取锁</p></span></div></foreignObject></g></g><g transform="translate(177.3125, 193.7624969482422)" id="flowchart-B-1" class="node default"><polygon transform="translate(-45.9375,45.9375)" class="label-container" points="45.9375,0 91.875,-45.9375 45.9375,-91.875 0,-45.9375"></polygon><g transform="translate(-18.9375, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="37.875"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">成功?</p></span></div></foreignObject></g></g><g transform="translate(78, 376.5249938964844)" id="flowchart-C-3" class="node default"><rect height="54" width="140" y="-27" x="-70" style="" class="basic label-container"></rect><g transform="translate(-40, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="80"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">进入临界区</p></span></div></foreignObject></g></g><g transform="translate(276.625, 376.5249938964844)" id="flowchart-D-5" class="node default"><rect height="54" width="157.25" y="-27" x="-78.625" style="" class="basic label-container"></rect><g transform="translate(-48.625, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="97.25"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">加入_cxq头部</p></span></div></foreignObject></g></g><g transform="translate(646.8281211853027, 35)" id="flowchart-E-6" class="node default"><rect height="54" width="108" y="-27" x="-54" style="" class="basic label-container"></rect><g transform="translate(-24, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="48"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">锁释放</p></span></div></foreignObject></g></g><g transform="translate(646.8281211853027, 193.7624969482422)" id="flowchart-F-7" class="node default"><polygon transform="translate(-81.76250076293945,81.76250076293945)" class="label-container" points="81.76250076293945,0 163.5250015258789,-81.76250076293945 81.76250076293945,-163.5250015258789 0,-81.76250076293945"></polygon><g transform="translate(-54.76250076293945, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="109.5250015258789"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">_EntryList为空?</p></span></div></foreignObject></g></g><g transform="translate(511.6937484741211, 376.5249938964844)" id="flowchart-G-9" class="node default"><rect height="54" width="212.8874969482422" y="-27" x="-106.4437484741211" style="" class="basic label-container"></rect><g transform="translate(-76.4437484741211, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="152.8874969482422"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">反转_cxq到_EntryList</p></span></div></foreignObject></g></g><g transform="translate(781.9624938964844, 376.5249938964844)" id="flowchart-H-11" class="node default"><rect height="54" width="227.65000915527344" y="-27" x="-113.82500457763672" style="" class="basic label-container"></rect><g transform="translate(-83.82500457763672, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="167.65000915527344"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">直接唤醒_EntryList线程</p></span></div></foreignObject></g></g><g transform="translate(646.8281211853027, 480.5249938964844)" id="flowchart-I-13" class="node default"><rect height="54" width="227.65000915527344" y="-27" x="-113.82500457763672" style="" class="basic label-container"></rect><g transform="translate(-83.82500457763672, -12)" style="" class="label"><rect></rect><foreignObject height="24" width="167.65000915527344"><div xmlns="http://www.w3.org/1999/xhtml" style="display: table-cell; white-space: nowrap; line-height: 1.5; max-width: 200px; text-align: center;"><span class="nodeLabel" style="fill: rgb(51, 51, 51); color: rgb(51, 51, 51);"><p style="margin: 0px;">唤醒_EntryList头部线程</p></span></div></foreignObject></g></g></g></g></g></svg>

**5. 为什么这样设计？**

1. **性能考虑**：
   - `_cxq` 使用LIFO使得热点线程更容易获得锁，利用局部性原理
   - 减少队列操作的开销（只需操作头部）
2. **折衷公平性**：
   - 通过定期将 `_cxq` 迁移到 `_EntryList` 提供基本的公平性
   - 但允许新线程插队，保持高吞吐量
3. **竞争激烈时的优化**：
   - 在大量线程竞争时，`_cxq` 能快速接纳新线程
   - 批量迁移减少同步开销







### 四、非公平锁的优缺点

优点：

1. **更高的吞吐量**：减少了线程挂起和唤醒的开销
2. **减少上下文切换**：新线程有机会直接获取锁而不必进入等待队列
3. **更好的性能**：在低竞争情况下表现更好

缺点：

1. **可能造成饥饿**：某些线程可能长时间获取不到锁
2. **不可预测性**：无法保证等待时间与获取锁的顺序关系

### 五、与ReentrantLock的公平模式对比

```
// synchronized是非公平的
synchronized(obj) { ... }

// ReentrantLock可以选择公平模式
ReentrantLock lock = new ReentrantLock(true); // 公平锁
```

### 六、性能影响

1. 在高竞争环境下，非公平锁可能造成某些线程饥饿
2. 在低竞争环境下，非公平锁性能通常优于公平锁
3. JVM对synchronized做了大量优化(偏向锁、轻量级锁等)，使得非公平性带来的影响减小

### 七、设计考量

Java选择将synchronized实现为非公平锁的主要原因是：

1. 大多数情况下性能更好
2. 更符合实际应用场景
3. 实现更简单高效

如果需要公平锁特性，可以考虑使用`ReentrantLock(true)`，但要注意公平锁会带来一定的性能开销。





# 博客
* [深入理解Java并发之synchronized实现原理](https://blog.csdn.net/javazejian/article/details/72828483?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522a3f75f0d7fc3c697ce9d94e12a615c5f%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=a3f75f0d7fc3c697ce9d94e12a615c5f&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~sobaiduend~default-2-72828483-null-null.142)
> 【主要讲了1、synchronized的详细原理 2、interrupt 3、notify/notifyAll和wait】

# 其他

## 用户态内核态切换

**用户态内核态**

* 用户态（User Mode）：
  * 在用户态运行的程序只能访问受限资源和指令集，如应用程序。用户态下运行的程序无法直接访问系统硬件和敏感资源，必须通过操作系统提供的服务来实现对系统资源的访问和操作。
* 内核态（Kernel Mode）：
  * 内核态具有更高的权限和更广泛的访问权限，可以直接访问系统硬件和所有内存空间。在内核态下运行的代码可以执行特权指令、管理硬件设备、执行中断处理等敏感操作。

**切换过程**

  * 切换从用户态到内核态或从内核态到用户态的过程涉及以下几个关键步骤：

1. 触发切换条件：
   * 当用户态的程序需要访问操作系统内核提供的服务或需要执行特权操作时，例如进行系统调用（如文件操作、网络通信）、处理异常（如内存访问异常）、处理中断（如硬件中断）等情况，就会触发从用户态到内核态的切换。
2. 保存上下文：
   * 在切换之前，操作系统会保存当前用户态的程序执行上下文（如寄存器状态、程序计数器值等），以便后续恢复执行。
3. 切换到内核态：
   * 当触发切换条件时，处理器会执行特定的切换指令，将当前程序的执行权限从用户态切换到内核态。这包括改变处理器的特权级别和访问权限。
4. 执行内核态代码：
   * 在内核态下，操作系统会执行相应的内核代码来处理请求或异常，如处理系统调用、中断服务例程等。
5. 恢复用户态：
   * 处理完请求或异常处理后，操作系统会恢复之前保存的用户态程序上下文，并将执行权限切换回用户态，使得用户程序可以继续执行。

**作用和重要性**

* 用户态和内核态切换的过程是操作系统实现多任务调度、资源管理和安全保护的基础。它确保了系统的稳定性和安全性，同时允许用户程序通过系统调用等方式间接访问和使用系统的硬件和资源。
  【用户态下的程序相互之间和对系统资源的访问受到限制，通过操作系统提供的接口（如系统调用）来间接访问和使用系统资源。操作系统可以对这些访问进行审查和控制，避免恶意或非法操作。】

## 锁消除（Lock Elision）和偏向锁（Biased Locking）对比详解

是的，**锁消除（Lock Elision）** 和 **偏向锁（Biased Locking）** 都是 JVM 针对单线程或无竞争锁场景的优化手段，但它们的作用机制和应用场景有所不同。下面从原理、触发条件和实际效果的角度对比分析这两种优化：

------

1. **锁消除（Lock Elision）**

**作用场景**

- 当 JVM 通过 **逃逸分析（Escape Analysis）** 确定某个锁对象**仅被当前线程访问**（未逃逸出线程）时，会直接移除这个锁的同步操作。
- 典型例子：单线程中使用 `StringBuffer` 的 `append()` 方法。

**实现原理**

1. **逃逸分析**：JIT 编译器分析锁对象的作用域，确认无跨线程共享。
2. **代码优化**：直接删除 `monitorenter` 和 `monitorexit` 指令，变为无锁操作。

**效果**

- **彻底消除锁开销**：同步代码退化为普通代码，无任何锁相关指令。
- **依赖 JIT 编译**：通常在方法被多次调用后触发。

**示例验证**

```
// 以下代码在单线程中运行时，StringBuffer 的锁会被消除
public void singleThreadMethod() {
    StringBuffer sb = new StringBuffer(); // 对象未逃逸
    sb.append("foo"); // 无实际同步开销
}
```

------

2. **偏向锁（Biased Locking）**

**作用场景**

- 假设锁**多数时间由同一线程持有**（无竞争），JVM 会优先使用偏向锁避免同步开销。
- 适用于**偶尔有竞争但多数时间单线程访问**的场景。

**实现原理**

1. **首次加锁**：线程通过 CAS 操作将锁对象头标记为“偏向模式”，并记录线程 ID。
2. **后续加锁**：检查线程 ID 是否匹配，若匹配则直接执行（无需原子操作）。
3. **遇到竞争**：升级为轻量级锁或重量级锁（撤销偏向状态）。

**效果**

- **减少无竞争时的开销**：同一线程重复加锁几乎无代价。
- **不彻底消除锁**：仍保留锁的框架，仅优化重入操作。

**示例验证**

```
// 同一线程多次调用同步方法时，偏向锁生效
public void biasedLockExample() {
    synchronized (lock) { // 首次加锁：偏向当前线程
        // 后续同一线程进入时，直接跳过同步检查
    }
}
```

------

3. **关键区别对比**

| **特性**     | **锁消除**                            | **偏向锁**                       |
| :----------- | :------------------------------------ | :------------------------------- |
| **优化目标** | 完全移除无用的同步操作                | 优化同一线程的重复加锁           |
| **触发条件** | 对象未逃逸出当前线程                  | 锁多数时间由同一线程持有         |
| **锁状态**   | 同步代码退化为普通代码                | 锁仍存在，但记录偏向线程 ID      |
| **适用场景** | 单线程无竞争（如局部 `StringBuffer`） | 多线程但竞争极少（如初始化阶段） |
| **开销**     | 彻底零开销                            | 首次加锁有少量开销，后续几乎为零 |

------

**4. 实际应用中的协作**

- **锁消除更彻底**：适合完全无竞争的代码（如局部变量），JVM 直接删除锁逻辑。
- **偏向锁更灵活**：适合可能但极少有竞争的代码（如懒加载的单例模式），保留锁机制但优化重入。

**协作示例**

```
public class LazySingleton {
    private static volatile LazySingleton instance;
    
    public static LazySingleton getInstance() {
        if (instance == null) {
            synchronized (LazySingleton.class) { // 偏向锁优化首次初始化
                if (instance == null) {
                    instance = new LazySingleton(); // 锁消除分析对象逃逸
                }
            }
        }
        return instance;
    }
}
```

- **第一次调用**：偏向锁标记初始化线程。
- **后续调用**：无竞争时直接返回实例（偏向锁生效），且 `instance` 的访问无锁消除（因可能逃逸出线程）。

------

**5. 如何选择？**

- **代码设计原则**：
  - 如果确定无竞争（如局部变量），优先用非同步类（如 `StringBuilder`），避免依赖 JVM 优化。
  - 若可能存在竞争但概率极低（如单例模式），可依赖偏向锁优化。
- **性能敏感场景**：
  - 通过 `-XX:-DoEscapeAnalysis` 和 `-XX:-UseBiasedLocking` 关闭优化进行对比测试。

> **总结**：锁消除和偏向锁都是 JVM 对单线程友好场景的优化，但锁消除更激进（直接删锁），而偏向锁保留锁机制以应对潜在竞争。在代码层面，仍应遵循“避免不必要的同步”原则。
