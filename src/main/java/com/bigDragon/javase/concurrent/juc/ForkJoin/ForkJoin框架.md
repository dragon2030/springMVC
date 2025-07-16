# Java Fork/Join框架介绍

Fork/Join框架是Java 7引入的一个用于并行执行任务的框架，它特别适合处理可以递归分解的任务。这个框架是ExecutorService接口的一个实现，能够高效地利用多核处理器。

## 核心概念

1. **分而治之(Divide and Conquer)**：将大任务拆分成小任务，直到任务足够小可以直接解决
2. **工作窃取(Work-Stealing)**：空闲线程可以从其他线程的任务队列中"窃取"任务执行，提高CPU利用率

## 主要组件

1. **ForkJoinPool**：特殊的线程池，管理线程和任务
2. **ForkJoinTask**：表示任务的抽象基类，有两个子类：
   - RecursiveAction：用于不返回结果的任务
   - RecursiveTask：用于返回结果的任务

## 关键方法

1. **fork()**：将任务异步推入工作队列
2. **join()**：等待任务完成并返回结果
3. **invoke()**：开始执行任务并等待结果

## 用法示例

```
public class ForkJoinDemo extends RecursiveTask<Long> {
    private static final long THRESHOLD = 10000; // 阈值
    private final long start;
    private final long end;
    
    public ForkJoinDemo(long start, long end) {
        this.start = start;
        this.end = end;
    }
    
    @Override
    protected Long compute() {
        long length = end - start;
        if (length <= THRESHOLD) {
            // 直接计算
            long sum = 0;
            for (long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            // 拆分任务
            long middle = start + length / 2;
            ForkJoinDemo left = new ForkJoinDemo(start, middle);
            ForkJoinDemo right = new ForkJoinDemo(middle + 1, end);
            
            left.fork();  // 异步执行左半部分
            long rightResult = right.compute(); // 同步执行右半部分
            long leftResult = left.join(); // 等待左半部分结果
            
            return leftResult + rightResult;
        }
    }
    
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinDemo task = new ForkJoinDemo(1, 100000000L);
        Long result = pool.invoke(task);
        System.out.println("计算结果: " + result);
    }
}
```

## 示例执行流程详解

（计算1-100,000,000的和）

**1. 初始调用**

- `main` 方法创建 `ForkJoinPool` 和初始任务 `(1, 100000000)`
- `pool.invoke(task)` 开始执行

**2. 第一层 compute() 调用**

- 检查范围：100,000,000 - 1 = 99,999,999 > THRESHOLD(10,000)
- 计算中点：middle = 1 + 99,999,999/2 ≈ 50,000,000
- 创建两个子任务：
  - left: (1, 50,000,000)
  - right: (50,000,001, 100,000,000)

**3. 任务拆分和执行**

- `left.fork()`：将左半部分(1-50,000,000)推入工作队列（异步）
- `right.compute()`：立即开始计算右半部分(50,000,001-100,000,000)

**4. 右半部分的递归计算**

右半部分(50,000,001-100,000,000)的 compute() 调用：

- 范围：100,000,000 - 50,000,001 = 49,999,999 > 10,000
- 继续拆分：
  - left: (50,000,001, 75,000,000)
  - right: (75,000,001, 100,000,000)
- left.fork()
- 直接计算 right(75,000,001-100,000,000)

这个过程会一直递归下去，直到：

- 某个子任务的范围 ≤ 10,000
- 例如最终会拆分成类似 (75,000,001-75,010,000) 这样的块

**5. 左半部分的并行计算**

与此同时，最初 fork() 的左半部分(1-50,000,000)：

- 被工作线程从队列中取出
- 同样进行递归拆分：
  - 拆分为 (1-25,000,000) 和 (25,000,001-50,000,000)
  - 继续拆分直到范围 ≤ 10,000

**6. 结果合并**

每一层的 compute() 方法都会：

1. 等待 fork() 的子任务完成（通过 join()）
2. 将左右两部分的结果相加
3. 返回给上一级

**7. 最终结果**

所有子任务完成后，最初的 compute() 调用会返回：
左半部分(1-50,000,000)的和 + 右半部分(50,000,001-100,000,000)的和

### 关键点说明

1. **递归拆分**：
   - 每次任务范围 > 10,000 就会继续二分
   - 例如 1-100,000,000 → 1-50,000,000 + 50,000,001-100,000,000
   - 然后 1-50,000,000 → 1-25,000,000 + 25,000,001-50,000,000
   - 直到范围 ≤ 10,000
2. **并行执行**：
   - fork() 的子任务会被其他工作线程并行处理
   - compute() 的调用会在当前线程同步执行
3. **工作窃取**：
   - 如果某个线程完成了自己的任务，会从其他线程的队列尾部"偷"任务执行
   - 这确保了所有CPU核心都能充分利用
4. **阈值选择**：
   - 10,000 是一个经验值
   - 太小会导致过多任务调度开销
   - 太大会导致负载不均衡

### 可视化执行流程

```
初始任务: 1-100,000,000
├─ fork左: 1-50,000,000 (异步执行)
│   ├─ fork左: 1-25,000,000
│   │   ├─ ... (继续拆分)
│   │   └─ 直接计算小范围
│   └─ compute右: 25,000,001-50,000,000
│       ├─ ... (继续拆分)
│       └─ 直接计算小范围
└─ compute右: 50,000,001-100,000,000 (同步执行)
    ├─ fork左: 50,000,001-75,000,000
    │   ├─ ... (继续拆分)
    │   └─ 直接计算小范围
    └─ compute右: 75,000,001-100,000,000
        ├─ ... (继续拆分)
        └─ 直接计算小范围
```

这种分治策略使得大规模计算能够高效地利用多核CPU，每个小任务都可以并行处理，最后再合并结果。

## 特点与优势

1. **高效利用多核CPU**：通过工作窃取算法平衡负载
2. **适合递归任务**：特别是可以分解的任务
3. **减少线程竞争**：每个线程有自己的工作队列
4. **自动任务拆分**：框架自动处理任务的分解与合并

## 适用场景

1. 大规模数据处理
2. 递归算法实现（如归并排序、快速排序）
3. 数学计算（如矩阵运算）
4. 任何可以分解为更小子任务的问题

## 注意事项

1. 任务不应执行I/O操作或同步操作
2. 任务应避免修改共享数据
3. 合理设置阈值，避免任务过小导致过多开销
4. 不是所有问题都适合Fork/Join，对于简单循环可能传统方法更高效

Fork/Join框架是Java并发编程中的一个强大工具，特别适合处理可分解的CPU密集型任务。