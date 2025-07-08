# Java CyclicBarrier 介绍

cyclic  美 /ˈsaɪklɪk/ 循环的 

Barrier 美 /ˈbæriər/ 屏障

`CyclicBarrier` 是 Java 并发包 (`java.util.concurrent`) 中的一个同步辅助类，它允许一组线程互相等待，直到所有线程都到达某个公共屏障点（barrier point）后再继续执行。

## 主要特点

1. **循环使用**：与 `CountDownLatch` 不同，`CyclicBarrier` 可以被重置并重复使用
2. **屏障点**：所有线程必须到达屏障点后才能继续执行
3. **可选操作**：可以定义一个在所有线程到达屏障后执行的操作（Runnable）

## 构造函数

```
// 创建一个CyclicBarrier，当给定数量的线程到达屏障时，屏障才会放行
CyclicBarrier(int parties)

// 创建一个CyclicBarrier，当给定数量的线程到达屏障时，执行给定的屏障操作
CyclicBarrier(int parties, Runnable barrierAction)
```

## 主要方法

- `int await()`：等待直到所有线程都到达屏障点
- `int await(long timeout, TimeUnit unit)`：带超时的等待
- `int getParties()`：返回需要跨越屏障的线程数量
- `boolean isBroken()`：查询屏障是否处于损坏状态
- `void reset()`：重置屏障到初始状态

## 使用示例

```
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        final int threadCount = 3;
        CyclicBarrier barrier = new CyclicBarrier(threadCount, () -> {
            System.out.println("所有线程已到达屏障，执行屏障操作");
        });
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Worker(barrier), "线程-" + (i + 1)).start();
        }
    }
    
    static class Worker implements Runnable {
        private final CyclicBarrier barrier;
        
        Worker(CyclicBarrier barrier) {
            this.barrier = barrier;
        }
        
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " 正在执行第一阶段工作");
                Thread.sleep((long)(Math.random() * 2000));
                System.out.println(Thread.currentThread().getName() + " 到达屏障，等待其他线程");
                barrier.await();
                
                System.out.println(Thread.currentThread().getName() + " 继续执行第二阶段工作");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
```

## 应用场景

1. 多线程计算，需要合并结果
2. 多阶段任务，需要所有线程完成前一阶段才能进入下一阶段
3. 模拟并发测试，确保所有线程同时开始执行

## 注意事项

1. 如果等待的线程被中断，屏障会被破坏（`BrokenBarrierException`）
2. 如果有一个线程在调用 `await()` 时超时，其他线程会收到 `BrokenBarrierException`
3. 重置屏障 (`reset()`) 会中断所有等待的线程

`CyclicBarrier` 是一个强大的同步工具，特别适合需要多线程分阶段协同工作的场景。