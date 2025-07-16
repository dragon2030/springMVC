package com.bigDragon.javase.concurrent.juc;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author: bigDragon
 * @create: 2025/7/8
 * @Description:
 */
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
