completablefuture 和 future 的区别
CompletableFuture 与 Future 的区别
CompletableFuture 和 Future 都是 Java 中用于异步编程的接口，但它们在功能和使用方式上有显著差异。以下是它们的核心区别：
{CompletableFuture 是 Java 8 中的一个类，它实现了 Future 接口，并提供了一些额外的方法来处理异步编程。}
1. 基本概念差异
   特性	Future	CompletableFuture
   引入版本	Java 5 (2004)	Java 8 (2014)
   接口/类	接口	具体实现类
   异步结果获取方式	阻塞式(get())	非阻塞式(回调)
2. 功能对比
   Future 的局限性
   阻塞获取结果：必须调用get()方法阻塞等待结果

无法手动完成：不能主动设置完成状态或值

无回调机制：结果准备好时无法自动通知

无法链式组合：难以将多个异步操作组合起来

异常处理有限：异常只能通过get()抛出

CompletableFuture 的优势
非阻塞回调：通过thenApply(), thenAccept()等注册回调

手动完成控制：可以主动完成(complete())或异常完成(completeExceptionally())

组合操作：支持多个异步操作的链式组合(thenCompose(), thenCombine())

异常处理：专门的异常处理方法(exceptionally(), handle())

多任务组合：支持allOf(), anyOf()等多任务协调

3. 使用示例对比
   Future 示例
   java
   复制
   ExecutorService executor = Executors.newCachedThreadPool();
   Future<String> future = executor.submit(() -> {
   Thread.sleep(1000);
   return "Result";
   });

// 阻塞等待结果
try {
String result = future.get(); // 阻塞
System.out.println(result);
} catch (Exception e) {
e.printStackTrace();
}
CompletableFuture 示例
java
复制
CompletableFuture.supplyAsync(() -> {
try {
Thread.sleep(1000);
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
return "Result";
}).thenAccept(result -> {
System.out.println(result); // 非阻塞回调
}).exceptionally(ex -> {
System.out.println("Error: " + ex.getMessage());
return null;
});
4. 高级特性 (仅 CompletableFuture 具备)
   流水线操作：

java
复制
CompletableFuture.supplyAsync(() -> "Hello")
.thenApply(s -> s + " World")
.thenApply(String::toUpperCase)
.thenAccept(System.out::println);
组合多个Future：

java
复制
CompletableFuture<String> cf1 = getDataAsync();
CompletableFuture<String> cf2 = getMoreDataAsync();

cf1.thenCombine(cf2, (r1, r2) -> r1 + r2)
.thenAccept(System.out::println);
超时控制 (Java 9+):

java
复制
future.orTimeout(1, TimeUnit.SECONDS)
.exceptionally(ex -> "Timeout occurred");
5. 性能考虑
   CompletableFuture 默认使用ForkJoinPool.commonPool()

对于阻塞IO操作，建议指定自定义线程池：

java
复制
ExecutorService executor = Executors.newFixedThreadPool(10);
CompletableFuture.supplyAsync(() -> dbQuery(), executor);
总结
Future 是基础的异步编程接口，而 CompletableFuture 是其功能强大的扩展，提供了：

非阻塞的回调机制

灵活的异步任务组合能力

更完善的异常处理

丰富的结果转换方法

在新的Java项目中，除非有特殊兼容性要求，否则推荐使用CompletableFuture来实现更优雅高效的异步编程。
