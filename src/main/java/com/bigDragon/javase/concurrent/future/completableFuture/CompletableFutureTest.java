package com.bigDragon.javase.concurrent.future.completableFuture;

import com.bigDragon.javase.ioStream.caseRecord.CommonUtil;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 CompletableFuture 是 Java 中用于异步编程的类，Future 接口的增强实现，提供了强大的 API，支持异步操作的组合、链式调用和异常处理，有效解决了传统回调地狱问题。
 执行
 runAsync
 supplyAsync
 
 串行
 thenApply
 thenAccept
 thenRun
 thenCompose
 
 并行
 且
 thenCombine，thenAcceptBoth，runAfterBoth
 或
 applyToEither，acceptEither，runAfterEither
 
 异常和结果
 exceptionally，whenComplete，handle
 */
public class CompletableFutureTest {
    public static void main (String[] args) throws Exception{
        //runAsync supplyAsync
        new CompletableFutureTest().runAsync();
        new CompletableFutureTest().supplyAsync();
        
        //串行
        //thenApply thenApplyAsync
        new CompletableFutureTest().thenApply();
        new CompletableFutureTest().thenApplyAsync();
        //thenAccept() 和 thenAcceptAsync
        new CompletableFutureTest().thenAccept();
        new CompletableFutureTest().thenAcceptAsync();
        //thenRun，thenRunAsync
        new CompletableFutureTest().thenRun();
        //thenCompose
        new CompletableFutureTest().thenCompose();
        
        //并行
        //thenCombine，thenAcceptBoth，runAfterBoth （且的关系 &&）
        new CompletableFutureTest().thenCombine();
        //applyToEither，acceptEither，runAfterEither（或的关系 ||）
        new CompletableFutureTest().applyToEither();
        
        //异常和结果
        //exceptionally，whenComplete，handle
        new CompletableFutureTest().exceptionally();
        new CompletableFutureTest().whenComplete();
        new CompletableFutureTest().handle();
    }
    @Test
    public void thenCompose(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 2);
        //public <U> CompletableFuture<U> thenCompose(
        //        Function<? super T, ? extends CompletionStage<U>> fn)
        CompletableFuture<Integer> resultFuture = future.thenCompose(value ->
                CompletableFuture.supplyAsync(() -> value * 3)
        );
    
        resultFuture.thenAccept(result -> System.out.println("Final Result: " + result));
        //Final Result: 6
    }
    @Test
    public void handle(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Error occurred");
            }
            return 100;
        });
    
        CompletableFuture<String> resultFuture = future.handle((result, ex) -> {
            if (ex != null) {
                System.out.println("Operation failed: " + ex.getMessage());
                return "Default Value"; // 提供一个默认值作为恢复操作
            } else {
                return "Result: " + result; // 处理成功的结果
            }
        });
    
        String result = resultFuture.join();
        System.out.println("Final Result: " + result);
        //Operation failed: java.lang.RuntimeException: Error occurred
        //Final Result: Default Value
    }
    
    //whenComplete
    @Test
    public void whenComplete(){
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Error occurred");
            }
            return 100;
        });
    
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Operation failed: " + ex.getMessage());
                // 可以进行重试或其他处理逻辑
            } else {
                System.out.println("Operation succeeded, result: " + result);
                // 可以处理成功后的结果，例如持久化、下一步操作等
            }
        });
        //Exception occurred: java.lang.RuntimeException: Error occurred
        //或者返回 Result: Hello
    }
    //exceptionally
    @Test
    public void exceptionally(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Error occurred");
        });
        
        //public CompletableFuture<T> exceptionally(
        //        Function<Throwable, ? extends T> fn)
        CompletableFuture<String> resultFuture = future.exceptionally(ex -> {
            System.out.println("Exception occurred: " + ex.getMessage());
            return "Default Value"; // 提供一个默认值作为恢复操作
        });
        
        String result = resultFuture.join();
        System.out.println("Result: " + result);
        //Exception occurred: java.lang.RuntimeException: Error occurred
        //Result: Default Value
    }
    //applyToEither，acceptEither，runAfterEither（或的关系 ||）
    @Test
    public void applyToEither() throws Exception{
        CompletableFuture<Integer> taskC = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务A");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 78;
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("任务B");
            return 66;
        }), resultFirst -> {
            System.out.println("任务C");
            return resultFirst;
        });
    
        System.out.println(taskC.join());
        //任务A
        //任务B
        //任务C
        //66
    }    
    //thenCombine，thenAcceptBoth，runAfterBoth （且的关系 &&）
    @Test
    public void thenCombine() throws Exception{
        //CompletableFuture<V> thenCombine(
        //        CompletionStage<? extends U> other,
        //        BiFunction<? super T,? super U,? extends V> fn)
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务A执行");
            return 10;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            System.out.println("任务B执行");
            return 10;
        }), (r1, r2) -> {
            System.out.println("任务C执行");
            return r1 + r2;
        });
        System.out.println("任务C结果=" + future.join());
        //任务A执行
        //任务B执行
        //任务C执行
        //任务C结果=20
    }
    @Test
    public void thenRun() throws Exception{
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
    
        CompletableFuture<Void> thenRunFuture = future.thenRun(() -> {
            System.out.println("Task completed");
        });
    }
    @Test
    public void thenAcceptAsync() throws Exception{
        System.out.println("主线程："+Thread.currentThread());
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync执行线程："+Thread.currentThread());
            return "Hello";
        });
        //CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action)
        CompletableFuture<Void> thenAcceptFuture = future.thenAcceptAsync(result -> {
            System.out.println("thenAcceptAsync执行线程："+Thread.currentThread());
            System.out.println("Received result: " + result);
        });
        //主线程：Thread[main,5,main]
        //supplyAsync执行线程：Thread[ForkJoinPool.commonPool-worker-1,5,main]
        //thenAcceptAsync执行线程：Thread[ForkJoinPool.commonPool-worker-1,5,main]
        //Received result: Hello
    }
    //thenAccept
    @Test
    public void thenAccept() throws Exception{
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Void> thenAcceptFuture = future.thenAccept(result -> {
            System.out.println("Received result: " + result);
        });
        //Received result: Hello
    }
    @Test
    public void thenApplyAsync() throws Exception{
        //thenApply() 和 thenApplyAsync()
        System.out.println("主线程："+Thread.currentThread());
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync执行线程："+Thread.currentThread());
            return "Hello";
        });
        //<U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn) {
        CompletableFuture<Integer> lengthFuture = future.thenApplyAsync(s -> {
            System.out.println("thenApply执行线程："+Thread.currentThread());
            return s.length();
        });
        
        // 等待异步任务完成并获取结果
        Integer length = lengthFuture.get();
        System.out.println("Length of 'Hello': " + length); // 输出：Length of 'Hello': 5
        //返回结果
        //主线程：Thread[main,5,main]
        //supplyAsync执行线程：Thread[ForkJoinPool.commonPool-worker-1,5,main]
        //thenApply执行线程：Thread[ForkJoinPool.commonPool-worker-1,5,main]【证明用了异步线程执行了后续操作】
        //Length of 'Hello': 5
    }
    @Test
    public void thenApply() throws Exception{
        //thenApply() 和 thenApplyAsync()
        System.out.println("主线程："+Thread.currentThread());
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("supplyAsync执行线程："+Thread.currentThread());
            return "Hello";
        });
    
        CompletableFuture<Integer> lengthFuture = future.thenApply(s -> {
            System.out.println("thenApply执行线程："+Thread.currentThread());
            return s.length();
        });

        // 等待异步任务完成并获取结果
        Integer length = lengthFuture.get();
        System.out.println("Length of 'Hello': " + length); // 输出：Length of 'Hello': 5
        //返回结果
        //主线程：Thread[main,5,main]
        //supplyAsync执行线程：Thread[ForkJoinPool.commonPool-worker-1,5,main]
        //thenApply执行线程：Thread[main,5,main]
        //Length of 'Hello': 5
    }
    @Test
    public void supplyAsync() throws Exception{
        // 使用supplyAsync方法创建一个CompletableFuture对象，并执行异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 模拟一个耗时操作，比如网络请求或长时间计算
            try {
                Thread.sleep(2000); // 休眠2秒钟，模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, CompletableFuture!";
        });
    
        // 在CompletableFuture完成后获取结果
        String result = future.get(); // get方法会阻塞当前线程，直到异步任务完成并返回结果
        System.out.println(result); // 输出：Hello, CompletableFuture!
    }
    @Test
    public void runAsync() throws Exception{
        //public static CompletableFuture<Void> runAsync(Runnable runnable)
        //CompletableFuture<Void> runAsync(Runnable runnable,Executor executor) 可以指定线程池
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 模拟一个耗时操作，比如网络请求或长时间计算
            try {
                Thread.sleep(2000); // 休眠2秒钟，模拟耗时操作
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Async task completed!"); // 异步任务完成后输出消息
        });
        // 等待CompletableFuture完成
        future.get(); // get方法会阻塞当前线程，直到异步任务完成
        System.out.println("Main thread continues..."); // 异步任务完成后继续执行主线程
    }
}
