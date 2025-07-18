# JMM(Java内存模型)

## 1. JMM基本概念

Java内存模型（Java Memory Model，简称JMM）是Java虚拟机（JVM）对多线程程序中的内存访问和操作进行规范的一种抽象，并不真实存在。**它定义了线程如何与主存（共享内存）和工作内存（线程私有内存）进行交互，以及如何同步和互斥地访问共享数据。**

> 《Java虚拟机规范》中曾试图定义一种“Java内存模型”来**屏蔽各种硬件和操作系统的内存访问差异**，**以实现让Java程序在各种平台下都能达到一致的访问效果**，但这并非是一件易事，这个模型必须定义的足够严谨，才能让Java的并发内存访问操作不会产生歧义；但是也必须定义得足够宽松，使得虚拟机得实现能有足够得自由空间去利用硬件得各种特性（寄存器、高速缓存和指令集中某些特有的指令）来获取更好的执行速度。Java内存模型自JDK1.2建立起来，随后又经过长时间的验证和修补，直到JDK5（JSR-133)发布后，也就是目前正在使用的Java内存模型，才终于成熟、完善起来了。

JMM的核心目标是解决多线程环境下的三个关键问题：

- **原子性**：确保操作不可分割
- **可见性**：一个线程对共享变量的修改能及时被其他线程看到
- **有序性**：防止指令重排序导致的问题210

Java内存模型(JMM)是Java语言规范(JLS)和Java虚拟机规范(JVMS)的核心组成部分

## 2. JMM的内存结构

JMM定义了两种抽象的内存区域：

**主内存(Main Memory)**：

- 存储所有共享变量(实例字段、静态字段和数组元素)
- 所有线程都可以访问
- 对应于物理内存中的堆区

**工作内存(Working Memory)**：

- 每个线程私有的内存区域
- 存储该线程使用变量的主内存副本
- 对应于CPU缓存和寄存器

### **2.1主内存和工作内存的工作流程：**

Java内存模型规定了所有的变量都存储在主内存中，每条线程还有自己的工作内存，线程的工作内存保存了被该线程使用到的变量的主内存副本，线程对变量的所有操作（读取、赋值等）都必须在工作内存中进行，而不能直接读写主内存中的数据。不同线程之间也无法直接访问对方工作内存中的变量，线程间变量值的传递均需要通过主内存来完成。线程、主内存、工作内存三者的交互关系如下图：

![](https://i-blog.csdnimg.cn/blog_migrate/18fc7ddd36c2bf245f27e8143fa0eb6f.png)



### 2.2 内存间交互操作(了解即可)

>  ps：这里做个简单了解即可，因为除了虚拟机开发团队外，大概没有其他开发人员会以这种方式来思考并发问题。下面会介绍该部分内容的等效判断原则——先行发生规则（happens-before），相较于这种方式更容易理解。

关于主内存与工作内存之间具体的交互协议，Java虚拟机定义了8种原子操作：

* lock（锁定）：作用于主内存的变量，它把一个变量标识为一条线程独占的状态。
* unlock（解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其它线程锁定。
* read（读取）：作用于主内存的变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用。
* load（载入）：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中。
* use（使用）：作用于工作内存的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用的变量的值的字节码指令时，执行这个操作。
* assign（赋值）：作用于工作内存的变量，它把一个执行引擎接收到的值赋给工作内存的变量，每当虚拟机遇到一个给变量赋值的字节码指令时，执行这个操作。
* stroe（存储）：作用于工作内存的变量，它把工作内存中一个变量的值传送到主内存中，以便随后的write操作使用。
* write（写入）：作用于主内存的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中。

由上可见，如果要把一个变量从主内存复制到工作内存，那就要顺序执行read和load操作，而要把变量从工作内存同步回主内存，就要顺序执行strore和write操作。注意，这里是顺序执行而不代表它们会连续执行，如对主内存中的变量a、b进行访问时，可能出现的顺序是：read a、read b、load b、load a。

除此之外，针对上述8种基本操作，Java内存模型还制定了8种规则：

1. 不允许read和load、store和write操作之一单独出现，即不允许一个变量从主内存读取了但工作内存不接受，或者从工作内存发起回写了但主内存不接受的情况出现。

2. 不允许一个线程丢弃它的最近的assign操作，即变量在工作内存中改变了之后必须把该变化同步回主内存。

3. 不允许一个线程无原因地（没有发生过任何assign操作）把数据从线程的工作内存同步回主内存中。

4. 一个新的变量只能在主内存中“诞生”，不允许在工作内存中直接使用一个未被初始化（load或assign）的变量，换句话说，就是对一个变量实施use、store操作之前，必须先执行过了assign和load操作。

5. 一个变量在同一个时刻只允许一条线程对其进行lock操作，但lock操作可以被同一条线程重复执行多次，多次执行lock后，只有执行相同次数的unlock操作，变量才会被解锁。

   > synchronized的有序性,就是这条规则

6. 如果对一个变量执行lock操作，那将会清空工作内存中此变量的值，在执行引擎使用这个变量前，需要重新执行load或assign操作初始化变量的值。

7. 如果一个变量事先没有被lock操作锁定，那就不允许对它执行unlock操作，也不允许去unlock一个被其他线程锁定住的变量。

8. 对一个变量执行unlock操作之前，必须先把此变量同步回主内存中（执行store、write操作）。

   > synchronized的可见性,就是这条规则

### 2.3设计原因

1. **性能优化**：
   - 工作内存相当于CPU缓存，避免了每次读写都直接操作主内存
   - 减少了线程间的直接竞争，提高了并发效率
2. **硬件架构映射**：
   - 反映了现代计算机系统的实际架构（CPU寄存器、缓存、主内存的层次结构）
   - 工作内存对应CPU缓存，主内存对应物理内存
3. **可见性问题控制**：
   - 明确规定了线程何时必须从主内存读取数据，何时必须把数据写回主内存
   - 通过这种设计可以精确控制内存可见性
4. **指令重排序管理**：
   - 允许编译器/处理器在单个线程内进行指令重排序优化
   - 但通过happens-before规则保证最终一致性

## 3. JMM的三大特性

#### 3.1 可见性(Visibility)

可见性指当一个线程修改了共享变量的值，其他线程能够立即得知这个变更。

**实现机制**：

- `volatile`关键字：保证变量的读写直接在主内存进行

- `synchronized`和锁机制：线程释放锁前会将变量刷新到主内存

- `final`关键字：保证初始化完成后对其他线程可见

  > [final关键字的可见性保证](#final关键字的可见性保证)

#### 3.2 原子性(Atomicity)

原子性指一个操作不可中断，要么全部执行完成，要么完全不执行。

**实现机制**：

- 基本类型(除long/double)的读写是原子的

  > [基本类型原子性读写的含义解析](# 基本类型原子性读写的含义解析)

- `synchronized`和锁机制保证代码块原子性

- `java.util.concurrent.atomic`包中的原子类

**应用场景：**

应用场景来看，JVM保证原子性操作的主要方式如下：

1. synchronized关键字：虽然Java内存模型还提供了lock和unlock操作来满足原子性的需求，但并未对用户开放使用，而是提了更高层次的字节码指令monitorenter和monitorexit来隐式地使用这两个操作，也就是我们熟悉的同步块——synchronized关键字。

2. AQS的锁机制：比如ReentrantLock、ReentrantReadWriteLock等。

3. CAS实现，java.util.concurrent.atomic包下的原子操作类，比如AtomicInteger、AtomicLong等。

#### 3.3 有序性(Ordering)

有序性指程序的执行顺序按照代码的先后顺序执行。

> [指令重排序](# 指令重排序)

**实现机制**：

- `volatile`关键字禁止指令重排序
- `happens-before`原则定义操作间的顺序关系
- `synchronized`保证临界区内代码的顺序执行



## 4.Java 内存屏障

内存屏障(Memory Barrier)，也称为内存栅栏(Memory Fence)，是Java并发编程中的一个重要概念，用于控制指令执行顺序和内存可见性。

### 什么是内存屏障

内存屏障（Memory Barrier，又称内存栅栏）是一种CPU指令，用于控制指令的执行顺序，确保某些操作不会被重排序。具体来说：

### Java中的内存屏障类型

JMM 规范中定义了四种抽象的内存屏障，用于控制指令重排序和内存可见性

这些屏障是 **JVM 层面的抽象概念**，属于 JMM 规范的一部分，目的是屏蔽不同硬件平台的差异，为 Java 开发者提供一致的内存可见性保证。

依赖与 MESI（缓存一致性协议）

> [volatile](D:\PROJECT\IdeaProjects\springMVC\src\main\java\com\bigDragon\javase\concurrent\volatile\volatile.md)

#### **LoadLoad屏障**

**作用**：

* 确保屏障前的读操作先于屏障后的读操作完成。
* 确保后续读操作执行前，前面的读操作已从内存或其他处理器缓存中加载最新值。

```
// 线程1
value1 = x;   // Load1
//其他读操作。。。
<LoadLoad屏障>
value2 = y;   // Load2

// 确保Load1在Load2之前完成
```

**实际场景**：防止CPU对两个读操作进行重排序。

#### **StoreStore屏障**：

**作用**：

* 确保屏障前的写操作先于屏障后的写操作完成，都完成并刷新到主内存。
* 确保屏障前的所有写操作（结果）对其他处理器可见（刷新到主存或共享缓存）。

**伪代码表示**：

```
// 普通写操作
x = 42;         // 普通写1
y = 43;         // 普通写2
// <StoreStore屏障> (隐含)
sharedVar = 1;  // volatile写
```

**如果没有StoreStore屏障**：

- 编译器或处理器可能会将`x = 42`和`y = 43`重排序到`sharedVar = 1`之后
- 其他线程看到`sharedVar == 1`时，可能看不到`x`和`y`的更新值
- 导致"部分发布"问题，对象处于不一致状态

#### **LoadStore屏障**：

**作用**：

* 确保屏障前的读操作先于屏障后的写操作完成。
* **读操作的可见性**:屏障前的读操作（Load）必须从内存（或其他处理器的缓存）中加载最新值，而不是使用本地处理器的陈旧缓存。
* **写操作的可见性准备**:屏障后的写操作（Store）在执行时，必须确保其结果对其他处理器可见（例如通过写缓冲区刷新或缓存一致性协议）

```
// 线程1
value = x;    // Load
//其他读操作。。。
<LoadStore屏障>
y = 42;       // Store

// 确保Load在Store之前完成
```

**实际场景**：防止读操作和后续写操作重排序。

#### **StoreLoad屏障**：

**作用**：

* 确保屏障前的所有写操作对其他处理器可见，并且先于屏障后的读操作。

  > 会重新加载其他处理器的数据

* （全能屏障）：兼具其他三种屏障的功能，同时强制刷新写缓冲区并失效化缓存，保证屏障前的所有写操作对其他处理器可见，并确保后续读操作获取最新值。

```
// 线程1
x = 1;        // Store
//其他写操作。。。
<StoreLoad屏障>
value = y;    // Load

// 确保Store的结果对所有处理器可见后，才执行Load
```

**实际场景**：最严格的屏障，通常对应硬件的全屏障，volatile写操作后会插入此屏障。

#### **对比屏障的可见性保证**

| 屏障类型       | 禁止的重排序     | 可见性保证范围                          | 严格程度 |
| :------------- | :--------------- | :-------------------------------------- | :------- |
| **LoadLoad**   | Load1 → Load2    | 确保 Load2 读取最新数据                 | 低       |
| **StoreStore** | Store1 → Store2  | 确保 Store1 对其他线程可见              | 低       |
| **LoadStore**  | Load → Store     | 确保 Load 读取最新数据后才 Store        | 中       |
| **StoreLoad**  | **Store → Load** | **确保 Store 全局可见 + Load 读取最新** | **最高** |

#### 写操作与读操作

- **写操作（释放锁）**：强制将当前线程的工作内存刷新到主内存（`StoreStore` + `StoreLoad` 屏障）。
- **读操作（获取锁）**：强制从主内存重新加载变量（`LoadLoad` + `LoadStore` 屏障）。

### Java中的内存屏障实现

在Java中，内存屏障主要通过以下方式实现：

#### **volatile关键字**：

- 写volatile变量：相当于插入StoreStore屏障 + StoreLoad屏障

  - 当执行时，编译器会插入StoreStore屏障，确保value被修改之前，所有的写操作都已经完成。然后，编译器会插入StoreLoad屏障，确保在value被修改之后，所有的读操作都还没开始。

  - 完整volatile写屏障示例

    > ```java
    > // 线程1执行：
    > x = 42;          // 普通写(可能被缓存)
    > y = 43;          // 普通写(可能被缓存)
    > // StoreStore屏障：确保x=42和y=43刷新到主内存
    > // 线程2执行：
    > // 当看到sharedVar == 1时，保证也能看到x == 42和y == 43
    > sharedVar = 1;   // volatile写
    > // StoreLoad屏障：确保sharedVar=1对所有线程可见
    > ```

- 读volatile变量：相当于插入LoadLoad屏障 + LoadStore屏障
  - 当执行时，编译器会插入LoadLoad屏障，确保在读取value之前，前面的所有读操作都已经完成。然后，编译器会插入LoadStore屏障，确保在读取value之后，后面的所有写操作都还没有开始

#### **synchronized关键字**：

- 进入同步块：相当于插入LoadLoad屏障 + LoadStore屏障(同步块 **开始** 时插入 **acquire 屏障**)

- 退出同步块：相当于插入StoreStore屏障 + StoreLoad屏障(同步块 **结束** 时插入 **release 屏障**)

  > 参考见上面的读写volatile

#### **final关键字**：

- 正确构造的final字段在初始化后会插入StoreStore屏障，确保对其他线程可见

  > 在构造函数结束时，`final` 字段的写入会插入 **`StoreStore` 屏障**，确保：
  >
  > 1. `final` 字段的写入（如 `x = 42`）**一定先于**对象引用被发布（如将对象赋值给共享变量）。
  > 2. 其他线程看到的对象引用时，`final` 字段的值一定是初始化后的值（不会看到默认值或未初始化的值）。
  >
  > 示例：
  >
  > ```
  > public class Example {
  >     final int x;
  >     Example() {
  >         x = 42;  // final 字段初始化
  >         // StoreStore 屏障（隐式）
  >     }
  > }
  > 
  > // 其他线程通过正确发布的引用读取：
  > Example sharedRef = new Example(); // 正确构造
  > int value = sharedRef.x; // 保证看到 x = 42
  > ```

#### **Unsafe类**：

提供直接的内存屏障操作

```
Unsafe.getUnsafe().loadFence();    // LoadLoad + LoadStore
Unsafe.getUnsafe().storeFence();   // StoreStore + LoadStore
Unsafe.getUnsafe().fullFence();    // StoreLoad
```

### 内存屏障的作用

1. **防止指令重排序**：确保编译器和CPU不会将屏障前后的指令进行重排序
2. **保证内存可见性**：确保一个线程对共享变量的修改对其他线程立即可见
3. **保证有序性**：确保程序执行顺序符合预期

## 5.先行发生原则（happens-before）

Java内存模型(JMM)中的happen-before原则是理解Java多线程编程中内存可见性和操作顺序的关键概念。它定义了操作之间的偏序关系，确保一个操作的结果对另一个操作可见。

### happen-before原则的基本概念

happen-before关系是JMM中最核心的概念之一，它规定了：

- 如果操作A happen-before 操作B，那么A的所有写操作对B都是可见的
- 如果A不happen-before B，且B不happen-before A，那么这些操作就是"无序的"

### JMM中天然的happen-before关系

《JSR-133:Java Memory Model and Thread Specification》定义了如下happens-before规则，也是Java内存模型下“天然的”先行发生关系，这些先行发生关系无须任何同步器就已经存在，可以在编码中直接使用。如果两个操作之间的关系不在此列，并且无法从下列规则推导出来的话，它们就没有顺序性保障，虚拟机可以对它们随意地进行重排序：

1. **程序顺序规则**：同一个线程中的每个操作都happen-before该线程中在程序顺序上后续的每个操作
2. **锁规则**：对一个锁的解锁happen-before随后对这个锁的加锁
3. **volatile变量规则**：对一个volatile域的写happen-before任意后续对这个volatile域的读

> 这意味着：
>
> - 如果 **线程 A** 执行 `volatile` 写，**线程 B** 执行 `volatile` 读，那么线程 A 在 `volatile` 写之前的所有操作（包括读和写）对线程 B 都是可见的。
> - 这个规则 **比单纯的内存屏障更强**，它确保了 **整体顺序一致性**，而不仅仅是防止部分重排序。

1. **线程启动规则**：Thread对象的start()方法happen-before此线程的每一个动作
2. **线程终止规则**：线程中的所有操作都happen-before对此线程的终止检测（通过Thread.join()或Thread.isAlive()）
3. **中断规则**：对线程interrupt()方法的调用happen-before被中断线程检测到中断事件
4. **终结器规则**：对象的构造函数执行结束happen-before它的finalize()方法的开始
5. **传递性**：如果A happen-before B，且B happen-before C，那么A happen-before C

###  锁关键字-happen-before规则-内存屏障三者的协作关系

```
程序员代码 → JMM规则(happen-before) → 内存屏障插入 → 底层CPU执行
```

- 程序员使用volatile等关键字或同步结构
- JMM根据happen-before规则决定需要保证的顺序
- JVM在适当位置插入内存屏障实现这些顺序保证
- CPU执行时遵守内存屏障的约束



## as-if-serial 

`as-if-serial`（直译为 "如同串行"）是 **Java 内存模型（JMM）** 和 **CPU 执行指令** 的一个重要原则，它定义了 **单线程程序** 在指令重排序优化下的执行行为。

### **1. as-if-serial 的定义**

> **as-if-serial 语义**：
> **不管编译器和 CPU 如何对指令进行重排序（优化），单线程程序的执行结果必须和代码的原始顺序执行的结果一致。**

换句话说：

- **指令可以重排序**（编译器和 CPU 为了提高性能会进行重排序优化）
- **但重排序不能影响单线程程序的最终执行结果**（即程序的行为看起来像是按代码顺序执行一样）

### **2. 为什么需要 as-if-serial？**

现代 CPU 和编译器为了提高性能，会进行 **指令重排序（Instruction Reordering）**，例如：

- **CPU 乱序执行（Out-of-Order Execution）**：如果某些指令不依赖前一条指令的结果，CPU 可能会提前执行它们。
- **编译器优化**：编译器可能会调整指令顺序以减少指令流水线的停顿。

但 **单线程程序** 的正确性不能因为重排序而破坏，所以 JMM 规定：

> **只要单线程的执行结果不变，编译器和 CPU 可以自由优化。**

### **3. as-if-serial 示例**

**示例 1：允许重排序的情况**

```
int a = 1;      // 操作1
int b = 2;      // 操作2
int c = a + b;  // 操作3
```

- **操作1** 和 **操作2** 没有依赖关系，可以重排序：
  - 可能的执行顺序：`操作2 → 操作1 → 操作3`
  - 但最终 `c = 3`，结果不变。

**示例 2：不允许重排序的情况**

```
int a = 1;      // 操作1
int b = a + 1;  // 操作2
int c = b + 2;  // 操作3
```

- **操作2** 依赖 **操作1**，**操作3** 依赖 **操作2**，所以不能重排序：
  - 必须按 `操作1 → 操作2 → 操作3` 执行，否则结果会出错。

### **4. as-if-serial 与 happens-before 的关系**

| 对比项       | as-if-serial                          | happens-before                    |
| :----------- | :------------------------------------ | :-------------------------------- |
| **关注点**   | **单线程** 程序执行结果不受重排序影响 | **多线程** 之间的操作顺序和可见性 |
| **作用**     | 保证单线程的正确性                    | 保证多线程的正确性                |
| **优化限制** | 允许不影响单线程结果的重排序          | 建立跨线程的操作顺序约束          |

**联系**

- **happens-before 包含了 as-if-serial**：

  - happens-before 的 **程序顺序规则**（Program Order Rule）规定：

    > **同一线程内，前面的操作 happens-before 后面的操作。**

  - 这实际上就是 as-if-serial 在多线程环境下的扩展。

# 其他

[Java并发编程第5讲——volatile关键字（万字详解）](https://blog.csdn.net/weixin_45433817/article/details/132395341)

## final关键字的可见性保证

**原理分析**：
`final`关键字的可见性保证来自JMM的特殊规则：

1. **构造函数内的final字段写入**：
   - 在对象引用对其他线程可见之前，final字段的写入必须完成
   - JVM会禁止重排序，确保final字段初始化先于对象引用赋值
2. **读取final字段**：
   - 其他线程看到包含final字段的对象时，保证能看到正确初始化的final值
   - 不需要额外的同步措施

**内存屏障实现**：
JVM会在final字段赋值后插入StoreStore屏障，防止与后续操作重排序

**示例说明**：

```
public class FinalVisibility {
    private final int immutableValue;
    
    public FinalVisibility(int value) {
        this.immutableValue = value;  // 保证在构造函数完成时对其他线程可见
    }
    
    public int getValue() {
        return immutableValue;  // 无需同步，总能读取到正确初始化的值
    }
}
```

## 基本类型原子性读写的含义解析

**1. 基本概念解释**

"基本类型(除long/double)的读写是原子的"这句话的意思是：

在Java内存模型(JMM)中，对于**基本数据类型**（byte、short、int、char、float、boolean）的**单一读写操作**，虚拟机保证这些操作是不可分割的（atomic），但**long和double**这两种64位长度的基本类型除外。

**2. 原子性读写的具体表现**

**2.1 原子性操作的特点**

- **不可分割**：操作要么完全执行，要么完全不执行
- **中间状态不可见**：其他线程不会看到操作完成一半的状态
- **线程安全**：不需要额外同步措施就能保证单一读写的正确性

**2.2 典型原子操作示例**

```
int i = 10;    // 原子性写操作
int j = i;     // 原子性读操作
```

**2.3 非原子操作示例**

```
i++;          // 非原子操作，实际上是读-改-写三个步骤
i = i + 1;    // 同上，非原子操作
```

**3. long/double的特殊性**

**3.1 为什么long和double例外？**

- **64位长度**：在32位JVM上，long/double的读写可能需要两次32位操作
- **历史原因**：早期Java规范允许将64位读写拆分为两个32位操作
- **实际现状**：现代JVM通常也保证long/double的原子性，但规范仍保留例外

**3.2 解决方案**

如果需要确保long/double的原子性：

```
private volatile long counter;  // 使用volatile保证原子性
// 或
private final AtomicLong atomicCounter = new AtomicLong(); // 使用原子类
```

**4. 实际影响与验证**

**4.1 正常情况下的表现**

```
int x = 0;    // 其他线程总会看到0或新值，不会看到中间状态
boolean b = true; // 其他线程总会看到true或false，不会看到"半真半假"
```

**4.2 可能出现问题的场景（long/double）**

```
long value = 0x123456789ABCDEF0L; 
// 理论上可能被拆分为高32位和低32位分别写入
// 其他线程可能看到0x1234567800000000或0x000000009ABCDEF0这样的中间值
```

**5. Java规范的具体说明**

根据Java语言规范(JLS 17.7)：

- 对非volatile的long/double变量的写操作可能被分为两个32位的写操作
- 对非volatile的long/double变量的读操作可能被分为两个32位的读操作
- 使用volatile修饰的long/double变量保证读写原子性

**6. 编程建议**

1. **基本类型**：int等32位类型无需特殊处理
2. **long/double**：
   - 如果存在多线程共享访问，建议使用volatile
   - 或者使用AtomicLong/DoubleAdder等原子类
3. **复合操作**：即使是int等类型，++操作也不是原子的，需要同步

**7. 现代JVM的实际情况**

虽然规范允许long/double的非原子性操作，但：

- 大多数现代64位JVM实际上保证所有基本类型的原子性访问
- 但为了代码的严格可移植性，仍应按规范处理

理解这一点对编写正确的多线程程序非常重要，特别是在涉及共享变量访问时。

## 指令重排序

**什么是重排序**

定义：指在保证最终结果不受影响的前提下，可以改变程序中指令的执行顺序，以达到提高代码执行效率的效果。

具体来说，指令重排可能会包括以下几种情况：

* 编译器优化：编译器在生成目标代码时可以对指令进行重排。
* 处理器优化：处理器可以根据指令之间的依赖性重排指令的执行顺序，以便更有效地使用处理器资源。
* 内存系统优化：处理器可以利用缓存和读写缓冲区等机制来重排对内存的读和写操作。

**数据依赖性**

如果两个操作访问同一变量，且这两个操作中有一个是写操作，此时这两个操作之间就存在数据依赖性。数据依赖分为3种类型，如下图：

![](https://i-blog.csdnimg.cn/blog_migrate/c426c10e3c5290e05f3a431afa9f168a.png)

上面的三种情况，在单线程的情况下，只要重排序了两个操作的执行顺序，就会改变最终结果，因此这三种情况是不会被重排序的。

```
int a = 1;
int b = 2;
```

上面这段代码的两个操作并没有数据依赖性，改变两者的执行顺序也不会影响最终结果，因此有可能被重排序。

> ps：补充一个as-if-serial语义——不管怎么重排序，（单线程）程序的执行结果不能改变。编译器、runtime和处理器都必须遵守as-if-serial语义。为了遵守as-if-serial语义，编译器和处理器不会对存在数据依赖性关系的操作做重排序。