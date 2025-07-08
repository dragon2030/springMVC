# Java中的volatile关键字

`volatile`是Java中的一个关键字，用于修饰变量，主要解决多线程环境下的可见性和有序性问题，但是不保证原子。

volatile /ˈvɑːlət(ə)l/

**详细介绍**

volatile关键字可以说是Java虚拟机提供的最轻量级的同步机制，和synchronized不同，volatile是一个变量修饰符，只能用来修饰变量，用法也比较简单，只需要在声明一个可能被多线程访问的变量时，使用volatile修饰即可。

但是，在并发编程的三大特性——原子性、可见性、有序性中，volatile只能保证可见性和有序性（禁止指令重排），并不能保证原子性，而synchronized这三种特性都可以保证。

## 主要特性

### **1. 可见性**

- 当一个线程修改了volatile变量的值，这个新值会立即被刷新到主内存中
- 当其他线程读取该变量时，会直接从主内存中读取最新值，而不是使用本地缓存(工作内存)中的旧值

原理解析:

当对volatile变量进行写操作的时候，JVM会向处理器发送一条Lock前缀的指令，将这个缓存中的变量写回到系统主存中。

所以，如果一个变量被volatile修饰的话，在每次数据变化之后，其值都会被强制刷入主存。而其它处理器的缓存由于遵守了缓存一致性协议（比如MSI、MESI、MOSI、Synapse、Firefly及Dragon Protocaol等），也会把这个变量的值从主存加载到自己的缓存中，这就保证了一个volatile在并发编程中，其值在多个缓存中是可见的。

### **2. 有序性（禁止指令重排序）**

- volatile变量的读写操作不会被JVM和处理器进行指令重排序优化
- 这确保了程序执行的顺序与代码的顺序一致
- volatile的有序性特性使得它在多线程编程中成为实现轻量级同步的有效工具。

**volatile的有序性作用主要体现在以下方面：**

1. **禁止指令重排序**

对于volatile变量的读写操作：

- 在volatile写操作之前的任何读写操作都不能被重排序到volatile写之后
- 在volatile读操作之后的任何读写操作都不能被重排序到volatile读之前

**2. 内存屏障(Memory Barrier)**

JVM会在volatile变量操作前后插入内存屏障：

- 写操作前插入StoreStore屏障，后插入StoreLoad屏障
- 读操作前插入LoadLoad屏障，后插入LoadStore屏障

这些屏障阻止了特定类型的处理器重排序。

**示例**

```
int a = 1;          // 普通写
volatile int b = 2; // volatile 写
```

JVM 会保证 `a = 1` 不会被重排到 `b = 2` 之后。

## 典型使用场景

1. **状态标志**：

   ```
   volatile boolean running = true;
   
   public void stop() {
       running = false;
   }
   
   public void run() {
       while (running) {
           // 执行任务
       }
   }
   ```

2. **双重检查锁定(DCL)的单例模式**：

   ```
   class Singleton {
       private volatile static Singleton instance;
       
       public static Singleton getInstance() {
           if (instance == null) {
               synchronized (Singleton.class) {
                   if (instance == null) {
                       instance = new Singleton();
                   }
               }
           }
           return instance;
       }
   }
   ```

> 在极端情况下，上述的单例对象可能发生空指针异常
>
> instance=new Singleton();这个代码的执行过程可以简化成3步
>
> 1. JVM为对象分配一块内存M。
> 2. 在内存上为对象进行初始化。
> 3. 将内存M的地址赋值给singleton变量。
>
> 如果没有volatile 指令可以重排序就会132执行就会发生空指针

## 注意事项

- volatile **不能保证原子性**，对于复合操作(如i++)仍需使用synchronized或原子类(AtomicInteger等)
- volatile的性能优于锁，但比普通变量稍差
- volatile的使用需要谨慎，过度使用可能导致代码难以理解和维护

## volatile与synchronized的区别

| 特性     | volatile         | synchronized       |
| :------- | :--------------- | :----------------- |
| 可见性   | 保证             | 保证               |
| 原子性   | 不保证           | 保证               |
| 有序性   | 保证             | 保证               |
| 阻塞     | 不会导致线程阻塞 | 可能导致线程阻塞   |
| 适用范围 | 变量             | 变量、方法、代码块 |
| 性能     | 更高             | 较低               |

volatile是Java轻量级的同步机制，适用于一写多读等简单同步场景，复杂场景仍需使用锁或其他同步工具。

> [有了synchronized为什么还需要volatile](# 有了synchronized为什么还需要volatile)

# Java volatile 变量的底层实现

> 核心内存屏障 和 缓存一致性协议
>
> > [MESI缓存一致性协议](#MESI缓存一致性协议)
> >
> > [MESI 缓存一致性协议与内存屏障的协作关系](#MESI 缓存一致性协议与内存屏障的协作关系)

## 2.硬件层面的实现

### 内存屏障 (Memory Barrier)

JVM 通过在指令序列中插入内存屏障来实现 volatile 的语义：

1. **写操作屏障**：
   - StoreStore 屏障：确保 volatile 写之前的普通写操作先完成
   - StoreLoad 屏障：确保 volatile 写操作先于后续可能的 volatile 读/写操作
2. **读操作屏障**：
   - LoadLoad 屏障：确保 volatile 读操作先于后续所有普通读操作
   - LoadStore 屏障：确保 volatile 读操作先于后续所有普通写操作

### 具体处理器实现

不同处理器架构的实现方式不同：

- **x86/x64**：相对较强的内存模型，只需要在 volatile 写操作后插入 StoreLoad 屏障
- **ARM/PowerPC**：较弱的内存模型，需要更全面的内存屏障
- **SPARC**：TSO (Total Store Order) 模型，屏障需求介于 x86 和 ARM 之间

## 3. JVM 层面的实现

HotSpot JVM 通过以下方式实现 volatile：

1. **字节码层面**：volatile 变量的访问会生成特定的字节码指令（如 ACC_VOLATILE 标志）
2. **JIT 编译层面**：
   - 在即时编译时，根据目标平台插入适当的内存屏障指令
   - 例如在 x86 上可能使用 `lock` 前缀指令实现内存屏障效果
3. **汇编代码示例**：
   - volatile 写操作可能编译为类似 `mov + mfence` 的指令序列
   - volatile 读操作可能编译为普通 load 指令（x86 的 load 操作本身就有 acquire 语义）

## 4. 缓存一致性协议

现代多核处理器通过 MESI (Modified-Exclusive-Shared-Invalid) 等协议保证缓存一致性：

- 当一个线程修改 volatile 变量时，会引发缓存行失效
- 其他处理器核心会感知到这一变化并从主存重新加载数据

## 5. 与 happens-before 关系

volatile 建立了 happens-before 关系：

- 对一个 volatile 变量的写操作 happens-before 后续对这个变量的读操作
- 这确保了操作的有序性和可见性

## 6. 性能考虑

volatile 变量的访问比普通变量慢，因为：

- 需要插入内存屏障指令
- 可能阻止编译器的优化
- 可能导致缓存行失效

## 总结

volatile 的底层实现是 JVM 规范、编译器优化和硬件特性的综合结果，通过内存屏障和缓存一致性协议保证了变量的可见性和有序性，但牺牲了一定的性能。

# 参考博客

## [Java并发编程第5讲——volatile关键字（万字详解）](https://blog.csdn.net/weixin_45433817/article/details/132395341?ops_request_misc=%257B%2522request%255Fid%2522%253A%25228c2de3752e548cd97f95ef87f3ef4d20%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=8c2de3752e548cd97f95ef87f3ef4d20&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-132395341-null-null.142^v102^pc_search_result_base4&utm_term=volatile&spm=1018.2226.3001.4187)

# 其他

## 有了synchronized为什么还需要volatile

介绍完并发中的三大特性，我们发现在这三种特性中，synchronized总能作为其中的一种解决方案，看起来很“万能”对吧😁。不过确实是这样，绝大部分的并发控制，都能用synchronized来完成，这就是出现“遇事不决，就synchronized”的原因。

虽然synchronized很“万能“，但它毕竟是锁，那么既然是锁，它天然就具备以下缺点：

有性能损耗：虽然在JDK1.6种对synchronized做了很多优化，比如适应性自选、锁消除、锁粗化、轻量级锁和偏向锁等。但它毕竟是一种锁，所以，无论是同步方法还是代码块，在同步操作之前还是要进行加锁，同步操作之后解锁，这个加锁和解锁的过程都是有性能损耗的。
产生阻塞：无论是同步方法还是代码块，换句话说，无论是ACC_SYNCHRONIZED还是monitorenter和monitorexit都是基于Monitor实现的。基于Monitor对象，当多个线程同时访问一段同步代码时，首先会进入Entry Set，当有一个线程获取到对象的锁之后，才能进行The Owner区域，其他线程还会继续等待。并且当某个线程调用了wait方法后，会释放锁并进入Wait Set等待，所以synchronized实现的锁本质上是一种阻塞锁。
所以volatiile比synchronized的性能更好，除此之外，volatile还有一个很好的附加功能，就是可以禁止指令重排，volatile借助内存屏障来帮助其解决可见性和有序性问题，有一个典型的例子就是3.3.2小节的双重校验锁实现的单例模式，在没有volatile修饰的intance变量时，可能会发生空指针异常，有volatile修饰时就可以用volatile禁止指令重排的特性完美解决此问题。



## MESI缓存一致性协议

**MESI协议**是一种广泛应用于多核处理器系统中的**缓存一致性协议**，用于确保多个CPU核心的缓存数据保持一致。它是**缓存一致性协议**的一种实现方式，属于“写失效”（Write Invalidate）类协议，通过维护缓存行的状态来协调多核间的数据访问。

------

**MESI 名称来源**

MESI 是四种缓存行（Cache Line）状态的缩写：

1. **Modified (M)**
   - 当前缓存行已被修改（与主存不同），仅存在于当前核心的缓存中。
   - 此状态下，缓存行必须最终写回主存。
   - 其他核心若想读取该数据，需先触发当前核心的写回操作。
2. **Exclusive (E)**
   - 当前缓存行与主存一致，且仅被当前核心独占持有。
   - 可随时被修改（转为Modified状态），无需通知其他核心。
   - 其他核心读取时，状态会转为Shared。
3. **Shared (S)**
   - 当前缓存行与主存一致，且可能被多个核心共享。
   - 任一核心修改该行时，其他核心的缓存行会失效（转为Invalid）。
4. **Invalid (I)**
   - 当前缓存行无效（数据已过期或未被缓存）。
   - 读取时必须从主存或其他核心的缓存中重新加载。

------

**MESI 的工作原理**

1. **读操作**
   - 若缓存行状态为 **M/E/S**，直接读取本地缓存。
   - 若状态为 **I**，发起总线请求，从主存或其他核心缓存中加载数据，并根据其他核心的状态更新为 **S** 或 **E**。
2. **写操作**
   - 若状态为 **M**，直接写入本地缓存。
   - 若状态为 **E**，转为 **M** 并写入。
   - 若状态为 **S** 或 **I**，需通过总线广播“写失效”信号，使其他核心的该缓存行失效（转为 **I**），然后将本地状态转为 **M** 并写入。
3. **总线监听（Snooping）**
   - 所有核心通过总线监听其他核心的读写操作，根据协议规则调整自身缓存状态。
   - 例如：核心A修改某数据时，会通知其他核心将该数据的缓存行设为 **I**。

------

**MESI 的优势**

- **减少总线流量**：仅在必要时（如写共享数据时）广播失效信号。
- **支持写缓冲（Write Buffer）**：允许核心在等待失效确认时继续执行其他指令，提高并行性。
- **状态明确**：通过四种状态精确管理数据一致性，避免脏读或冲突。

------

**MESI 的局限性**

1. **总线竞争**：多核同时修改共享数据时，总线可能成为瓶颈。
2. **失效延迟**：等待其他核心确认失效时可能引入延迟（可通过写缓冲缓解）。
3. **伪共享（False Sharing）**：不同核心频繁修改同一缓存行的不同变量，导致不必要的失效。

------

**扩展变种**

- **MOESI**：增加了 **Owned (O)** 状态，允许共享已修改的数据（如AMD处理器使用）。
- **MESIF**：Intel的变种，增加了 **Forward (F)** 状态，指定一个核心负责转发共享数据。

------

总结来说，MESI协议通过状态机管理缓存行，在保证一致性的同时尽可能减少性能开销，是现代多核CPU缓存系统的核心机制之一。



## MESI 缓存一致性协议与内存屏障的协作关系

在多核 CPU 系统中，**MESI 协议**确保缓存一致性，而**内存屏障（Memory Barrier）**则用于控制指令执行顺序，防止 CPU 或编译器优化导致的数据竞争问题。两者共同作用，确保多线程程序的正确性。

------

**1. MESI 协议如何影响多线程读写**

MESI 协议通过缓存行状态管理数据一致性，但**仅保证最终一致性**，不保证**执行顺序**。例如：

- **核心 A** 修改变量 `x`（状态从 `S` → `M`），并发出总线失效信号。
- **核心 B** 读取 `x` 时，发现状态为 `I`，必须重新加载最新值。

但 MESI **不保证核心 A 的修改对核心 B 立即可见**（可能存在延迟），也不保证**指令的执行顺序**（CPU 可能乱序执行）。

------

**2. 内存屏障的作用**

内存屏障（Memory Barrier）用于：

1. **防止指令重排序**（Compiler/CPU Reordering）
   - 编译器或 CPU 可能优化指令顺序以提高性能，但破坏多线程语义。
   - 内存屏障强制屏障前后的指令按顺序执行。
2. **确保缓存一致性生效**
   - 内存屏障会**等待 MESI 协议完成**（如等待失效确认），确保数据对其他核心可见。

------

**3. `volatile` 关键字的实现（以 Java/C++ 为例）**

`volatile` 变量的读写会插入内存屏障，确保：

1. **可见性**（Visibility）：修改立即对其他线程可见（MESI 协议确保缓存一致性）。
2. **有序性**（Ordering）：防止指令重排序。

**`volatile` 写操作（Store）**

```
volatile int x = 1;
x = 2;  // volatile write
```

对应的 CPU 指令：

1. **StoreStore Barrier**（防止普通写和 `volatile` 写重排序）
   - 确保 `x = 2` 之前的普通写入（如 `y = 3`）先完成。
2. **写入 `x` 并触发 MESI 协议**
   - 如果 `x` 是共享状态（`S`），CPU 会发出总线失效信号，使其他核心的缓存行失效（`S` → `I`）。
3. **StoreLoad Barrier**（防止 `volatile` 写和后续读重排序）
   - 确保 `x = 2` 完成后，后续的读取才能执行。

**`volatile` 读操作（Load）**

```
int a = x;  // volatile read
```

对应的 CPU 指令：

1. **LoadLoad Barrier**（防止 `volatile` 读和普通读重排序）
   - 确保 `a = x` 之后的读取不会提前执行。
2. **读取 `x` 并检查 MESI 状态**
   - 如果 `x` 在其他核心被修改（状态 `M`），当前核心必须从主存或其他缓存加载最新值。
3. **LoadStore Barrier**（防止 `volatile` 读和后续写重排序）
   - 确保 `a = x` 完成后，后续的写入才能执行。

------

**4. 实际场景分析**

**示例：DCL（Double-Checked Locking）**

```
class Singleton {
    private static volatile Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {               // 第一次检查（无锁）
            synchronized (Singleton.class) {  // 加锁
                if (instance == null) {       // 第二次检查（防止重复创建）
                    instance = new Singleton(); // volatile 写
                }
            }
        }
        return instance;                     // volatile 读
    }
}
```

- **`volatile` 写**（`instance = new Singleton()`）：
  - 插入 `StoreStore` 屏障，确保对象初始化完成后再写入 `instance`。
  - 触发 MESI 协议，使其他核心的 `instance` 缓存失效。
- **`volatile` 读**（`return instance`）：
  - 插入 `LoadLoad` 屏障，确保读取的是最新值（可能从主存或其他核心加载）。

如果没有 `volatile`，由于指令重排序和缓存不一致，可能导致其他线程看到未初始化的对象。

------

**5. 不同 CPU 架构的差异**

| 架构        | MESI 实现                        | 内存屏障支持                 |
| :---------- | :------------------------------- | :--------------------------- |
| **x86**     | 强一致性（StoreLoad 屏障开销大） | `mfence`、`lfence`、`sfence` |
| **ARM**     | 弱一致性（依赖显式屏障）         | `dmb`（Data Memory Barrier） |
| **PowerPC** | 更弱一致性                       | `sync`、`lwsync`             |

- **x86**：由于硬件较强一致性，`volatile` 写仅需 `StoreLoad` 屏障（`mfence`），读通常无需额外屏障。
- **ARM**：需要显式插入 `dmb` 确保可见性。

------

**6. 总结**

| 机制           | 作用                         | 与 MESI 的关系                             |
| :------------- | :--------------------------- | :----------------------------------------- |
| **MESI 协议**  | 维护多核缓存一致性           | 确保最终数据一致，但不保证顺序和即时可见性 |
| **内存屏障**   | 控制指令顺序，等待 MESI 生效 | 强制刷新缓存，确保 `volatile` 读写立即可见 |
| **`volatile`** | 提供可见性和有序性           | 通过内存屏障触发 MESI 协议                 |

- **MESI 协议** 负责缓存一致性，但**不保证执行顺序**。
- **内存屏障** 确保指令顺序，并**等待 MESI 协议完成**（如失效确认）。
- **`volatile`** 通过内存屏障 + MESI 实现多线程安全访问。