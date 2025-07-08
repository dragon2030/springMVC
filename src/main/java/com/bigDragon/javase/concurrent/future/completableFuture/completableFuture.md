# 简介
* CompletableFuture 是 Java 8 引入的一个强大的异步编程工具，它是 Future 接口的增强实现，提供了更丰富的功能来处理异步计算和组合多个异步操作。
* public class CompletableFuture`<T>` implements Future`<T>`, CompletionStage`<T>`
* CompletableFuture具有 事件驱动 的特性，但严格来说，它 不是标准的 Reactor 模式实现，而是更接近于 Promise 模式 或 回调驱动模型。
# CompletableFuture之前线程调用存在的小问题

平时多线程开发一般就是使用Runnable，Callable，Thread，FutureTask，ThreadPoolExecutor这些内容和并发编程息息相关。相对来对来说成本都不高，多多使用是可以熟悉这些内容。这些内容组合在一起去解决一些并发编程的问题时，很多时候没有办法很方便的去完成异步编程的操作。

* Thread + Runnable：执行异步任务，但是没有返回结果
* Thread + Callable + FutureTask：完整一个可以有返回结果的异步任务
  * 获取返回结果，如果基于get方法获取，线程需要挂起在WaitNode里
  * 获取返回结果，也可以基于isDone判断任务的状态，但是这里需要不断轮询

**上述的方式都是有一定的局限性的。**
CompletableFuture就是帮你处理这些任务之间的逻辑关系，编排好任务的执行方式后，任务会按照规划好的方式一步一步执行，不需要让业务线程去频繁的等待。

CompletionStage接口定义了任务编排的方法，执行某一阶段，可以向下执行后续阶段。异步执行的，默认线程池是ForkJoinPool.commonPool()，但为了业务之间互不影响，且便于定位问题，**强烈推荐使用自定义线程池**。
## 其他Future与CompletableFuture相比的改进
1. 不支持手动完成
  * Future 确实无法手动设置结果，但 CompletableFuture.complete() 或 completeExceptionally() 可以主动完成任务。
  * 补充说明：Future 只能通过 cancel(true) 中断任务，但无法注入已有结果。
2. 不支持回调函数
  * Future.get() 是同步阻塞的，而 CompletableFuture.thenApply()/thenAccept() 等支持异步回调。
3. 不支持链式调用
  * 强调 CompletableFuture 的函数式编程风格（如 thenCompose() 用于链式组合多个异步任务）。
4. 不支持多个Future合并

# 主要方法

## runAsync() 和 supplyAsync()

* runAsync方法和supplyAsync方法都是用来创建异步任务并返回CompletableFuture对象的静态工厂方法。它们之间的主要区别在于返回值和任务类型的不同。
* supplyAsync方法适合执行一个有返回值的异步任务，它接收一个Supplier作为参数，Supplier生成的结果会成为CompletableFuture的返回值。
* runAsync方法适合执行一个没有返回值的异步任务，它接收一个Runnable作为参数，Runnable定义了异步任务的执行逻辑，但是它没有返回值。

## join()和 get()

join()方法和get()方法都是用于获取CompletableFuture的计算结果，但它们在异常处理和返回类型上有所不同。选择哪个方法取决于你对异常的处理需求和返回结果的类型。

* join()方法不会抛出checked exception，而是将异常包装在CompletionException中抛出。
* get()方法会抛出ExecutionException和InterruptedException异常，需要显式处理或传播异常。

## 串行处理

以下方法在允许开发者在一个新的线程中执行任务，而不必等待任务完成(get方法阻塞)，从而实现非阻塞的异步编程。

### thenApply() 和 thenApplyAsync()

* thenApply和thenApplyAsync是用于处理CompletableFuture的两个重要方法，它们都用于处理异步操作的结果，并返回一个新的CompletableFuture，以便进行进一步的操作或处理。
  * thenApply方法用于在当前CompletableFuture完成后，对其结果进行处理，并返回一个新的CompletableFuture，该CompletableFuture的结果由该处理函数处理后的结果提供。
  * thenApplyAsync方法与thenApply类似，但它是异步执行的，它将处理函数提交给默认的ForkJoinPool或指定的Executor执行。这使得它适合处理那些不希望阻塞当前线程的操作，如IO操作或长时间的计算任务。

**源码解析以thenApply()为例**

```
public <U> CompletableFuture<U> thenApply(
    Function<? super T,? extends U> fn) {
    return uniApplyStage(null, fn);
}
```

**示例代码**

```
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
```

### thenAccept() 和 thenAcceptAsync

* thenAccept和thenAcceptAsync是用于处理异步任务结果的方法，它们都用于在CompletableFuture完成后执行一些操作
  * thenAccept方法接收一个Consumer作为参数，用于处理CompletableFuture的结果，但不返回任何结果。
  * thenAccept方法接收了一个Consumer，用于打印CompletableFuture的结果。由于Consumer没有返回值，因此thenAccept方法返回的是一个CompletableFuture`<Void>`，表示它本身不返回结果。
  * thenAcceptAsync方法与thenAccept类似，但它是异步执行的,不会阻塞当前线程，并且可以指定一个Executor来执行Consumer处理结果的操作。

### thenRun，thenRunAsync

* thenRun和thenRunAsync方法是用于在CompletableFuture完成后执行一些操作，而不关心前一个阶段的结果的方法。它们主要的区别在于同步执行和异步执行。

### thenCompose

* thenCompose 是 Java CompletableFuture 类中非常强大且常用的方法之一，用于处理异步操作的串行组合。它允许在一个 CompletableFuture 完成后，将其结果作为输入传递给另一个 CompletableFuture 的计算过程，实现链式调用和组合多个异步操作。

### thenApply vs thenCompose

#### 基本定义对比


| 方法           | 定义             | 返回值            | 执行线程                          |
| ---------------- | ------------------ | ------------------- | ----------------------------------- |
| thenApplyAsync | 异步执行转换函数 | 普通对象          | 默认使用 ForkJoinPool或指定线程池 |
| thenCompose    | 组合另一个       | CompletableFuture | CompletableFuture与调用线程相同   |

#### 核心区别

1. 执行方式

* thenApplyAsync：
  * 总是异步执行
  * 转换函数会在另一个线程执行
  * 适合CPU密集型操作
* thenCompose：
  * 同步组合Future
  * 不保证异步执行
  * 适合IO密集型操作链

2. 返回值

* thenApplyAsync 返回的是普通对象包装的 CompletableFuture
* thenCompose 返回的是另一个 CompletableFuture

## 并行处理

### thenCombine，thenAcceptBoth，runAfterBoth （且的关系 &&）

> 比如有任务A，任务B，任务C。任务A和任务B并行执行，等到任务A和任务B全部执行完毕后，再执行任务C。

* thenCombine,当前方式当前方式前置任务需要有返回结果，后置任务接收前置任务的结果，有返回值
* thenAcceptBoth,当前方式前置任务需要有返回结果，后置任务接收前置任务的结果，没有返回值
* runAfterBoth,当前方式前置任务不需要有返回结果，后置任务不会接收前置任务的结果，没有返回值

> 同时也有对应的Async，如thenCombineAsync，为异步执行

### applyToEither，acceptEither，runAfterEither（或的关系 ||）

> 这三个方法：比如有任务A，任务B，任务C。任务A和任务B并行执行，只要任务A或者任务B执行完毕，开始执行任务C

* applyToEither：可以接收结果并且返回结果
* acceptEither：可以接收结果没有返回结果
* runAfterEither：不接收结果也没返回结果

## allOf，anyOf

* allOf

> allOf的方式是让内部编写多个CompletableFuture的任务，多个任务都执行完后，才会继续执行你后续拼接的任务
> allOf返回的CompletableFuture是Void，没有返回结果

* anyOf：

> anyOf是基于多个CompletableFuture的任务，只要有一个任务执行完毕就继续执行后续，最先执行完的任务做作为返回结果的入参

## 结果和异常处理

* exceptionally
  exceptionally方法用于处理异步操作中发生的异常情况。它是一种特殊的异常处理方法，允许在CompletableFuture抛出异常时执行一些处理逻辑，并返回一个默认值或恢复操作，而不是直接抛出异常。
* whenComplete
  在异步操作完成后执行某些处理，无论操作是否成功或失败。它不像exceptionally专门处理异常，而是用于处理操作结果或异常的回调。
* handle
  handle方法是Java CompletableFuture 类中的一个方法，结合了 whenComplete 和 exceptionally 的功能，能够处理异步操作的结果和异常情况。它提供了一种通用的方式来处理操作的最终结果，并且在发生异常时提供一个备选的结果或执行恢复操作。

### exceptionally vs whenComplete 异常处理

* whenComplete方法不会捕获异常，它只是在操作完成后执行回调，无论是成功还是失败。它用于处理最终的结果和异常信息，但不提供替代的返回值。
* exceptionally方法专门用于处理异步操作的异常情况，提供一个备选的结果或恢复操作，以确保即使操作失败，也能返回一个默认值或继续后续操作。

# 参考博客
* https://way2j.com/a/554
> 面试题博客
